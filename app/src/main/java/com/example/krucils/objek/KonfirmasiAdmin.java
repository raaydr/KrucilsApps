package com.example.krucils.objek;

import java.util.Date;

public class KonfirmasiAdmin {

    public String keyPembelian;
    public String atasnama;
    public String bank;
    public String uidUser;
    public String username;
    public String email;
    public String kodeRef;
    public String hargaAwal;
    public String hargaAkhir;
    public String imageURL;

    public Date timestamp;

    public  KonfirmasiAdmin(){}

    public KonfirmasiAdmin(String keyPembelian, String atasnama, String bank, String uidUser, String username, String email, String kodeRef, String hargaAwal, String hargaAkhir, String imageURL, Date timestamp) {
        this.keyPembelian = keyPembelian;
        this.atasnama = atasnama;
        this.bank = bank;
        this.uidUser = uidUser;
        this.username = username;
        this.email = email;
        this.kodeRef = kodeRef;
        this.hargaAwal = hargaAwal;
        this.hargaAkhir = hargaAkhir;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getKeyPembelian() {
        return keyPembelian;
    }

    public void setKeyPembelian(String keyPembelian) {
        this.keyPembelian = keyPembelian;
    }

    public String getAtasnama() {
        return atasnama;
    }

    public void setAtasnama(String atasnama) {
        this.atasnama = atasnama;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKodeRef() {
        return kodeRef;
    }

    public void setKodeRef(String kodeRef) {
        this.kodeRef = kodeRef;
    }

    public String getHargaAwal() {
        return hargaAwal;
    }

    public void setHargaAwal(String hargaAwal) {
        this.hargaAwal = hargaAwal;
    }

    public String getHargaAkhir() {
        return hargaAkhir;
    }

    public void setHargaAkhir(String hargaAkhir) {
        this.hargaAkhir = hargaAkhir;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
