package com.release.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Catatan;
import com.release.model.Progress;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder> {
    private ArrayList<Catatan> catatanArrayList;
    Context mContext;
    ItemClickListener listener;
    public static Boolean is_clicked = true;

    public CatatanAdapter(Context mContext, ArrayList<Catatan> catatanArrayList){
        this.catatanArrayList = catatanArrayList;
        this.mContext = mContext;
    }

    public CatatanAdapter(Context mContext, ArrayList<Catatan> catatanArrayList, ItemClickListener listener){
        this.mContext = mContext;
        this.catatanArrayList = catatanArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CatatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listcatatan, parent, false);
        final CatatanViewHolder catatanViewHolder = new CatatanViewHolder(view);
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final int position = catatanViewHolder.getAdapterPosition();
//                listener.onItemLongClick(view, position);
//                notifyItemChanged(position);
//                return true;
//            }
//        });


        return new CatatanViewHolder(view);
    }


    class CatatanViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout container_catatan;
        TextView ca_catatan;
        TextView date_created;

        CatatanViewHolder(View itemView){
            super(itemView);
            container_catatan = itemView.findViewById(R.id.container_catatan);
            ca_catatan = itemView.findViewById(R.id.ca_ca_catatan);
            date_created = itemView.findViewById(R.id.ca_date_created);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CatatanViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(final CatatanViewHolder holder, final int position) {
        holder.ca_catatan.setText(catatanArrayList.get(position).getCaCatatan());
        holder.date_created.setText(catatanArrayList.get(position).getDateCreated());
        holder.container_catatan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(view, position);
                if(is_clicked){
                    holder.container_catatan.setBackgroundColor(Color.parseColor("#eeeeee"));
                    is_clicked = false;
                }else{
                    holder.container_catatan.setBackgroundColor(Color.parseColor("#ffffff"));
                    is_clicked = true;
                }

                return true;
            }
        });

//        holder.container_catatan.setBackgroundColor(Color.parseColor("#eeeeee"));
    }

    @Override
    public int getItemCount() {
        return catatanArrayList.size();
    }

    public ArrayList<Catatan> getDataSet(){
        return catatanArrayList;
    }

    public void removeData(ArrayList<Catatan> catatanArrayList){
        for(Catatan catatan : catatanArrayList){
            catatanArrayList.remove(catatan);
        }
        notifyDataSetChanged();
    }

    public void changeDataItem(int position, Catatan catatan){
        catatanArrayList.set(position, catatan);
        notifyDataSetChanged();
    }
}
