package com.example.portal_berita;

import java.io.Serializable;

public class News implements Serializable {
    private int id;
    private String judul;
    private String kategori;
    private String isi;
    private String tanggal;
    private String gambar; // Menyimpan URL atau path lokal

    public News(int id, String judul, String kategori, String isi, String tanggal, String gambar) {
        this.id = id;
        this.judul = judul;
        this.kategori = kategori;
        this.isi = isi;
        this.tanggal = tanggal;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getKategori() {
        return kategori;
    }

    public String getIsi() {
        return isi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getGambar() {
        return gambar;
    }
}