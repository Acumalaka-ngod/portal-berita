package com.portalberita.atta;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txtAppName = findViewById(R.id.txtAppName);
        TextView txtAppDesc = findViewById(R.id.txtAppDesc);
        TextView txtAppVersion = findViewById(R.id.txtAppVersion);

        txtAppName.setText("Portal Berita");
        txtAppDesc.setText("Aplikasi Portal Berita adalah aplikasi Android untuk membaca dan mengelola berita. " +
                "Fitur utama meliputi:\n\n" +
                "- Membaca daftar berita terkini\n" +
                "- Mencari berita berdasarkan judul\n" +
                "- Memfilter berita berdasarkan kategori\n" +
                "- Melihat detail berita lengkap\n" +
                "- Berbagi berita ke aplikasi lain\n" +
                "- Kelola berita (tambah, edit, hapus) untuk admin\n\n" +
                "Aplikasi ini menggunakan penyimpanan lokal SQLite untuk menyimpan data berita.");
        txtAppVersion.setText("Versi 1.0");
    }
}
