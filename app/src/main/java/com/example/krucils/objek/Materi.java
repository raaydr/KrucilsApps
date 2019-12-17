package com.example.krucils.objek;

public class Materi {

    private String judul;
    private String fileurl;
    public Materi(){

    }

    public Materi(String judul, String fileurl) {
        this.judul = judul;
        this.fileurl = fileurl;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
