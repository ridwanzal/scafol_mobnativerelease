package com.release.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Progress;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.util.ArrayList;
import java.util.List;

public class ProgressAdapterKeuangan extends RecyclerView.Adapter<ProgressAdapterKeuangan.ProgressViewHolder> {
    private ArrayList<Progress> progressArrayList;
    Context mContext;
    ItemClickListener listener;
    private List<Progress> progressArrayList2s;

    public ProgressAdapterKeuangan(Context mContext, ArrayList<Progress> progressArrayList){
        this.progressArrayList = progressArrayList;
        this.mContext = mContext;
    }

    public ProgressAdapterKeuangan(Context mContext, ArrayList<Progress> progressArrayList, ItemClickListener listener){
        this.progressArrayList = progressArrayList;
        this.mContext = mContext;
        progressArrayList2s = new ArrayList<>(progressArrayList2s);
        this.listener = listener;
    }

    @Override
    public ProgressAdapterKeuangan.ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listprogress_keuangan, parent, false);
        final ProgressAdapterKeuangan.ProgressViewHolder paketViewHolder = new ProgressAdapterKeuangan.ProgressViewHolder(view);
        return new ProgressAdapterKeuangan.ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressAdapterKeuangan.ProgressViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        holder.progkeu_serapan.setText(formatMoneyIDR.convertIDR(progressArrayList.get(position).getPr_daya_serap_kontrak()));
        holder.progkeu_sisakontrak.setText(formatMoneyIDR.convertIDR(progressArrayList.get(position).getPr_sisa_kontrak()));
        holder.progkeu_sisaanggaran.setText(formatMoneyIDR.convertIDR(progressArrayList.get(position).getPr_sisa_anggaran()));
        holder.progkeu_tanggal.setText(progressArrayList.get(position).getPr_tanggal());
        holder.progkeu_keterangan.setText(progressArrayList.get(position).getPr_keterangan());
    }

    @Override
    public int getItemCount() {
        return progressArrayList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder{
        TextView progkeu_serapan;
        TextView progkeu_sisakontrak;
        TextView progkeu_sisaanggaran;
        TextView progkeu_tanggal;
        TextView progkeu_keterangan;

        ProgressViewHolder(View itemView){
            super(itemView);
            progkeu_serapan = itemView.findViewById(R.id.progkeu_serap);
            progkeu_sisakontrak = itemView.findViewById(R.id.progkeu_sisakontrak);
            progkeu_sisaanggaran = itemView.findViewById(R.id.progkeu_sisaanggaran);
            progkeu_tanggal = itemView.findViewById(R.id.progkeu_tanggal);
            progkeu_keterangan = itemView.findViewById(R.id.progkeu_keterangan);
        }
    }
}