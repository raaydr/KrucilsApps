package com.example.krucils.objek;

import java.util.Date;

public class Notifikasi {

    public String id ;
    public String uidUser;
    public String info;
    public String keterangan;

    public Date timestamp;

    public  Notifikasi(){}

    public Notifikasi(String id, String uidUser, String info, String keterangan, Date timestamp) {
        this.id = id;
        this.uidUser = uidUser;
        this.info = info;
        this.keterangan = keterangan;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
