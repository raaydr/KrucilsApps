package com.example.krucils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class GroupChat {
        private String name;
        private String message;
        private String uid;
        private Date timestamp;
        private String email;

        public GroupChat() { } // Needed for Firebase

        public GroupChat(String name, String message, String uid, String email,Date timestamp) {
            this.name = name;
            this.message = message;
            this.uid = uid;
            this.email = email;
            this.timestamp = timestamp;
        }

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }

        public String getMessage() { return message; }

        public void setMessage(String message) { this.message = message; }

        public String getUid() { return uid; }

        public void setUid(String uid) { this.uid = uid; }

        @ServerTimestamp
        public Date getTimestamp() { return timestamp; }

        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

        public String getEmail(){ return email; }

        public void setEmail(String email) { this.email = email;}
}
