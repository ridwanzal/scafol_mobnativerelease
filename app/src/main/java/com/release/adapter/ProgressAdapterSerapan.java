package com.release.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.interfacemodule.ItemClickListener;
import com.release.model.Progress;
import com.release.model.Serapan;

import java.util.ArrayList;
import com.release.R;
import java.util.List;

public class ProgressAdapterSerapan extends RecyclerView.Adapter<ProgressAdapterSerapan.SerapanViewHolder> {
    private ArrayList<Serapan> serapanArrayList;
    Context mContext;
    ItemClickListener listener;
    private List<Serapan> serapanList;

    public ProgressAdapterSerapan(Context mContext, ArrayList<Serapan> serapanArrayList){
        this.serapanArrayList = serapanArrayList;
        this.mContext = mContext;
    }

    @Override
    public ProgressAdapterSerapan.SerapanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listprogress_serapan, parent, false);
        return new ProgressAdapterSerapan.SerapanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SerapanViewHolder holder, int position) {
        holder.serapan_serap.setText(checkData(serapanArrayList.get(position).getSeDayaSerap()));
        holder.serapan_sisa.setText(checkData(serapanArrayList.get(position).getSeSisa()));
        holder.serapan_tanggal.setText(checkData(serapanArrayList.get(position).getSeTanggal()));
        holder.serapan_keterangan.setText(checkData(serapanArrayList.get(position).getSeKeterangan()));
    }

    @Override
    public int getItemCount() {
        return serapanArrayList.size();
    }

    class SerapanViewHolder extends RecyclerView.ViewHolder{
        TextView serapan_serap;
        TextView serapan_sisa;
        TextView serapan_tanggal;
        TextView serapan_keterangan;

        SerapanViewHolder(View itemView){
            super(itemView);
            serapan_serap = itemView.findViewById(R.id.serap_daya_serap);
            serapan_sisa = itemView.findViewById(R.id.serap_sisa);
            serapan_tanggal = itemView.findViewById(R.id.serap_tanggal);
            serapan_keterangan = itemView.findViewById(R.id.serap_keterangan);
        }
    }

    public static String checkData(String data){
        if(data == null || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }


}
