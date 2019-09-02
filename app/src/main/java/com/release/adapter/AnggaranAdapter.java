package com.release.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Anggaran;
import com.release.model.Paket;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.util.ArrayList;
import java.util.List;

public class AnggaranAdapter extends RecyclerView.Adapter<AnggaranAdapter.AnggaranViewHolder> implements Filterable {
    private ArrayList<Anggaran> anggaranList;
    private ArrayList<Anggaran> anggaranListFull;
    Context mContext;
    ItemClickListener listener;
    private List<Paket> anggaranList2;

    public AnggaranAdapter(Context mContext, ArrayList<Anggaran> anggaranList, ItemClickListener listener){
        this.anggaranList = anggaranList;
        this.mContext = mContext;
        anggaranListFull = new ArrayList<>(anggaranList);
        this.listener = listener;
    }

    public AnggaranAdapter(ArrayList<Paket> paketList){
        this.anggaranList2 = paketList;
    }

    public AnggaranAdapter(List<Paket> paketList2){
        this.anggaranList2 = paketList2;
    }

    class AnggaranViewHolder extends RecyclerView.ViewHolder{
        TextView paket_nama;
        TextView paket_pagu;
        TextView paket_jenis;
        TextView paket_id;
        TextView kegiatan_id;
        TextView paket_pagu_dummy;
        RelativeLayout layout_paketid;

        AnggaranViewHolder(View itemView){
            super(itemView);
            paket_nama = itemView.findViewById(R.id.txt_nama_paket);
            paket_pagu = itemView.findViewById(R.id.txt_pagu);
            paket_jenis = itemView.findViewById(R.id.txt_pajenis);
            paket_id = itemView.findViewById(R.id.txt_idpaket);
            kegiatan_id = itemView.findViewById(R.id.kegiatan_id);
            layout_paketid = itemView.findViewById(R.id.layout_paketid);
            paket_pagu_dummy = itemView.findViewById(R.id.txt_pagu_dummy);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AnggaranViewHolder holder, int position) {
        String paket_pagu = formatMoneyIDR.convertIDR(anggaranList.get(position).getAnpPagu());
        holder.paket_nama.setText(anggaranList.get(position).getAnNama());
        holder.paket_pagu.setText("Rp. " + paket_pagu);
        holder.paket_id.setText(anggaranList.get(position).getAnId());
        holder.kegiatan_id.setText(anggaranList.get(position).getKeId());
        if(anggaranList.get(position).getStatus() == ""){
            anggaranList.get(position).setStatus("Umum");
        }
        holder.paket_jenis.setText(anggaranList.get(position).getStatus());
        holder.paket_pagu_dummy.setText(anggaranList.get(position).getAnpPagu());
    }

    @NonNull
    @Override
    public AnggaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listanggaran, parent, false);
        final AnggaranAdapter.AnggaranViewHolder paketViewHolder = new AnggaranAdapter.AnggaranViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, paketViewHolder.getAdapterPosition());
            }
        });
        return new AnggaranAdapter.AnggaranViewHolder(view);
    }

    private Filter anggaranFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Anggaran> filteredlist = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredlist.addAll(anggaranListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Anggaran item : anggaranListFull){
                    if(item.getAnNama().toLowerCase().contains(filterPattern)){
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            anggaranList.clear();
            anggaranList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return anggaranFilter;
    }

    @Override
    public int getItemCount() {
        return anggaranList.size();
    }



}
