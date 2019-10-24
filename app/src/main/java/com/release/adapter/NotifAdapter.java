package com.release.adapter;

import android.app.Notification;
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
import com.release.model.Info;
import com.release.model.Paket;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.util.ArrayList;
import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.PaketViewHolder> implements Filterable{
    private ArrayList<Info> infoArrayList;
    private ArrayList<Info> infoArrayList2;
    private ArrayList<Info> infoArrayListfull;
    Context mContext;
    ItemClickListener listener;
    private List<Paket> paketList2;

    public NotifAdapter(ArrayList<Info> infoArrayList){
        this.infoArrayList = infoArrayList;
    }

    public NotifAdapter(Context mContext, ArrayList<Info> infoArrayList, ItemClickListener listener){
        this.infoArrayList = infoArrayList;
        this.mContext = mContext;
        infoArrayListfull = new ArrayList<>(infoArrayList);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PaketViewHolder holder, int position) {
        holder.info_title.setText(infoArrayList.get(position).getInfoTitle());
        holder.info_caption.setText(infoArrayList.get(position).getInfoCaption());
        holder.info_id.setText(infoArrayList.get(position).getInfoId());
        holder.date_created.setText(infoArrayList.get(position).getDateUpdated());

    }

    @NonNull
    @Override
    public PaketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listinfo, parent, false);
        final PaketViewHolder paketViewHolder = new PaketViewHolder(view);
        return new NotifAdapter.PaketViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return infoArrayList.size();
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
            ArrayList<Info> filteredlist = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredlist.addAll(infoArrayListfull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Info item : infoArrayListfull){
                    if(item.getInfoTitle().toLowerCase().contains(filterPattern)){
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
            infoArrayList.clear();
            infoArrayList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class PaketViewHolder extends RecyclerView.ViewHolder{
        TextView info_title;
        TextView info_caption;
        TextView date_created;
        TextView info_id;

        PaketViewHolder(View itemView){
            super(itemView);

            info_title = itemView.findViewById(R.id.txt_nama_paket);
            info_caption = itemView.findViewById(R.id.txt_pagu);
            date_created = itemView.findViewById(R.id.tx_tanggal_update);
            info_id = itemView.findViewById(R.id.txt_idpaket);
        }
    }


}
