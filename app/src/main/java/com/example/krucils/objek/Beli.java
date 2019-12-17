package com.example.krucils.objek;

import java.util.Date;

public class Beli {

    private String id;
    private String uidkelas;
    private String judul;
    private String detail;
    private String harga;
    private String tanggal;
    private String imageURL;
    private boolean grupchat;

    private String uiduser;
    private String email;
    private String username;

    private String uidAkses;
    private String keyPembelian;
    private boolean check;
    private Date created;


    public Beli(){}

    public Beli(String id, String uidkelas, String judul, String detail, String harga, String tanggal, String imageURL, boolean grupchat, String uiduser, String email, String username, String uidAkses, String keyPembelian, boolean check, Date created) {
        this.id = id;
        this.uidkelas = uidkelas;
        this.judul = judul;
        this.detail = detail;
        this.harga = harga;
        this.tanggal = tanggal;
        this.imageURL = imageURL;
        this.grupchat = grupchat;
        this.uiduser = uiduser;
        this.email = email;
        this.username = username;
        this.uidAkses = uidAkses;
        this.keyPembelian = keyPembelian;
        this.check = check;
        this.created = created;
    }

    public String getUidAkses() {
        return uidAkses;
    }

    public void setUidAkses(String uidAkses) {
        this.uidAkses = uidAkses;
    }

    public String getKeyPembelian() {
        return keyPembelian;
    }

    public void setKeyPembelian(String keyPembelian) {
        this.keyPembelian = keyPembelian;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUidkelas() {
        return uidkelas;
    }

    public void setUidkelas(String uidkelas) {
        this.uidkelas = uidkelas;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isGrupchat() {
        return grupchat;
    }

    public void setGrupchat(boolean grupchat) {
        this.grupchat = grupchat;
    }

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
