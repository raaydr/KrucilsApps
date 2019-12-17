package com.example.krucils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krucils.objek.Kelas;
import com.example.krucils.objek.Notifikasi;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotifikasiFragment extends Fragment {
    private final String TAG = " Notifikasi";
    private TextView judul,detail,kelasMulai;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String currentuserUID;
    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");

    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentuserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        View v = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        Query query = FirebaseFirestore.getInstance()
                .collection("Notifikasi")
                .whereEqualTo("uidUser",currentuserUID)
                ;
        FirestoreRecyclerOptions<Notifikasi> options = new FirestoreRecyclerOptions.Builder<Notifikasi>()
                .setQuery(query,Notifikasi.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Notifikasi, NotifikasiHolder>(options) {


            @NonNull
            @Override
            public NotifikasiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi,parent,false);
                return new NotifikasiHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull NotifikasiHolder holder, int position, @NonNull Notifikasi model) {
                        String id = model.getId();
                        String uidUser = model.getUidUser();
                        String info = model.getInfo();
                        String keterangan = model.getKeterangan();

                        Date created = model.getTimestamp();

                        holder.setText(keterangan,created,info);
            }
        };

        recyclerView = v.findViewById(R.id.RecyclerViewNotifikasi);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        return v;
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
    private class NotifikasiHolder extends RecyclerView.ViewHolder {
        public NotifikasiHolder(@NonNull View itemView) {
            super(itemView);


        }

        void setText (final String setketerangan, final Date setTanggal, final String setinfo ){


            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");//formating according to my need
            String date = formatter.format(setTanggal);
            TextView keterangan = itemView.findViewById(R.id.keterangan_tv);
            TextView info = itemView.findViewById(R.id.info_tv);
            TextView tanggal = itemView.findViewById(R.id.tanggal_tv);

            keterangan.setText(setketerangan);
            info.setText(setinfo);
            tanggal.setText(date);



        }





    }
}