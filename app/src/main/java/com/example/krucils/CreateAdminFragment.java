package com.example.krucils;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.krucils.objek.Materi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class CreateAdminFragment extends Fragment{
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private EditText userRegister,passRegister,emailRegister;
    private Button signupBtn;
    private FirebaseFirestore db;
    private static String z;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_admin, container, false);

        mAuth = FirebaseAuth.getInstance();
        userRegister= v.findViewById(R.id.inputregister_username);
        emailRegister= v.findViewById(R.id.inputregister_email);
        passRegister=v.findViewById(R.id.inputregister_password);
        signupBtn=v.findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userRegister.getText().toString();
                String email = emailRegister.getText().toString();
                String password = passRegister.getText().toString();
                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    createAccount(username, email, password);
                } else {
                    Toast.makeText(getActivity(), R.string.field_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }



    private void createAccount(String username, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        // if (!validateForm()) {
        //    return;
        // }
        //    showProgressDialog();
        final String usernamefinal= username;



        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");



                            //set result buat di cek di profil fragment
                            //input data to database

                            FirebaseUser user = mAuth.getCurrentUser();


                            //buat nginput sesuatu yang mantap gan
                            Map<String, Object> data = new HashMap<>();

                            data.put("UID", user.getUid());
                            data.put("email", user.getEmail());
                            //todo coba cari cara buat manggil manual jangan di paksa gan
                            data.put("username", usernamefinal);
                            data.put("admin", true);

                            //apply ke database
                            //masukin ke dalam document dengan judul UID di koleksi users
                            db.collection("users").document(user.getUid()).set(data);

                            Toast.makeText(getActivity(), "Register success.",
                                    Toast.LENGTH_SHORT).show();

                            emailRegister.setText(null);
                            passRegister.setText(null);
                            userRegister.setText(null);
                            //todo kalo register udah selesai kira kira mau ngapain langkah selanjutnya ?

                            //kalo email duplicate register gagal

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e){
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                Toast.makeText(getActivity(), "Email sudah terdaftar",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthWeakPasswordException e){
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                Toast.makeText(getActivity(), "Password harus lebih dari enam karakter.",
                                        Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }

                        // [START_EXCLUDE]
                        //          hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

}