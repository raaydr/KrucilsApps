package com.example.krucils.objek;

import java.util.Date;

public class Kelas {

    public String id;
    public String judul;
    public String hargaFull;
    public String hargaBiasa;
    public String detail;
    public Date mulaiKelas;
    public boolean check;
    public String imageURL;
    public Date created;

    public Kelas(){

    }

    public Kelas(String id, String judul, String hargaFull, String hargaBiasa, String detail, Date mulaiKelas, boolean check, String imageURL, Date created) {
        this.id = id;
        this.judul = judul;
        this.hargaFull = hargaFull;
        this.hargaBiasa = hargaBiasa;
        this.detail = detail;
        this.mulaiKelas = mulaiKelas;
        this.check = check;
        this.imageURL = imageURL;
        this.created = created;
    }

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

    public String getHargaFull() {
        return hargaFull;
    }

    public void setHargaFull(String hargaFull) {
        this.hargaFull = hargaFull;
    }

    public String getHargaBiasa() {
        return hargaBiasa;
    }

    public void setHargaBiasa(String hargaBiasa) {
        this.hargaBiasa = hargaBiasa;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getMulaiKelas() {
        return mulaiKelas;
    }

    public void setMulaiKelas(Date mulaiKelas) {
        this.mulaiKelas = mulaiKelas;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
