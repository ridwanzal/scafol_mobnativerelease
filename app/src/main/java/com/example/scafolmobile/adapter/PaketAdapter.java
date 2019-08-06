package com.example.scafolmobile.adapter;

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

import com.example.scafolmobile.R;
import com.example.scafolmobile.model.Paket;
import com.example.scafolmobile.sharedexternalmodule.formatMoneyIDR;
import com.example.scafolmobile.interfacemodule.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PaketAdapter extends RecyclerView.Adapter<PaketAdapter.PaketViewHolder> implements Filterable{
    private ArrayList<Paket> paketList;
    private ArrayList<Paket> paketListFull;
    Context mContext;
    ItemClickListener listener;
    private List<Paket> paketList2;

    public PaketAdapter(ArrayList<Paket> paketList){
        this.paketList = paketList;
    }

    public PaketAdapter(List<Paket> paketList2){
        this.paketList2 = paketList2;
    }

    public PaketAdapter(Context mContext, ArrayList<Paket> paketList, ItemClickListener listener){
        this.paketList = paketList;
        this.mContext = mContext;
        paketListFull = new ArrayList<>(paketList);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PaketViewHolder holder, int position) {
        String paket_pagu = formatMoneyIDR.convertIDR(paketList.get(position).getPaPagu());
        holder.paket_nama.setText(paketList.get(position).getPaJudul());
        holder.paket_pagu.setText(paket_pagu);
        holder.paket_id.setText(paketList.get(position).getPaId());
        holder.kegiatan_id.setText(paketList.get(position).getKeId());
        if(paketList.get(position).getPaJenis() == ""){
            paketList.get(position).setPaJenis("Umum");
        }
        holder.paket_jenis.setText(paketList.get(position).getPaJenis());
    }

    @NonNull
    @Override
    public PaketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listpaket, parent, false);
        final PaketViewHolder paketViewHolder = new PaketViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, paketViewHolder.getAdapterPosition());
            }
        });
        return new PaketAdapter.PaketViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return paketList.size();
    }

    @Override
    public Filter getFilter() {
        return paketFilter;
    }

    private Filter paketFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Paket> filteredlist = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredlist.addAll(paketListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Paket item : paketListFull){
                    if(item.getPaJudul().toLowerCase().contains(filterPattern)){
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
            paketList.clear();
            paketList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class PaketViewHolder extends RecyclerView.ViewHolder{
        TextView paket_nama;
        TextView paket_pagu;
        TextView paket_jenis;
        TextView paket_id;
        TextView kegiatan_id;
        RelativeLayout layout_paketid;

        PaketViewHolder(View itemView){
            super(itemView);
            paket_nama = itemView.findViewById(R.id.txt_nama_paket);
            paket_pagu = itemView.findViewById(R.id.txt_pagu);
            paket_jenis = itemView.findViewById(R.id.txt_pajenis);
            paket_id = itemView.findViewById(R.id.txt_idpaket);
            kegiatan_id = itemView.findViewById(R.id.kegiatan_id);
            layout_paketid = itemView.findViewById(R.id.layout_paketid);
        }
    }


}
