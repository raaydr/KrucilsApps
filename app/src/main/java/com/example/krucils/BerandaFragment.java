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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class BerandaFragment extends Fragment {
    private final String TAG = " Mantap";
    private TextView judul,detail,kelasMulai;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");

    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_beranda, container, false);
        Query query = FirebaseFirestore.getInstance()
                .collection("Kelas")
                .whereEqualTo("publish",true)
                ;
        FirestoreRecyclerOptions<Kelas> options = new FirestoreRecyclerOptions.Builder<Kelas>()
                .setQuery(query,Kelas.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Kelas, KelasHolder>(options) {
            @NonNull
            @Override
            public KelasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kelas,parent,false);
                return new KelasHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull KelasHolder holder, int position, @NonNull Kelas model) {
                String id = model.getId();
                String judul = model.getJudul();
                String hargaFull = model.getHargaFull();
                String hargaBiasa = model.getHargaBiasa();
                String detail = model.getDetail();
                Date mulaiKelas = model.getMulaiKelas();
                Boolean check = model.isCheck();
                Boolean publish = model.isPublish();
                String imageURL = model.getImageURL();
                String uidAkses=model.getUidAkses();



                holder.setText(judul,mulaiKelas,imageURL);

                holder.getData(id,judul,hargaFull,hargaBiasa,detail,mulaiKelas,publish,imageURL,uidAkses);
            }
        };

        recyclerView = v.findViewById(R.id.RecyclerViewBeranda);
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

        void getData(String id, String judul, String hargaFull, String hargaBiasa, String detail, Date mulaiKelas, boolean check, String imageURL,String uidAkses){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("judul", judul);
                    bundle.putString("harga", harga);
                    bundle.putString("detail", detail);
                    bundle.put("mulaiKelas", mulaiKelas);
                    bundle.putString("judul", judul);


                     */

                    SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");//formating according to my need
                    String date = formatter.format(mulaiKelas);

                    Intent intent = new Intent(getContext(), DetailKelas.class);
                    intent.putExtra("id",id);
                    intent.putExtra("uidAkses",uidAkses);
                    intent.putExtra("judul",judul);
                    intent.putExtra("hargaFull",hargaFull);
                    intent.putExtra("hargaBiasa",hargaBiasa);
                    intent.putExtra("detail",detail);
                    intent.putExtra("mulaiKelas",date);
                    intent.putExtra("check",check);
                    intent.putExtra("imageURL",imageURL);
                    //intent.putExtras(bundle);

                    startActivity(intent);
                }
            });



        }



    }
}