package com.example.portal_berita;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditNewsActivity extends Activity {
    private TextView txtFormTitle;
    private EditText etJudul, etKategori, etIsi, etGambar;
    private Button btnSimpan;
    private DatabaseHelper dbHelper;
    private int beritaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_news);

        txtFormTitle = findViewById(R.id.txtFormTitle);
        etJudul = findViewById(R.id.etJudul);
        etKategori = findViewById(R.id.etKategori);
        etIsi = findViewById(R.id.etIsi);
        etGambar = findViewById(R.id.etGambar);
        btnSimpan = findViewById(R.id.btnSimpan);

        dbHelper = new DatabaseHelper(this);

        beritaId = getIntent().getIntExtra("id", -1);

        if (beritaId != -1) {
            txtFormTitle.setText("Edit Berita");
            loadDataForEdit();
        } else {
            txtFormTitle.setText("Tambah Berita");
        }

        btnSimpan.setOnClickListener(v -> saveData());
    }

    private void loadDataForEdit() {
        News news = dbHelper.getBeritaById(beritaId);
        if (news != null) {
            etJudul.setText(news.getJudul());
            etKategori.setText(news.getKategori());
            etIsi.setText(news.getIsi());
            etGambar.setText(news.getGambar());
        }
    }

    private void saveData() {
        String judul = etJudul.getText().toString().trim();
        String kategori = etKategori.getText().toString().trim();
        String isi = etIsi.getText().toString().trim();
        String gambar = etGambar.getText().toString().trim();

        if (judul.isEmpty()) {
            etJudul.setError("Judul tidak boleh kosong");
            return;
        }
        if (kategori.isEmpty()) {
            etKategori.setError("Kategori tidak boleh kosong");
            return;
        }
        if (isi.isEmpty()) {
            etIsi.setError("Isi berita tidak boleh kosong");
            return;
        }

        String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        boolean success;
        if (beritaId == -1) {
            success = dbHelper.insertBerita(judul, kategori, isi, tanggal, gambar);
            if (success) {
                Toast.makeText(this, "Berita berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Berita gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        } else {
            success = dbHelper.updateBerita(beritaId, judul, kategori, isi, tanggal, gambar);
            if (success) {
                Toast.makeText(this, "Berita berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Berita gagal diperbarui", Toast.LENGTH_SHORT).show();
            }
        }
    }
}