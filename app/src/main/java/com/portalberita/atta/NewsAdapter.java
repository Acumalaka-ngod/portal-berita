package com.portalberita.atta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<News> newsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(News news);
        void onItemLongClick(News news);
    }

    public NewsAdapter(ArrayList<News> newsList, OnItemClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    public void setData(ArrayList<News> newList) {
        this.newsList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.txtJudul.setText(news.getJudul());
        holder.txtKategoriTanggal.setText(news.getKategori() + " | " + news.getTanggal());
        
        String isi = news.getIsi();
        if (isi.length() > 120) {
            isi = isi.substring(0, 120) + "...";
        }
        holder.txtIsiSingkat.setText(isi);

        // Load image with Glide
        if (news.getGambar() != null && !news.getGambar().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(news.getGambar())
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(holder.imgBerita);
        } else {
            holder.imgBerita.setImageResource(android.R.color.darker_gray);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(news));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(news);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView txtJudul, txtKategoriTanggal, txtIsiSingkat;
        ImageView imgBerita;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtJudul = itemView.findViewById(R.id.txtJudul);
            txtKategoriTanggal = itemView.findViewById(R.id.txtKategoriTanggal);
            txtIsiSingkat = itemView.findViewById(R.id.txtIsiSingkat);
            imgBerita = itemView.findViewById(R.id.imgBerita);
        }
    }
}