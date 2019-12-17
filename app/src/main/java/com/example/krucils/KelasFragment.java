package com.example.krucils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krucils.objek.AksesKelas;
import com.example.krucils.objek.Kelas;
import com.example.krucils.objek.Materi;
import com.example.krucils.objek.UidUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class KelasFragment extends Fragment {
    private final String TAG = " Kelas_Fragment";
    private TextView judul,detail,kelasMulai;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String UIDuser,email,username,UIDkelas,hargaPick,judulPick,imageURL,detailPick,mulaiKelas, uidAkses;
    private FirebaseAuth mAuth;
    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");

    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String currentuser = user.getUid();

        View v = inflater.inflate(R.layout.fragment_kelas, container, false);
        Query query = FirebaseFirestore.getInstance()
                .collection("AksesKelas")
                .whereArrayContains("uidUser",currentuser)

                .whereEqualTo("check",true)
                .whereEqualTo("publish",true)
                ;
        FirestoreRecyclerOptions<AksesKelas> options = new FirestoreRecyclerOptions.Builder<AksesKelas>()
                .setQuery(query,AksesKelas.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<AksesKelas, KelasHolder>(options) {

            @NonNull
            @Override
            public KelasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_akses_kelas,parent,false);
                return new KelasHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull KelasHolder holder, int position, @NonNull AksesKelas model) {
                String id = model.getId();
                String judul = model.getJudul();
                String imageURL= model.getImageURL();
                String uidkelas = model.getUidkelas();
                String createdBy= model.getCreatedBy();

                Date mulaiKelas=model.getMulaiKelas();





                holder.setText(judul,mulaiKelas,imageURL);
                holder.getData(id);

            }
        };

        recyclerView = v.findViewById(R.id.RecyclerViewKelas);
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
    private class KelasHolder extends RecyclerView.ViewHolder {
        public KelasHolder(@NonNull View itemView) {
            super(itemView);


        }

        void setText (final String setJudul, final Date setTanggal, final String setImage ){


            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");//formating according to my need
            String date = formatter.format(setTanggal);
            TextView judul = itemView.findViewById(R.id.judul_tv);

            TextView tanggal = itemView.findViewById(R.id.mulaiKelas_tv);
            ImageView imageView = itemView.findViewById(R.id.thumbnail);
            judul.setText(setJudul);

            tanggal.setText(date);
            Picasso.get().load(setImage).into(imageView);


        }

        void getData(String id){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), Materi_Kelas.class);
                    intent.putExtra("uidAkses",id);

                    //intent.putExtras(bundle);

                    startActivity(intent);
                }
            });



        }



    }
}