package com.example.krucils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetailKelas extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    FirebaseFirestore db;
    private Spinner spinner;
    private FirebaseAuth mAuth;
    private ImageView image;
    private TextView judul,detail,kelasMulai,harga,detail_harga;
    private Button bayar,login;
    boolean checkGrupchat;
    private static final String[] paths = {"Harga Full", "Harga Biasa"};
    private String hargaFull,hargaBiasa,UIDuser,email,username,UIDkelas,hargaPick,judulPick,imageURL,detailPick,mulaiKelas, uidAkses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kelas);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        image = findViewById(R.id.image);
        judul = findViewById(R.id.judul_kelas);
        detail = findViewById(R.id.detail_kelas);
        kelasMulai = findViewById(R.id.kelas_berlaku);
        harga = findViewById(R.id.harga_kelas);
        detail_harga=findViewById(R.id.detail_harga);

        bayar = findViewById(R.id.btn_bayar);
        bayar.setOnClickListener(this);
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(this);


        if(getIntent().getExtras() != null){
            //Statement Disini Akan Berjalan Jika Menggunakan Bundle
            UIDkelas = getIntent().getStringExtra("id");
            uidAkses = getIntent().getStringExtra("uidAkses");
            String title = getIntent().getStringExtra("judul");
             imageURL = getIntent().getStringExtra("imageURL");
            String detaill = getIntent().getStringExtra("detail");
            String date =  getIntent().getStringExtra("mulaiKelas");
            Picasso.get().load(imageURL).into(image);
            judul.setText(title);
            detail.setText(detaill);

           // SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");//formating according to my need
           // String date = formatter.format(getIntent().getStringExtra("mulaiKelas"));
            kelasMulai.setText(date);
            hargaFull = getIntent().getStringExtra("hargaFull");
             hargaBiasa = getIntent().getStringExtra("hargaBiasa");

            boolean check = getIntent().getExtras().getBoolean("check");


        }else {
            //Statement Berikut ini Akan Dijalankan Jika Tidak Menggunakan Bundle
            Toast.makeText(getApplicationContext(), "Tolong kembali", Toast.LENGTH_LONG).show();

        }


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(DetailKelas.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (user != null) {
            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            ReadUser(currentuser);

            login.setVisibility(View.GONE);
            bayar.setVisibility(View.VISIBLE);
        }else{
            bayar.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    private void ReadUser(String s) {

        DocumentReference user = db.collection("users").document(s);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    UIDuser = (String) doc.getString("UID");
                    email = (String) doc.getString("email");
                    username =(String)doc.getString("username");

                }

            }

        })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        // ajaib ini
                        Toast.makeText(getApplicationContext(), "tidak ada user", Toast.LENGTH_LONG).show();

                    }

                });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_bayar:
                judulPick = judul.getText().toString();
                hargaPick = harga.getText().toString();
                detailPick= detail_harga.getText().toString();
                mulaiKelas = kelasMulai.getText().toString();
                inputKeranjang(UIDuser,username,email,UIDkelas,judulPick,imageURL,hargaPick,detailPick,mulaiKelas,checkGrupchat,uidAkses);
                break;
            case R.id.btn_login:
                // Intent ke login
                Intent loginIntent = new Intent(DetailKelas.this, LoginActivity.class);
                DetailKelas.this.startActivity(loginIntent);
                break;


        }

    }

    private void inputKeranjang(String UIDuser,
                                String username,
                                String email,
                                String UIDkelas,
                                String judul,
                                String imageURL,
                                String harga,
                                String detail,
                                String mulaiKelas,
                                boolean grupchat,
                                String uidAkses
                                ) {

        String id = UUID.randomUUID().toString();
        String keyPembelian = "kosong";
        Map<String,Object> doc = new HashMap<>();

        doc.put("id", id);
        doc.put("uiduser", UIDuser);
        doc.put("username", username);
        doc.put("email", email);

        doc.put("uidkelas", UIDkelas);
        doc.put("judul", judul);
        doc.put("harga", harga);
        doc.put("imageURL", imageURL);
        doc.put("detail", detail);
        doc.put("tanggal", mulaiKelas);
        doc.put("grupchat", grupchat);

        doc.put("uidAkses",uidAkses);
        doc.put("keyPembelian",keyPembelian);
        doc.put("check",true);
        doc.put("created", FieldValue.serverTimestamp());

        db.collection("NewKeranjang")
                .document(id)
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Berhasil tambahkan ke keranjang", Toast.LENGTH_LONG).show();
                        Intent beranda = new Intent(DetailKelas.this, Beranda.class);
                        DetailKelas.this.startActivity(beranda);

                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_SHORT).show();
                    }
                })


        ;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                checkGrupchat = true;
                harga.setText(hargaFull);
                detail_harga.setText("Materi dan Akses Grup chat");
                break;
            case 1:
                checkGrupchat = false;
                harga.setText(hargaBiasa);
                detail_harga.setText("Materi aja");
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "Harga tolong diisi", Toast.LENGTH_LONG).show();

    }
}
