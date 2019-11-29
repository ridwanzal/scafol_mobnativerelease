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
import com.release.model.Rencana;
import com.release.model.User;

import java.util.ArrayList;
import java.util.List;

public class KurvaSRencanaAdapter extends RecyclerView.Adapter<KurvaSRencanaAdapter.RencanaViewHolder> {
    private ArrayList<Rencana> rencanaArrayList;
    Context mContext;
    ItemClickListener listener;
    private List<Rencana> rencanaArrayList2;

    public KurvaSRencanaAdapter(Context mContext, ArrayList<Rencana> rencanaArrayList){
        this.rencanaArrayList = rencanaArrayList;
        this.mContext = mContext;
    }

    public KurvaSRencanaAdapter(Context mContext, ArrayList<Rencana> rencanaArrayList, ItemClickListener listener){
        this.mContext = mContext;
        this.rencanaArrayList = rencanaArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RencanaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listrencana, parent, false);
        final KurvaSRencanaAdapter.RencanaViewHolder rencanaViewHolder = new KurvaSRencanaAdapter.RencanaViewHolder(view);
        return new KurvaSRencanaAdapter.RencanaViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return rencanaArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull KurvaSRencanaAdapter.RencanaViewHolder holder, final int position) {
        String date_only = rencanaArrayList.get(position).getReTanggal();
        holder.prog_target.setText(rencanaArrayList.get(position).getReProgress());
        holder.prog_tanggal.setText(date_only);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(view, position, rencanaArrayList.get(position).getReId());
                return true;
            }
        });
    }

    class RencanaViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        TextView prog_target;
        TextView prog_tanggal;
        RencanaViewHolder(View itemView){
            super(itemView);
            layout = itemView.findViewById(R.id.layout_progress_list);
            prog_target = itemView.findViewById(R.id.rencana_target);
            prog_tanggal = itemView.findViewById(R.id.tanggal_rencana);
        }
    }
}
