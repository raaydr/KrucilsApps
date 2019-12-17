package com.example.krucils.objek;

import java.util.Date;

public class AksesKelas {

    public String id;
    public String imageURL;
    public String judul;
    public String uidkelas;
    public String createdBy;

    public boolean check;
    public boolean publish;
    public boolean tester;

    public Date mulaiKelas;
    public Date timestamp;

    public AksesKelas(){}

    public AksesKelas(String id, String imageURL, String judul, String uidkelas, String createdBy, boolean check, boolean publish, boolean tester, Date mulaiKelas, Date timestamp) {
        this.id = id;
        this.imageURL = imageURL;
        this.judul = judul;
        this.uidkelas = uidkelas;
        this.createdBy = createdBy;
        this.check = check;
        this.publish = publish;
        this.tester = tester;
        this.mulaiKelas = mulaiKelas;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getUidkelas() {
        return uidkelas;
    }

    public void setUidkelas(String uidkelas) {
        this.uidkelas = uidkelas;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public boolean isTester() {
        return tester;
    }

    public void setTester(boolean tester) {
        this.tester = tester;
    }

    public Date getMulaiKelas() {
        return mulaiKelas;
    }

    public void setMulaiKelas(Date mulaiKelas) {
        this.mulaiKelas = mulaiKelas;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
