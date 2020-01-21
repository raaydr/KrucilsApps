package com.example.krucils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krucils.objek.Beli;
import com.example.krucils.objek.Kelas;
import com.example.krucils.objek.Keranjang;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KeranjangFragment extends Fragment implements View.OnClickListener {

    private final String TAG = " Keranjang";
    private TextView judul,detail,kelasMulai,harga,totalHarga;
    private Button bayarKeranjang;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String currentuserUID,email,username;
    private int inflater;

    public  int total;
    //private List keranjangHarga = new ArrayList<Integer>();
    private ArrayList<Integer> keranjangHarga = new ArrayList<Integer>();
    private ArrayList<Keranjang> keranjangList = new ArrayList<Keranjang>();

    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");
    private FirebaseAuth mAuth;
    FirebaseFirestore db= FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keranjang,container,false);
        totalHarga = view.findViewById(R.id.tv_total);
        currentuserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        arrayReload();
        bayarKeranjang=view.findViewById(R.id.btn_bayar);
        bayarKeranjang.setOnClickListener(this);
        //getUID


        Query query = FirebaseFirestore.getInstance()
                .collection("NewKeranjang")
                .whereEqualTo("uiduser",currentuserUID)
                .whereEqualTo("check", true)
                .whereEqualTo("keyPembelian", "kosong")
                ;

        FirestoreRecyclerOptions<Beli> options = new FirestoreRecyclerOptions.Builder<Beli>()
                .setQuery(query,Beli.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Beli,KeranjangHolder>(options) {

            @NonNull
            @Override
            public KeranjangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keranjang,parent,false);
                return new KeranjangHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull KeranjangHolder holder, int position, @NonNull Beli model) {

                String id = model.getId();
                String uidkelas = model.getUidkelas();
                String judul=model.getJudul();
                String detail=model.getDetail();
                String harga=model.getHarga();
                String image = model.getImageURL();
                String tanggal = model.getTanggal();
                boolean grupchat=model.isGrupchat();

                int hargaKelas = Integer.parseInt(harga);
                String uiduser=model.getUiduser();
                email=model.getEmail();
                username=model.getUsername();

                boolean check=model.isCheck();
                Date created=model.getCreated();






                holder.delete(position,id);
                holder.setText(judul,tanggal,image,harga,detail);
            }




        };


        recyclerView = view.findViewById(R.id.RecyclerViewKeranjang);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void arrayReload(){

        CollectionReference collectionReferences = db.collection("NewKeranjang");
        Query query = collectionReferences
                .whereEqualTo("uiduser",currentuserUID)
                .whereEqualTo("check", true)
                .whereEqualTo("keyPembelian", "kosong");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String hargas = documentSnapshot.getString("harga");
                        int harga = Integer.parseInt(hargas);
                        keranjangHarga.add(harga);

                        String uidKeranjang = documentSnapshot.getString("id");
                        boolean faq = documentSnapshot.getBoolean("grupchat");
                        Keranjang keranjang = new Keranjang(uidKeranjang,faq);
                        keranjang.setUidKeranjang(uidKeranjang);
                        keranjang.setFaq(faq);

                        keranjangList.add(keranjang);
                        calculateTotal();
                    }
                }
            }
        });

    }
    public  void calculateTotal(){


        int i=0;
        total=0;
        int k = keranjangHarga.size();
        while(i<k){
            total= total + keranjangHarga.get(i);
            i++;
        }
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        totalHarga.setText(kursIndonesia.format(total));
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
        Bundle bundle = new Bundle();
        bundle.putString("uidUser",currentuserUID);
        bundle.putString("username",username);
        bundle.putString("usermail",email);
        bundle.putInt("totalHarga",total);
        bundle.putSerializable("keranjangList",keranjangList);

        Intent intent = new Intent(getContext(), DetailKeranjang.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private class KeranjangHolder extends RecyclerView.ViewHolder{

        public KeranjangHolder(@NonNull View itemView) {
            super(itemView);
        }

        void setText(final String setJudul, final String setTanggal, final String setImage, final  String setHarga, final  String setDetail) {
            imageView = itemView.findViewById(R.id.thumbnail);
            Picasso.get().load(setImage).into(imageView);

            judul= itemView.findViewById(R.id.judul_tv);
            judul.setText(setJudul);

            detail=itemView.findViewById(R.id.detail_tv);
            detail.setText(setDetail);

            kelasMulai= itemView.findViewById(R.id.mulaiKelas_tv);
            kelasMulai.setText(setTanggal);

            harga=itemView.findViewById(R.id.harga_tv);
            harga.setText(setHarga);
        }

        void delete(final int position, final String id){

            db = FirebaseFirestore.getInstance();
            ImageButton delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    DocumentReference keranjang = db.collection("NewKeranjang")
                            .document(id);
                    keranjang.update("check",false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
                                    if (keranjangList.size()>1) {
                                        keranjangHarga.clear();
                                        keranjangList.clear();
                                        arrayReload();
                                    } else {

                                        keranjangHarga.clear();
                                        keranjangList.clear();
                                        totalHarga.setText("Rp 0");
                                    }
                                }
                            });




                }




            });


        }




    }
}
