package com.release.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Progress;

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
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, final int position) {
        String[] date_only = progressArrayList.get(position).getPr_tanggal().split(" ");
        String[] date_only2 = progressArrayList.get(position).getDate_updated().split(" ");
        holder.prog_target.setText(progressArrayList.get(position).getPr_target());
        holder.prog_real.setText(progressArrayList.get(position).getPr_real());
        holder.prog_deviasi.setText(progressArrayList.get(position).getPr_deviasi());
        holder.prog_tanggal.setText(date_only[0]);
        holder.prog_tanggal_update.setText(date_only2[0]);
        holder.layout_progress_list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(view, position, progressArrayList.get(position).getPr_id());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return progressArrayList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout_progress_list;
        TextView prog_target;
        TextView prog_real;
        TextView prog_deviasi;
        TextView prog_tanggal_update;
        TextView prog_tanggal;

        ProgressViewHolder(View itemView){
            super(itemView);
            layout_progress_list = itemView.findViewById(R.id.layout_progress_list);
            prog_target = itemView.findViewById(R.id.prog_target_list);
            prog_real = itemView.findViewById(R.id.prog_real_list);
            prog_deviasi = itemView.findViewById(R.id.prog_deviasi_list);
            prog_tanggal = itemView.findViewById(R.id.prog_tanggal);
            prog_tanggal_update = itemView.findViewById(R.id.prog_tanggal_update);
        }
    }
}
