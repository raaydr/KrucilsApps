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
import com.example.krucils.objek.KonfirmasiAdmin;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KonfirmasiAdminFragment extends Fragment {

    private final String TAG = " Keranjang";
    private TextView nama,uploadbukti;
    private Button bayarKeranjang;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String currentuserUID,email,username;
    private int inflater;

    public  int total;

    private static FirestoreRecyclerAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");
    private FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_konfirmasi_admin,container,false);

        //getUID
        currentuserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = FirebaseFirestore.getInstance()
                .collection("Pembelian")
                .whereEqualTo("checkAdmin", false)
                ;

        FirestoreRecyclerOptions<KonfirmasiAdmin> options = new FirestoreRecyclerOptions.Builder<KonfirmasiAdmin>()
                .setQuery(query,KonfirmasiAdmin.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<KonfirmasiAdmin,KeranjangHolder>(options) {


            @NonNull
            @Override
            public KeranjangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_konfirmasi_admin,parent,false);
                return new KeranjangHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull KeranjangHolder holder, int position, @NonNull KonfirmasiAdmin model) {
                String keyPembelian = model.getKeyPembelian();
                String atasnama = model.getAtasnama();
                String bank = model.getBank();
                String uidUser = model.getUidUser();
                String username = model.getUsername();
                String email = model.getEmail();
                String kodeRef = model.getKodeRef();
                String hargaAwal = model.getHargaAwal();
                String hargaAkhir = model.getHargaAkhir();
                String imageURL = model.getImageURL();

                Date uploadBukti = model.getTimestamp();




                holder.setText(keyPembelian,atasnama,bank,uidUser,username,email,kodeRef,hargaAwal,hargaAkhir,imageURL,uploadBukti);


            }
        };


        recyclerView = view.findViewById(R.id.RecyclerViewKonfirmasiAdmin);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
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



    private class KeranjangHolder extends RecyclerView.ViewHolder{

        public KeranjangHolder(@NonNull View itemView) {
            super(itemView);
        }

        void setText(final String keypembelian, final String atasnama, final String bank, final String uidUser, final String username, final String email, final String kodeRef, final String hargaAwal, final String hargaAkhir, final String imageURL,final Date tanggal) {

            nama= itemView.findViewById(R.id.username_tv);
            nama.setText(atasnama);

            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");//formating according to my need
            String date = formatter.format(tanggal);
            uploadbukti=itemView.findViewById(R.id.tanggal_tv);
            uploadbukti.setText(date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), DetailKonfirmasi.class);
                    intent.putExtra("keyPembelian",keypembelian);
                    intent.putExtra("atasnama",atasnama);
                    intent.putExtra("bank",bank);
                    intent.putExtra("uidUser",uidUser);
                    intent.putExtra("username",username);
                    intent.putExtra("email",email);
                    intent.putExtra("kodeRef",kodeRef);
                    intent.putExtra("hargaAwal",hargaAwal);
                    intent.putExtra("hargaAkhir",hargaAkhir);
                    intent.putExtra("imageURL",imageURL);
                    intent.putExtra("date",date);

                    //intent.putExtras(bundle);

                    startActivity(intent);
                }
            });


        }






    }
}
