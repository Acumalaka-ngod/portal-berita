package com.example.portal_berita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

public class DetailNewsActivity extends Activity {
    private TextView txtDetailJudul, txtDetailKategoriTanggal, txtDetailIsi;
    private ImageView imgDetailBerita;
    private Button btnShare;
    private DatabaseHelper dbHelper;
    private int beritaId;
    private News currentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        txtDetailJudul = findViewById(R.id.txtDetailJudul);
        txtDetailKategoriTanggal = findViewById(R.id.txtDetailKategoriTanggal);
        txtDetailIsi = findViewById(R.id.txtDetailIsi);
        imgDetailBerita = findViewById(R.id.imgDetailBerita);
        btnShare = findViewById(R.id.btnShare);

        dbHelper = new DatabaseHelper(this);
        beritaId = getIntent().getIntExtra("id", -1);

        if (beritaId != -1) {
            loadDetail();
        } else {
            Toast.makeText(this, "Data berita tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnShare.setOnClickListener(v -> shareNews());
    }

    private void loadDetail() {
        currentNews = dbHelper.getBeritaById(beritaId);
        if (currentNews != null) {
            txtDetailJudul.setText(currentNews.getJudul());
            txtDetailKategoriTanggal.setText(currentNews.getKategori() + " | " + currentNews.getTanggal());
            txtDetailIsi.setText(currentNews.getIsi());

            if (currentNews.getGambar() != null && !currentNews.getGambar().isEmpty()) {
                imgDetailBerita.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(currentNews.getGambar())
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.darker_gray)
                        .into(imgDetailBerita);
            } else {
                imgDetailBerita.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Berita tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void shareNews() {
        if (currentNews == null) return;

        String shareContent = currentNews.getJudul() + "\n\n" + currentNews.getIsi();
        if (currentNews.getGambar() != null && !currentNews.getGambar().isEmpty()) {
            shareContent += "\n\nLink Gambar: " + currentNews.getGambar();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentNews.getJudul());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        startActivity(Intent.createChooser(shareIntent, "Bagikan Berita"));
    }
}