package com.example.portal_berita;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "portal_berita.db";
    private static final int DATABASE_VERSION = 3;
    
    public static final String TABLE_BERITA = "berita";
    public static final String COL_ID = "id";
    public static final String COL_JUDUL = "judul";
    public static final String COL_KATEGORI = "kategori";
    public static final String COL_PENULIS = "penulis";
    public static final String COL_ISI = "isi";
    public static final String COL_TANGGAL = "tanggal";
    public static final String COL_GAMBAR = "gambar";

    public static final String TABLE_ADMIN = "admin";
    public static final String COL_ADMIN_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBeritaTable = "CREATE TABLE " + TABLE_BERITA + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_JUDUL + " TEXT NOT NULL, " +
                COL_KATEGORI + " TEXT NOT NULL, " +
                COL_PENULIS + " TEXT NOT NULL, " +
                COL_ISI + " TEXT NOT NULL, " +
                COL_TANGGAL + " TEXT NOT NULL, " +
                COL_GAMBAR + " TEXT)";
        db.execSQL(createBeritaTable);

        String createAdminTable = "CREATE TABLE " + TABLE_ADMIN + " (" +
                COL_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createAdminTable);

        // Default admin
        db.execSQL("INSERT INTO " + TABLE_ADMIN + " (" + COL_USERNAME + ", " + COL_PASSWORD + ") VALUES ('admin', 'admin123')");

        // Initial sample
        db.execSQL("INSERT INTO " + TABLE_BERITA + " (" + COL_JUDUL + ", " + COL_KATEGORI + ", " + COL_PENULIS + ", " +
                COL_ISI + ", " + COL_TANGGAL + ", " + COL_GAMBAR + ") VALUES ('Kampus Mengadakan Seminar Teknologi', " +
                "'Pendidikan', 'Admin', 'Universitas mengadakan seminar teknologi untuk meningkatkan literasi digital mahasiswa.', '01-06-2026', '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_BERITA + " ADD COLUMN " + COL_GAMBAR + " TEXT");
            String createAdminTable = "CREATE TABLE " + TABLE_ADMIN + " (" +
                    COL_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USERNAME + " TEXT NOT NULL, " +
                    COL_PASSWORD + " TEXT NOT NULL)";
            db.execSQL(createAdminTable);
            db.execSQL("INSERT INTO " + TABLE_ADMIN + " (" + COL_USERNAME + ", " + COL_PASSWORD + ") VALUES ('admin', 'admin123')");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_BERITA + " ADD COLUMN " + COL_PENULIS + " TEXT NOT NULL DEFAULT ''");
        }
    }

    public boolean insertBerita(String judul, String kategori, String penulis, String isi, String tanggal, String gambar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_JUDUL, judul);
        values.put(COL_KATEGORI, kategori);
        values.put(COL_PENULIS, penulis);
        values.put(COL_ISI, isi);
        values.put(COL_TANGGAL, tanggal);
        values.put(COL_GAMBAR, gambar);
        long result = db.insert(TABLE_BERITA, null, values);
        return result != -1;
    }

    public ArrayList<News> getAllBerita() {
        ArrayList<News> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BERITA + " ORDER BY " + COL_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new News(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_JUDUL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_KATEGORI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PENULIS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ISI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TANGGAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_GAMBAR))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<News> searchBerita(String query) {
        ArrayList<News> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BERITA + " WHERE " + COL_JUDUL + " LIKE ? OR " + COL_KATEGORI + " LIKE ? ORDER BY " + COL_ID + " DESC",
                new String[]{"%" + query + "%", "%" + query + "%"});
        if (cursor.moveToFirst()) {
            do {
                list.add(new News(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_JUDUL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_KATEGORI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PENULIS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ISI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TANGGAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_GAMBAR))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<News> getBeritaByKategori(String kategori) {
        ArrayList<News> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BERITA + " WHERE " + COL_KATEGORI + " = ? ORDER BY " + COL_ID + " DESC",
                new String[]{kategori});
        if (cursor.moveToFirst()) {
            do {
                list.add(new News(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_JUDUL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_KATEGORI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PENULIS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ISI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TANGGAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_GAMBAR))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public News getBeritaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BERITA + " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(id)});
        News news = null;
        if (cursor.moveToFirst()) {
            news = new News(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_JUDUL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_KATEGORI)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PENULIS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ISI)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TANGGAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_GAMBAR))
            );
        }
        cursor.close();
        return news;
    }

    public boolean updateBerita(int id, String judul, String kategori, String penulis, String isi, String tanggal, String gambar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_JUDUL, judul);
        values.put(COL_KATEGORI, kategori);
        values.put(COL_PENULIS, penulis);
        values.put(COL_ISI, isi);
        values.put(COL_TANGGAL, tanggal);
        values.put(COL_GAMBAR, gambar);
        return db.update(TABLE_BERITA, values, COL_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteBerita(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BERITA, COL_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean checkAdminLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMIN + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}