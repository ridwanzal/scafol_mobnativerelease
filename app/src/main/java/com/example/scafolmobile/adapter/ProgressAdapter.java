package com.example.scafolmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scafolmobile.R;
import com.example.scafolmobile.interfacemodule.ItemClickListener;
import com.example.scafolmobile.model.Progress;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {
    private ArrayList<Progress> progressArrayList;
    Context mContext;
    ItemClickListener listener;
    private List<Progress> progressArrayList2s;

    public ProgressAdapter(Context mContext, ArrayList<Progress> progressArrayList){
        this.progressArrayList = progressArrayList;
        this.mContext = mContext;
    }

    public ProgressAdapter(Context mContext, ArrayList<Progress> progressArrayList, ItemClickListener listener){
        this.progressArrayList = progressArrayList;
        this.mContext = mContext;
        progressArrayList2s = new ArrayList<>(progressArrayList2s);
        this.listener = listener;
    }

    @Override
    public ProgressAdapter.ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listprogress, parent, false);
        final ProgressAdapter.ProgressViewHolder paketViewHolder = new ProgressAdapter.ProgressViewHolder(view);
        return new ProgressAdapter.ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressAdapter.ProgressViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        holder.prog_target.setText(progressArrayList.get(position).getPr_target());
        holder.prog_real.setText(progressArrayList.get(position).getPr_real());
        holder.prog_deviasi.setText(progressArrayList.get(position).getPr_deviasi());
        holder.prog_tanggal.setText(progressArrayList.get(position).getDate_created());
        holder.prog_tanggal_update.setText(progressArrayList.get(position).getDate_updated());
    }

    @Override
    public int getItemCount() {
        return progressArrayList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder{
        TextView prog_target;
        TextView prog_real;
        TextView prog_deviasi;
        TextView prog_tanggal_update;
        TextView prog_tanggal;

        ProgressViewHolder(View itemView){
            super(itemView);
            prog_target = itemView.findViewById(R.id.prog_target_list);
            prog_real = itemView.findViewById(R.id.prog_real_list);
            prog_deviasi = itemView.findViewById(R.id.prog_deviasi_list);
            prog_tanggal = itemView.findViewById(R.id.prog_tanggal);
            prog_tanggal_update = itemView.findViewById(R.id.prog_tanggal_update);
        }
    }
}
