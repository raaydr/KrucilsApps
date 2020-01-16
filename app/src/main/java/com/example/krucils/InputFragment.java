package com.example.krucils;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.krucils.objek.Materi;
import com.example.krucils.objek.Test;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class InputFragment extends Fragment implements View.OnClickListener{
    private ProgressBar progressBar;
    private EditText judul, hargatest;
    private Button input,check;
    private String id;
    private int start;
    private ArrayList<Materi> materiList = new ArrayList<Materi>();
    private ArrayList<Materi> ambilList = new ArrayList<Materi>();
    private static final String TAG = "check";
    FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input, container, false);

        progressBar =v.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        judul =(EditText)v.findViewById(R.id.tv_paket);
        hargatest= (EditText)v.findViewById((R.id.tv_harga));


        db = FirebaseFirestore.getInstance();

        input = v.findViewById(R.id.input);
        input.setOnClickListener(this);
        check=v.findViewById(R.id.check);
        check.setOnClickListener(this);


        return v;
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.input:
            //String paket = Paket.getText().toString();
            String nama = judul.getText().toString();

            String hargaString = hargatest.getText().toString();
            int harga = Integer.parseInt(hargaString);

            uploadPaket(nama, harga);
            judul.setText(null);
            hargatest.setText(null);

            break;

            case R.id.check:
                //Intent intent = new Intent(getContext(), Test_Activity.class);
                //startActivity(intent);
                //String hargate = hargatest.getText().toString();
                //int start = Integer.parseInt(hargate);

                //start misalnya random
                start = 2;
                getUID(start);
                break;

        }

        }
    private void check (){
        DocumentReference user = db.collection("paket").document("2");

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    //Test test = doc.toObject(Test.class);
                    String s = doc.getString("nama");
                    long b = doc.getLong("harga");
                    //int a = Integer.parseInt(doc.getString("harga"));
                    String id = doc.getId();


                    Toast.makeText(getActivity(), s+"dan"+b , Toast.LENGTH_SHORT).show();






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
    private void uploadPaket(String nama, int harga) {




        Map<String, Object> doc = new HashMap<>();

        doc.put("nama", nama);
        doc.put("harga", harga);


        db.collection("paket")
                .document(id)
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getActivity(), "upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "gagal", Toast.LENGTH_SHORT).show();
                    }
                })


        ;


    }


    private void getUID (int mulai) {

            String UID = Integer.toString(mulai);
            DocumentReference user = db.collection("paket").document(UID);

            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override

                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                       DocumentSnapshot doc = task.getResult();
                       if(doc.exists()){
                           //ini buat random lagi
                           start = start + 1;
                           Toast.makeText(getActivity(), "udah ada id " + UID + ", UID baru anda = " + start, Toast.LENGTH_SHORT).show();
                           //hasil random dimasukin ke kelas ini lagi buat di loop
                           getUID(start);
                       } else {
                           id = UID;
                           Toast.makeText(getActivity(), "unik id " + id, Toast.LENGTH_SHORT).show();
                       }
                    }

                }

            });

    }
}