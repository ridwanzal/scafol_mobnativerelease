package com.release.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;

import com.release.model.User;

import java.util.ArrayList;
import java.util.List;

public class KontraktorAdapter extends RecyclerView.Adapter<KontraktorAdapter.KontraktorViewHolder>{
    private ArrayList<User> userList;
    private List<User> userList2;

    public KontraktorAdapter(ArrayList<User> userList){
        this.userList = userList;
    }

    public KontraktorAdapter(List<User> userList2){
        this.userList2 = userList2;
    }

    @Override
    public KontraktorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listuser, parent, false);
        return new KontraktorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KontraktorViewHolder holder, int position) {
        holder.user_nama.setText(userList.get(position).getNama());
    }

    @Override
    public void onBindViewHolder(@NonNull KontraktorViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class KontraktorViewHolder extends RecyclerView.ViewHolder{
        TextView user_nama;
        TextView keg_rek;

        KontraktorViewHolder(View itemView){
            super(itemView);
            user_nama = itemView.findViewById(R.id.txt_namauser);
        }
    }
}
