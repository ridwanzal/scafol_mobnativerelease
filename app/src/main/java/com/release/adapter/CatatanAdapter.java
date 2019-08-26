package com.release.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.model.Catatan;
import com.release.model.Progress;

import java.util.ArrayList;
import java.util.List;

public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder> {
    private ArrayList<Catatan> catatanArrayList;
    Context mContext;
    public CatatanAdapter(){
        this.catatanArrayList = catatanArrayList;
    }


    public CatatanAdapter(Context mContext, ArrayList<Catatan> catatanArrayList){
        this.catatanArrayList = catatanArrayList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public CatatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listcatatan, parent, false);
        return new CatatanAdapter.CatatanViewHolder(view);
    }


    class CatatanViewHolder extends RecyclerView.ViewHolder{
        TextView ca_catatan;
        TextView date_created;

        CatatanViewHolder(View itemView){
            super(itemView);
            ca_catatan = itemView.findViewById(R.id.ca_ca_catatan);
            date_created = itemView.findViewById(R.id.ca_date_created);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CatatanViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull CatatanViewHolder holder, int position) {
        holder.ca_catatan.setText(catatanArrayList.get(position).getCaCatatan());
        holder.date_created.setText(catatanArrayList.get(position).getDateCreated());
    }

    @Override
    public int getItemCount() {
        return catatanArrayList.size();
    }
}
