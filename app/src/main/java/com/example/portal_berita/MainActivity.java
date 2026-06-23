package com.example.portal_berita;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {
    private RecyclerView recyclerBerita;
    private Button btnTambah, btnSearch, btnCatAll, btnCatPolitik, btnCatOlahraga, btnCatTeknologi, btnCatHiburan;
    private EditText editSearch;
    private ImageView imgAdmin;
    private TextView txtTentang;
    private DatabaseHelper dbHelper;
    private NewsAdapter adapter;
    private ArrayList<News> newsList;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerBerita = findViewById(R.id.recyclerBerita);
        btnTambah = findViewById(R.id.btnTambah);
        btnSearch = findViewById(R.id.btnSearch);
        txtTentang = findViewById(R.id.txtTentang);
        editSearch = findViewById(R.id.editSearch);
        imgAdmin = findViewById(R.id.imgAdmin);

        btnCatAll = findViewById(R.id.btnCatAll);
        btnCatPolitik = findViewById(R.id.btnCatPolitik);
        btnCatOlahraga = findViewById(R.id.btnCatOlahraga);
        btnCatTeknologi = findViewById(R.id.btnCatTeknologi);
        btnCatHiburan = findViewById(R.id.btnCatHiburan);

        dbHelper = new DatabaseHelper(this);
        newsList = new ArrayList<>();

        adapter = new NewsAdapter(newsList, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
                intent.putExtra("id", news.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(News news) {
                if (isAdmin) {
                    showOptionDialog(news);
                } else {
                    Toast.makeText(MainActivity.this, "Silakan login sebagai admin untuk mengelola berita", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        recyclerBerita.setLayoutManager(new LinearLayoutManager(this));
        recyclerBerita.setAdapter(adapter);

        btnCatAll.setOnClickListener(v -> loadData());
        btnCatPolitik.setOnClickListener(v -> filterCategory("Politik"));
        btnCatOlahraga.setOnClickListener(v -> filterCategory("Olahraga"));
        btnCatTeknologi.setOnClickListener(v -> filterCategory("Teknologi"));
        btnCatHiburan.setOnClickListener(v -> filterCategory("Hiburan"));

        imgAdmin.setOnClickListener(v -> {
            if (isAdmin) {
                showLogoutDialog();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnTambah.setOnClickListener(v -> {
            if (isAdmin) {
                startActivity(new Intent(MainActivity.this, AddEditNewsActivity.class));
            } else {
                Toast.makeText(this, "Silakan login sebagai admin untuk menambah berita", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        txtTentang.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        });

        btnSearch.setOnClickListener(v -> {
            String query = editSearch.getText().toString();
            newsList = dbHelper.searchBerita(query);
            adapter.setData(newsList);
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newsList = dbHelper.searchBerita(s.toString());
                adapter.setData(newsList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAdminSession();
        loadData();
    }

    private void checkAdminSession() {
        SharedPreferences prefs = getSharedPreferences("admin_session", MODE_PRIVATE);
        isAdmin = prefs.getBoolean("isAdmin", false);
        updateAdminUI();
    }

    private void updateAdminUI() {
        if (isAdmin) {
            btnTambah.setVisibility(View.VISIBLE);
            imgAdmin.setImageResource(android.R.drawable.ic_menu_manage);
            imgAdmin.setAlpha(1.0f);
        } else {
            btnTambah.setVisibility(View.GONE);
            imgAdmin.setImageResource(R.drawable.user);
            imgAdmin.setAlpha(0.6f);
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Admin")
                .setMessage("Anda sedang login sebagai admin. Ingin logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    SharedPreferences prefs = getSharedPreferences("admin_session", MODE_PRIVATE);
                    prefs.edit().putBoolean("isAdmin", false).apply();
                    isAdmin = false;
                    updateAdminUI();
                    Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void loadData() {
        newsList = dbHelper.getAllBerita();
        adapter.setData(newsList);
    }

    private void filterCategory(String kategori) {
        newsList = dbHelper.getBeritaByKategori(kategori);
        adapter.setData(newsList);
    }

    private void showOptionDialog(News news) {
        String[] options = {"Edit Berita", "Hapus Berita"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent intent = new Intent(MainActivity.this, AddEditNewsActivity.class);
                intent.putExtra("id", news.getId());
                startActivity(intent);
            } else {
                confirmDelete(news);
            }
        });
        builder.show();
    }

    private void confirmDelete(News news) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus berita ini?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            boolean deleted = dbHelper.deleteBerita(news.getId());
            if (deleted) {
                Toast.makeText(this, "Berita berhasil dihapus", Toast.LENGTH_SHORT).show();
                loadData();
            } else {
                Toast.makeText(this, "Berita gagal dihapus", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }
}