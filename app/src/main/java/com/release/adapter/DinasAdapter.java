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
import com.release.model.Dinas;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DinasAdapter extends RecyclerView.Adapter<DinasAdapter.DinasViewHolder>  {
    private ArrayList<Dinas> dinasArrayList;
    Context mContext;
    ItemClickListener listener;

    public DinasAdapter(Context mContext, ArrayList<Dinas> dinasArrayList){
        this.dinasArrayList = dinasArrayList;
        this.mContext = mContext;
    }

    public DinasAdapter(Context mContext, ArrayList<Dinas> dinasArrayList, ItemClickListener listener){
        this.dinasArrayList = dinasArrayList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull DinasAdapter.DinasViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull DinasViewHolder holder, int position) {
        holder.dinas_nama.setText(dinasArrayList.get(position).getDinasNama());
    }

    @NonNull
    @Override
    public DinasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listprogress_dinas, parent, false);
        return new DinasViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dinasArrayList.size();
    }

    class DinasViewHolder extends RecyclerView.ViewHolder{
        TextView dinas_nama;

        DinasViewHolder(View itemView){
            super(itemView);
            dinas_nama = itemView.findViewById(R.id.dinas_nama);
        }
    }
}
