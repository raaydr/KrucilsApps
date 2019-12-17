package com.example.krucils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView name;
    private Button btn_ya,btn_tidak;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);
        mAuth = FirebaseAuth.getInstance();

        btn_ya = findViewById(R.id.btn_ya);
        btn_ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent logout = new Intent(LogoutActivity.this, Beranda.class);
                startActivity(logout);
                finish();
            }
        });

        btn_tidak = findViewById(R.id.btn_tidak);
        btn_tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
