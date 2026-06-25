package com.portalberita.atta;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditNewsActivity extends Activity {
    private TextView txtFormTitle, txtCharCount;
    private EditText etJudul, etPenulis, etIsi, etGambar;
    private Spinner spinnerKategori;
    private Button btnSimpan;
    private DatabaseHelper dbHelper;
    private int beritaId = -1;
    private String selectedKategori = "";

    private static final String[] KATEGORI_LIST = {"Pendidikan", "Politik", "Olahraga", "Teknologi", "Hiburan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_news);

        txtFormTitle = findViewById(R.id.txtFormTitle);
        txtCharCount = findViewById(R.id.txtCharCount);
        etJudul = findViewById(R.id.etJudul);
        etPenulis = findViewById(R.id.etPenulis);
        etIsi = findViewById(R.id.etIsi);
        etGambar = findViewById(R.id.etGambar);
        spinnerKategori = findViewById(R.id.spinnerKategori);
        btnSimpan = findViewById(R.id.btnSimpan);

        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, KATEGORI_LIST);
        spinnerKategori.setAdapter(adapter);
        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKategori = KATEGORI_LIST[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedKategori = KATEGORI_LIST[0];
            }
        });

        etIsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtCharCount.setText(s.length() + " karakter (min. 50)");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

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
            etPenulis.setText(news.getPenulis());
            etIsi.setText(news.getIsi());
            etGambar.setText(news.getGambar());

            for (int i = 0; i < KATEGORI_LIST.length; i++) {
                if (KATEGORI_LIST[i].equals(news.getKategori())) {
                    spinnerKategori.setSelection(i);
                    break;
                }
            }

            txtCharCount.setText(news.getIsi().length() + " karakter (min. 50)");
        }
    }

    private void saveData() {
        String judul = etJudul.getText().toString().trim();
        String penulis = etPenulis.getText().toString().trim();
        String isi = etIsi.getText().toString().trim();
        String gambar = etGambar.getText().toString().trim();

        if (judul.isEmpty()) {
            etJudul.setError("Judul tidak boleh kosong");
            etJudul.requestFocus();
            return;
        }
        if (penulis.isEmpty()) {
            etPenulis.setError("Nama penulis tidak boleh kosong");
            etPenulis.requestFocus();
            return;
        }
        if (isi.isEmpty()) {
            etIsi.setError("Isi berita tidak boleh kosong");
            etIsi.requestFocus();
            return;
        }
        if (isi.length() < 50) {
            etIsi.setError("Isi berita minimal 50 karakter");
            etIsi.requestFocus();
            return;
        }

        String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        boolean success;
        if (beritaId == -1) {
            success = dbHelper.insertBerita(judul, selectedKategori, penulis, isi, tanggal, gambar);
            if (success) {
                Toast.makeText(this, "Berita berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Berita gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        } else {
            success = dbHelper.updateBerita(beritaId, judul, selectedKategori, penulis, isi, tanggal, gambar);
            if (success) {
                Toast.makeText(this, "Berita berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Berita gagal diperbarui", Toast.LENGTH_SHORT).show();
            }
        }
    }
}