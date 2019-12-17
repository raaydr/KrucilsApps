package com.example.krucils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StartActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            getRole(currentuser);


        }else{

            Intent userLogin = new Intent(StartActivity.this, Beranda.class);
            StartActivity.this.startActivity(userLogin);
            finish();
        }

    }

    private void getRole (String UID){

        DocumentReference user = db.collection("users").document(UID);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();


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

            Intent adminLogin = new Intent(StartActivity.this, BerandaAdmin.class);
            StartActivity.this.startActivity(adminLogin);

            finish();
        }else {
            Intent userLogin = new Intent(StartActivity.this, Beranda.class);
            StartActivity.this.startActivity(userLogin);
            finish();
        }
    }
}
