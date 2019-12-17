package com.example.krucils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView UsernameField, PassField, registerBtn;
    private Button loginBtn;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UsernameField=findViewById(R.id.input_email);
        PassField=findViewById(R.id.input_password);
        loginBtn= findViewById(R.id.btn_login);
        registerBtn=findViewById(R.id.link_signup);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO login maunya pake email doang atau bisa username juga ?
                String username = UsernameField.getText().toString();
                String password = PassField.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    signIn(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
                }

            }

        });

        //buat register dari dont have an account yet
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        //    if (!validateForm()) {
        //       return;
        //   }

        //     showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String currentuserUID = user.getUid();
                            //bikin pindah ke fragment yang tadi
                            //ini udah pake finish sih

                            //set result buat di cek di profil fragment
                            setResult(Activity.RESULT_OK);
                            getRole(currentuserUID);


                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e){
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Email tidak terdaftar",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Cek lagi email dan password anda.",
                                        Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }

                        // [START_EXCLUDE]
                        //    if (!task.isSuccessful()) {
                        //        mStatusTextView.setText(R.string.auth_failed);
                    }//   hideProgressDialog();
                    // [END_EXCLUDE]
                    // }
                });
        // [END sign_in_with_email]
    }

    private void getRole (String UID){

        DocumentReference user = db.collection("users").document(UID);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    String role = (String) doc.getString("role");
                    Boolean admin= doc.getBoolean("admin");




                    getBeranda(admin);
                }

            }

        })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        // ajaib ini


                    }

                });
    }

    private void getBeranda(boolean admin){

        if (admin==true){

            Intent adminLogin = new Intent(LoginActivity.this, BerandaAdmin.class);
            LoginActivity.this.startActivity(adminLogin);

            finish();
        }else {
            Intent userLogin = new Intent(LoginActivity.this, Beranda.class);
            LoginActivity.this.startActivity(userLogin);
            finish();
        }
    }

}