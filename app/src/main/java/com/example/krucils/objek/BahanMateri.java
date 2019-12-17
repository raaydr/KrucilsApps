package com.example.krucils.objek;

import java.util.Date;

public class BahanMateri {

    public String id;
    public String judul;
    public String urlFile;
    public String uidAdmin;
    public String uidAkses;
    public String uploadBy;

    public boolean check;
    public boolean typeFile;
    public Date created;

    public BahanMateri(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getUidAdmin() {
        return uidAdmin;
    }

    public void setUidAdmin(String uidAdmin) {
        this.uidAdmin = uidAdmin;
    }

    public String getUidAkses() {
        return uidAkses;
    }

    public void setUidAkses(String uidAkses) {
        this.uidAkses = uidAkses;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isTypeFile() {
        return typeFile;
    }

    public void setTypeFile(boolean typeFile) {
        this.typeFile = typeFile;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
