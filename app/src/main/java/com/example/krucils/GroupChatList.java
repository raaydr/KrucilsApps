package com.example.krucils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class GroupChatList {

    private String judul;

    public GroupChatList() { } // Needed for Firebase

    public GroupChatList(String judul) {
        this.judul = judul;
    }

    public String getJudul() { return judul; }

    public void setJudul(String judul) { this.judul = judul; }

}
