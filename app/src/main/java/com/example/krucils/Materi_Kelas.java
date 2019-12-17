package com.example.krucils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Materi_Kelas extends AppCompatActivity  {

    private final String TAG = " Mantap";
    private TextView judul,detail,kelasMulai;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DownloadManager downloadManager;
    private Button delete,publish;
    private String uidAkses,uidKelas;
    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi__kelas);
        db = FirebaseFirestore.getInstance();



        if(getIntent().getExtras() != null){
            //Statement Disini Akan Berjalan Jika Menggunakan Bundle

            uidAkses = getIntent().getStringExtra("uidAkses");




        }else {
            //Statement Berikut ini Akan Dijalankan Jika Tidak Menggunakan Bundle
            Toast.makeText(getApplicationContext(), "Tolong kembali", Toast.LENGTH_LONG).show();

        }
        Query query = FirebaseFirestore.getInstance()
                .collection("MateriKelas")
                .whereEqualTo("uidAkses",uidAkses)
                .whereEqualTo("check",true)
                ;

        FirestoreRecyclerOptions<BahanMateri> options = new FirestoreRecyclerOptions.Builder<BahanMateri>()
                .setQuery(query,BahanMateri.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<BahanMateri,BahanMateriHolder>(options) {

            @NonNull
            @Override
            public BahanMateriHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi_kelas,parent,false);
                return new BahanMateriHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull BahanMateriHolder holder, int position, @NonNull BahanMateri model) {
                String id = model.getId();
                String judul = model.getJudul();
                String urlFile = model.getUrlFile();
                String uidAdmin = model.getUidAdmin();
                String uidAkses = model.getUidAkses();
                String uploadBy = model.getUploadBy();

                boolean check = model.isCheck();
                boolean typeFile = model.isTypeFile();
                Date created = model.getCreated();

                holder.setText(judul);
                holder.downloadMateri(judul,urlFile,typeFile);
            }
        };
        recyclerView = findViewById(R.id.RecyclerViewMateriKelas);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(Materi_Kelas.this);
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





    private class BahanMateriHolder extends RecyclerView.ViewHolder {
        public BahanMateriHolder(@NonNull View itemView) {
            super(itemView);
        }

        void setText(final String setJudul){

            judul = itemView.findViewById(R.id.judul_materi);
            judul.setText(setJudul);
        }



        void downloadMateri(final String judul, final String urlFile, final boolean typeFile){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(typeFile==true){
                        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(urlFile);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalFilesDir(Materi_Kelas.this,DIRECTORY_DOWNLOADS,judul +".pdf");
                        Long reference = downloadManager.enqueue(request);
                    } else {
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFile));
                        startActivity(appIntent);

                    }
                }
            });
        }
    }
}
