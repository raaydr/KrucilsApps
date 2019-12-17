package com.example.krucils;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
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
    private EditText judul, fileurl;
    private Button input,check;
    private ArrayList<Materi> materiList = new ArrayList<Materi>();
    private ArrayList<Materi> ambilList = new ArrayList<Materi>();
    FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input, container, false);

        progressBar =v.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        judul =(EditText)v.findViewById(R.id.tv_paket);
        fileurl = (EditText)v.findViewById((R.id.tv_harga));


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

            String harga = fileurl.getText().toString();


            uploadPaket(nama, harga);
            judul.setText(null);
            fileurl.setText(null);

            break;

            case R.id.check:

                break;

        }

        }

    private void uploadPaket(String nama, String harga) {
        Materi materi = new Materi();
        materi.setJudul(nama);
        materi.setFileurl(harga);

        materiList.add(materi);

        Map<String, Object> doc = new HashMap<>();


        doc.put("test",materiList);
        doc.put("regions", Arrays.asList("test", "hebei"));




        DocumentReference keranjangg = db.collection("paket")
                .document("1");
        keranjangg.update("regions", FieldValue.arrayUnion("greater_virginia"))


                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                });


    }

}