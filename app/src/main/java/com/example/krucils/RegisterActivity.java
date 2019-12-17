package com.example.krucils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity{


    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private TextView userRegister,passRegister,emailRegister;
    private Button signupBtn;
    private FirebaseFirestore db;
    private static String z;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();
        userRegister= findViewById(R.id.inputregister_username);
        emailRegister= findViewById(R.id.inputregister_email);
        passRegister=findViewById(R.id.inputregister_password);
        signupBtn=findViewById(R.id.btn_signup);
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
                    Toast.makeText(RegisterActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });


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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");



                            //set result buat di cek di profil fragment
                            //input data to database

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernamefinal)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });


                            //buat nginput sesuatu yang mantap gan
                            Map<String, Object> data = new HashMap<>();

                            data.put("UID", user.getUid());
                            data.put("email", user.getEmail());
                            //todo coba cari cara buat manggil manual jangan di paksa gan
                            data.put("username", usernamefinal);
                            data.put("admin", false);

                            //apply ke database
                            //masukin ke dalam document dengan judul UID di koleksi users
                            db.collection("users").document(user.getUid()).set(data);

                            Toast.makeText(RegisterActivity.this, "Register success.",
                                    Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                            //todo kalo register udah selesai kira kira mau ngapain langkah selanjutnya ?

                            //kalo email duplicate register gagal

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e){
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Email sudah terdaftar",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthWeakPasswordException e){
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Password harus lebih dari enam karakter.",
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
