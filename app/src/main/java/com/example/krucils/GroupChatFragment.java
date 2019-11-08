package com.example.krucils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;



public class GroupChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;

    private static final String TAG = "bastard";

    private static FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;

    private EditText textmessage;

    private FirebaseFirestore db;

    private Button sendButton;
    private String isiteks;

    private boolean lastItemVisible;


    //todo bikin recyclerview list kelas yang dia udah daftar


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groupchat, container, false);
        mAuth = FirebaseAuth.getInstance();

        sendButton = v.findViewById(R.id.sendchat);

        db = FirebaseFirestore.getInstance();


        //todo nanti ubah documentpath disini jadi berdasarkan apa yang dia klik di list groupchat sama cek lagi limit
        Query query = db
                .collection("Messages").document("groupchat1")
                .collection("messages")
                .orderBy("timestamp")
                .limit(50);

        FirestoreRecyclerOptions<GroupChat> options = new FirestoreRecyclerOptions.Builder<GroupChat>()
                .setQuery(query, GroupChat.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<GroupChat, GroupChatHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  GroupChatHolder  holder, int position, @NonNull GroupChat model) {
                //todo parse datenya jadi jam doang mungkin ? tergantung mau gimana mereka
                holder.setText("Nama = "+ model.getName(), "Message = " + model.getMessage(), model.getTimestamp().toDate());
            }

            @NonNull
            @Override
            public  GroupChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listchat, parent, false);
                return new  GroupChatHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                //todo untuk sekarang kalo ada data baru langsung pindah ke data baru recyclerviewnya, harusnya cuma pindah kalo item terakhir lagi keliatan
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        };
        recyclerView = v.findViewById(R.id.recyclerviewchat);

        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());


        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        textmessage = v.findViewById(R.id.inputchatmessage);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isiteks = textmessage.getText().toString();
                if(!isiteks.isEmpty()) {
                    inputMessage();
                }
            }
        });

        textmessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isLastItemVisible();
                return false;
            }
        });

        //automatis ke last object kalo keyboard muncul (kalo last object lagi keliatan)
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int z = Integer.valueOf(adapter.getItemCount() -1);
                Log.d(TAG, "sebelum : " + z );
                Log.d(TAG, "sebelum: " + layoutManager.findLastCompletelyVisibleItemPosition());
                if (lastItemVisible) {
                    Log.d(TAG, "masuk: " + z);
                    Log.d(TAG, "masuk: " + layoutManager.findLastCompletelyVisibleItemPosition());
                    if (bottom < oldBottom) {
                        recyclerView.getScrollState();
                        layoutManager.findLastCompletelyVisibleItemPosition();
                        Log.d(TAG, "selesai: " + z);
                        Log.d(TAG, "selesai: " + layoutManager.findLastCompletelyVisibleItemPosition());
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        }, 100);
                    }
                }
            }
        });


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

    private class GroupChatHolder extends RecyclerView.ViewHolder {
        private View view;

        GroupChatHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setText(String setName, String setMessage, Date setTanggal) {
            TextView nama = view.findViewById(R.id.chatnama);
            TextView message = view.findViewById(R.id.chatmessage);
            TextView tanggal = view.findViewById(R.id.chattanggal);
            nama.setText(setName);
            message.setText(setMessage);
            tanggal.setText(setTanggal.toString());
        }
    }

    public void inputMessage(){

        FirebaseUser user = mAuth.getCurrentUser();
        //todo pas register masukin display namenya, input ke currentUser sama database, terus disini buat namenya ganti user.getDisplayName() dkk

        //todo restrict access write sama read ke database dan restrict user
        GroupChat chat = new GroupChat("test", isiteks,"test","test", Timestamp.now());
        db.collection("Messages")
                .document("groupchat1")
                .collection("messages")
                .add(chat);
        textmessage.getText().clear();
    }

    //cek last objek keliatan atau nggak
    private void isLastItemVisible() {
        if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
            lastItemVisible = true;
        } else {
            lastItemVisible = false;
        }
    }
}
