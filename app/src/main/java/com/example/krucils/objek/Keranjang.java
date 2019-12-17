package com.example.krucils.objek;

import java.io.Serializable;

public class Keranjang implements Serializable {

    String uidKeranjang;
    boolean faq;

    public String getUidKeranjang() {
        return uidKeranjang;
    }

    public void setUidKeranjang(String uidKeranjang) {
        this.uidKeranjang = uidKeranjang;
    }

    public boolean isFaq() {
        return faq;
    }

    public void setFaq(boolean faq) {
        this.faq = faq;
    }

    public Keranjang(String uidKeranjang, boolean faq) {
        this.uidKeranjang = uidKeranjang;
        this.faq = faq;
    }

    public Keranjang(){

    }
}
