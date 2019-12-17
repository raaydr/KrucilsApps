package com.example.krucils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krucils.objek.BahanMateri;
import com.example.krucils.objek.Beli;
import com.example.krucils.objek.Keranjang;
import com.example.krucils.objek.KonfirmasiAdmin;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DetailKonfirmasi extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = " Mantap";
    private TextView username, atasnama,bank,hargaAwal,kodeRef,tanggalUpload,hargaAkhir,judulKelas,tanggalKelas,hargaKelas,detailKelas;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DownloadManager downloadManager;
    private  List<String> Akses = new ArrayList<>();
    private  List<String> Setuju = new ArrayList<>();
    private Button downloadBukti,setuju,tolak;
    private String keyPembelian,imageURL,userName,tanggalupload,UIDUser;
    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konfirmasi);
        db = FirebaseFirestore.getInstance();


        username = findViewById(R.id.username_tv);
        atasnama = findViewById(R.id.atas_nama);
        bank = findViewById(R.id.bank_tv);
        hargaAwal = findViewById(R.id.harga_awal_tv);
        kodeRef = findViewById(R.id.kodeRef);
        tanggalUpload=findViewById(R.id.tanggal_upload);
        hargaAkhir = findViewById(R.id.harga_total);

        downloadBukti = findViewById(R.id.btn_gambar);
        setuju = findViewById(R.id.btn_setuju);
        tolak = findViewById(R.id.btn_tolak);
        downloadBukti.setOnClickListener(this);
        setuju.setOnClickListener(this);
        tolak.setOnClickListener(this);

        if(getIntent().getExtras() != null){
            //Statement Disini Akan Berjalan Jika Menggunakan Bundle
            keyPembelian = getIntent().getStringExtra("keyPembelian");

            String atasNama = getIntent().getStringExtra("atasnama");
            atasnama.setText(atasNama);

            String Bank = getIntent().getStringExtra("bank");
            bank.setText(Bank);

            String hargaawal = getIntent().getStringExtra("hargaAwal");
            hargaAwal.setText(hargaawal);

            String koderef = getIntent().getStringExtra("kodeRef");
            kodeRef.setText(koderef);

            tanggalupload = getIntent().getStringExtra("date");
            tanggalUpload.setText(tanggalupload);

            String hargatotal = getIntent().getStringExtra("hargaAkhir");
            hargaAkhir.setText(hargatotal);

            imageURL = getIntent().getStringExtra("imageURL");
            userName = getIntent().getStringExtra("username");
            UIDUser = getIntent().getStringExtra("uidUser");





        }else {
            //Statement Berikut ini Akan Dijalankan Jika Tidak Menggunakan Bundle
            Toast.makeText(getApplicationContext(), "Tolong kembali", Toast.LENGTH_LONG).show();

        }
        Query query = FirebaseFirestore.getInstance()
                .collection("Keranjang")
                .whereEqualTo("keyPembelian",keyPembelian)
                .whereEqualTo("check",true)
                ;

        FirestoreRecyclerOptions<Beli> options = new FirestoreRecyclerOptions.Builder<Beli>()
                .setQuery(query,Beli.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Beli,KonfirmasiHolder>(options) {


            @NonNull
            @Override
            public KonfirmasiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_konfirmasi,parent,false);
                return new KonfirmasiHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull KonfirmasiHolder holder, int position, @NonNull Beli model) {
                    String judul = model.getJudul();
                    String detail = model.getDetail();
                    String tanggal = model.getTanggal();
                    String harga = model.getHarga();
                    String uidAkses = model.getUidAkses();
                    String username = model.getUsername();
                    String uidUser = model.getUiduser();

                    String disetujui= "Selamat pembayaran anda berhasi dan anda dapat mengakses kelas"+judul+"pada tanggal"+tanggal;
                    Akses.add(uidAkses);
                    Setuju.add(disetujui);
                    holder.setText(judul,tanggal,harga,detail);
            }
        };
        recyclerView = findViewById(R.id.RecyclerViewDetailKonfirmasi);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(DetailKonfirmasi.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_gambar:
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(imageURL);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(DetailKonfirmasi.this,DIRECTORY_DOWNLOADS,userName+"-"+tanggalupload+".jpg");
                Long reference = downloadManager.enqueue(request);
                break;


            case R.id.btn_tolak:

                alertTolak();

                Intent intent = new Intent(DetailKonfirmasi.this, BerandaAdmin.class);

                startActivity(intent);

                break;
            case  R.id.btn_setuju:
                alertSetuju();


                break;
        }


    }
    private void tolak(){
        DocumentReference akses = db.collection("Pembelian")
                .document(keyPembelian);
        akses.update("checkAdmin",true);
        akses.update("pembayaran",false)


                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String fail = " Mohon maaf pembayaran anda gagal, silahkan mengulangi proses pembelian";
                        String keterangan="Gagal";
                        uploadNotifikasi(UIDUser,fail,keterangan);
                        Toast.makeText(DetailKonfirmasi.this, "Tolak", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void setuju(){
        DocumentReference akseskelas = db.collection("Pembelian")
                .document(keyPembelian);
        akseskelas.update("checkAdmin",true);
        akseskelas.update("pembayaran",true)


                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //bikin notifikasi + akses Kelas
                        inputAkses(UIDUser);
                        Toast.makeText(DetailKonfirmasi.this, "Berhasil ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void alertTolak(){
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(DetailKonfirmasi.this);

        // Set the message show for the Alert time
        builder.setMessage("Anda yakin mau menghapus kelas ini ?");

        // Set Alert Title
        builder.setTitle("Perhatian");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ya",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                tolak();
                                dialog.cancel();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "Tidak",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
    private void alertSetuju(){
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(DetailKonfirmasi.this);

        // Set the message show for the Alert time
        builder.setMessage("Anda yakin mau menghapus kelas ini ?");

        // Set Alert Title
        builder.setTitle("Perhatian");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ya",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                setuju();
                                dialog.cancel();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "Tidak",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
    private void inputAkses (String uiduser){

        for(int i = 0; i < Akses.size(); i++) {
            String id = Akses.get(i);
            int k = i;
            DocumentReference submitkelas = db.collection("AksesKelas")
                    .document(id);
            submitkelas.update("uidUser", FieldValue.arrayUnion(uiduser))


                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            String accept = Setuju.get(k);
                            String keterangan = "Sukses";
                            uploadNotifikasi(uiduser,accept,keterangan);



                        }
                    });
        }



    }

    private void uploadNotifikasi(String uiduser, String persetujuan, String keterangan ){
        String uid = UUID.randomUUID().toString();


        Map<String, Object> doc = new HashMap<>();

        doc.put("id", uid);

        doc.put("uidUser", uiduser);
        doc.put("keterangan", keterangan);
        doc.put("info", persetujuan);

        doc.put("timestamp", FieldValue.serverTimestamp());

        db.collection("Notifikasi")
                .document(uid)
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })


        ;
    }
    private class KonfirmasiHolder extends RecyclerView.ViewHolder {
        public KonfirmasiHolder(@NonNull View itemView) {
            super(itemView);
        }

        void setText(final String setJudul, final  String tanggal, final String harga,final String detail){

            judulKelas = itemView.findViewById(R.id.judul_kelas);
            tanggalKelas= itemView.findViewById(R.id.tanggal_kelas);
            detailKelas = itemView.findViewById(R.id.detail_kelas);
            hargaKelas = itemView.findViewById(R.id.harga_kelas);

            judulKelas.setText(setJudul);
            tanggalKelas.setText(tanggal);
            hargaKelas.setText(harga);
            detailKelas.setText(detail);
        }




    }
}
