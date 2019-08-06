package com.release.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.model.Kegiatan;

import java.util.ArrayList;
import java.util.List;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.KegiatanViewHolder>{
    private ArrayList<Kegiatan> kegiatanList;
    private List<Kegiatan> kegiatanList2;

    public KegiatanAdapter(ArrayList<Kegiatan> kegiatanList){
        this.kegiatanList = kegiatanList;
    }

    public KegiatanAdapter(List<Kegiatan> kegiatanList2){
        this.kegiatanList2 = kegiatanList2;
    }

    @NonNull
    @Override
    public KegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listkegiatan, parent, false);
        return new KegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position) {
        holder.keg_nama.setText(kegiatanList.get(position).getKeJudul());
        holder.keg_rek.setText(kegiatanList.get(position).getKeNoRekening());
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    class KegiatanViewHolder extends RecyclerView.ViewHolder{
        TextView keg_nama;
        TextView keg_rek;

        KegiatanViewHolder(View itemView){
            super(itemView);
            keg_nama = itemView.findViewById(R.id.txt_nama_kegiatan);
            keg_rek = itemView.findViewById(R.id.txt_rekening);
        }
    }
}
