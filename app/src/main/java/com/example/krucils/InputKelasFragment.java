package com.example.krucils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class InputKelasFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ProgressBar progressBar;
    private EditText judulKelas, detailKelas, hargaKelas,hargaKelas2;
    private TextView mulaiKelas;
    private Button upload, uImage, datePicker;
    static  final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    private ImageView imageView;
    private Uri filepath, imgUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageTask uploadTask;
    private String UIDuser,email,username;
    private boolean CheckImage = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://test-f3c56.appspot.com");

    FirebaseFirestore db;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_kelas, container, false);

        progressBar = v.findViewById(R.id.progressKelas);
        progressBar.setVisibility(View.GONE);

        judulKelas = v.findViewById(R.id.kelas_judul);
        detailKelas = v.findViewById(R.id.kelas_detail);
        hargaKelas = v.findViewById(R.id.kelas_harga);
        hargaKelas2 = v.findViewById(R.id.kelas_harga2);
        mulaiKelas = v.findViewById(R.id.kelas_mulai);
        upload = v.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        uImage = v.findViewById(R.id.uImage);
        uImage.setOnClickListener(this);
        imageView = v.findViewById(R.id.imgView);




        datePicker=v.findViewById(R.id.datePick);
        datePicker.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ReadUser(currentuser);


        return v;
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


                    }

                });

    }

    @Override
    public void onClick(View view) {



        if (view.getId()==R.id.upload) {

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());

            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final String judul = judulKelas.getText().toString();
            final String detail = detailKelas.getText().toString();
            final String harga = hargaKelas.getText().toString();
            final String harga2 = hargaKelas2.getText().toString();
            final String tanggal = mulaiKelas.getText().toString();

            checkDataInput();
            boolean checkTanggal= true;
            if (getDateFromString(tanggal)==null){
                checkTanggal=false;

            }

            boolean penuh_bastard = false;

            if(!judul.isEmpty() && !detail.isEmpty() && !harga.isEmpty()&& !harga2.isEmpty() && checkTanggal==true){
                penuh_bastard = true;
            }



            if (penuh_bastard==true && imgUri !=null) {



                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "sedang diproses", Toast.LENGTH_LONG).show();


                } else {

                    final StorageReference childRef = storageRef.child("Kelas/" + System.currentTimeMillis() + "." + getExtension(imgUri));
                    uploadTask = childRef.putFile(imgUri)


                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            final Uri downloadUrl = uri;
                                            String image = downloadUrl.toString();
                                            uploadKelas(judul, detail, harga,harga2, tanggal, image,UIDuser,username);
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "upload", Toast.LENGTH_LONG).show();
                                            judulKelas.setText(null);
                                            detailKelas.setText(null);
                                            hargaKelas.setText(null);
                                            hargaKelas2.setText(null);
                                            mulaiKelas.setText(null);
                                            imageView.setImageURI(null);
                                            imgUri= null;


                                        }
                                    });


                                        /*
                                        uploadKelas(judul, detail, harga, tanggal, image);
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "upload", Toast.LENGTH_LONG).show();

                                         */
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    progressDialog.dismiss();
                                    // ...
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });

                }


            }
            else {
                progressDialog.dismiss();
               // Toast.makeText(getActivity(), "Tolong dilengkapi kembali", Toast.LENGTH_LONG).show();
            }



        }


        if (view.getId()==R.id.uImage){

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_PICK);
            //startActivityForResult(intent.createChooser(intent,"Pilih gambar"), PICK_IMAGE_REQUEST);
            startActivityForResult(intent,PICK_IMAGE_REQUEST);

        }

        if ((view.getId()==R.id.datePick)){
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    InputKelasFragment.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );

            dpd.show(getFragmentManager(),"Datepickerdialog");
        }

    }




    private String getExtension (Uri uri){

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()
                !=null)
        {
            imgUri =data.getData();
            imageView.setImageURI(imgUri);
            CheckImage = true;
        }
    }


    private boolean isEmpty(EditText text){
        CharSequence s=text.getText().toString();

        return TextUtils.isEmpty(s);
    }


    private void checkDataInput(){

        int count = 5;
        if (isEmpty(judulKelas)){
            Toast.makeText(getActivity(), "Judul tolong diisi", Toast.LENGTH_LONG).show();
            count = count -1;
            judulKelas.setError("Isi Judul kelas");
        }

        if(isEmpty(hargaKelas)){
            Toast.makeText(getActivity(), "Harga tolong diisi", Toast.LENGTH_LONG).show();
            hargaKelas.setError("Isi Harga kelas");
            count = count -1;
        }

        if(isEmpty(hargaKelas2)){
            Toast.makeText(getActivity(), "Harga tolong diisi", Toast.LENGTH_LONG).show();
            hargaKelas2.setError("Isi Harga kelas");
            count = count -1;
        }

        if(isEmpty(detailKelas)){
            Toast.makeText(getActivity(), "Detail tolong diisi", Toast.LENGTH_LONG).show();
            detailKelas.setError("Isi detail kelas");
            count = count -1;
        }
/*
        if(isEmpty(mulaiKelas)){
            Toast.makeText(getActivity(), "tanggal tolong diisi", Toast.LENGTH_LONG).show();
            mulaiKelas.setError("Isi tanggal");
        }



 */
        if(getDateFromString(mulaiKelas.getText().toString())==null){
            Toast.makeText(getActivity(), "tanggal diisi sesuai format", Toast.LENGTH_LONG).show();
            mulaiKelas.setError("Isi Tanggal sesuai format yyyy/mm/dd");
            count = count -1;
        }

        if (imgUri==null){

            Toast.makeText(getActivity(), "Silahkan Upload foto", Toast.LENGTH_LONG).show();
            count = count -1;

        }
        if(count==0) {
            Toast.makeText(getActivity(), "Tolong dilengkapi kembali", Toast.LENGTH_LONG).show();
            count = 5;
        }
    }
    private void uploadAksesKelas(String uidkelas, String tanggal, String imageURL, String judul,String id,String uidUser, String createdBy){



        Map<String, Object> doc = new HashMap<>();

        doc.put("id", id);
        doc.put("uidkelas", uidkelas);
        doc.put("uidUser", Arrays.asList(uidUser));
        doc.put("judul", judul);
        doc.put("mulaiKelas", getDateFromString(tanggal));
        doc.put("createdBy", createdBy);
        doc.put("imageURL", imageURL);
        doc.put("publish",false);
        doc.put("tester",false);
        doc.put("timestamp", FieldValue.serverTimestamp());

        db.collection("AksesKelas")
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

                    }
                })


        ;
    }
    private void uploadKelas(String judul, String detail, String harga,String harga2, String tanggal, String imageURL, String uidAdmin, String username) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setTitle("Uploading...");
        //progressDialog.show();

        String id = UUID.randomUUID().toString();

        String uidAkses = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();

        doc.put("id", id);
        doc.put("judul", judul);
        doc.put("uidAkses", uidAkses);
        doc.put("uidAdmin", uidAdmin);
        doc.put("createdBy", username);
        doc.put("hargaFull", harga);
        doc.put("hargaBiasa", harga2);
        doc.put("detail", detail);
        doc.put("mulaiKelas", getDateFromString(tanggal));
        doc.put("imageURL", imageURL);
        doc.put("check",false);
        doc.put("publish",false);
        doc.put("timestamp", FieldValue.serverTimestamp());



        db.collection("Kelas")
                .document(id)
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       uploadAksesKelas(id,tanggal,imageURL,judul,uidAkses,UIDuser,username);

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

    public Date getDateFromString(String datetoSave) {
        try {
            Date date = format.parse(datetoSave);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = (monthOfYear+1)+"/"+dayOfMonth+"/"+year;
        mulaiKelas.setText(date);
    }
}
