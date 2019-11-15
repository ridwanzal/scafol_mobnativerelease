package com.release.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.activity.ActivityDetailPaket;
import com.release.model.Kegiatan;
import com.release.model.KegiatanTree;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.release.sharedexternalmodule.formatMoneyIDR;

import es.dmoral.toasty.Toasty;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.KegiatanViewHolder>{
    private ArrayList<KegiatanTree> kegiatanList;
    private List<KegiatanTree> kegiatanList2;

    public KegiatanAdapter(ArrayList<KegiatanTree> kegiatanList){
        this.kegiatanList = kegiatanList;
    }

    public KegiatanAdapter(List<KegiatanTree> kegiatanList2){
        this.kegiatanList2 = kegiatanList2;
    }

    @NonNull
    @Override
    public KegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listkegiatan, parent, false);
        return new KegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position) {
        KegiatanTree kegiatanTree = kegiatanList.get(position);
        holder.keg_nama.setText(kegiatanList.get(position).getKeJudul());
        int noOfChildTextViews = holder.child_layout.getChildCount();
        for (int index = 0; index < noOfChildTextViews; index++) {
            TextView currentTextView = (TextView) holder.child_layout.getChildAt(index);
            currentTextView.setVisibility(View.VISIBLE);
        }

        int noOfChild = kegiatanTree.getChild().size();
        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {
                TextView currentTextView = (TextView) holder.child_layout.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }

        Log.d("KegiatanAdapter", "noOfChild hahah : " + kegiatanTree.getChild().size() + " | ");
        int total_pagu = 0;
        BigInteger sum = new BigInteger("0");
        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
            TextView currentTextView = (TextView) holder.child_layout.getChildAt(textViewIndex);
            try{
                String get_pagu = String.valueOf(kegiatanTree.getChild().get(textViewIndex).getPaPagu());
                Log.d("KegiatanAdapter", "pagu hahah : " + get_pagu + " | " + kegiatanTree.getChild().get(textViewIndex).getKeId());
                if(get_pagu.equals("")){
                    get_pagu = "0";
                }
//                int parse_pagus = Integer.parseInt(get_pagu);
//                total_pagu += Integer.valueOf(parse_pagus);

                BigInteger pagu_now = new BigInteger(get_pagu.toString());
                Log.d("KegiatanAdapter", "pagu now : " + pagu_now + " | " + kegiatanTree.getChild().get(textViewIndex).getKeId());
                sum = sum.add(pagu_now);

            }catch (Exception e){
                e.printStackTrace();
            }
            currentTextView.setText(kegiatanTree.getChild().get(textViewIndex).getPaId() + " - " + kegiatanTree.getChild().get(textViewIndex).getPaJudul() + "");
        }
        holder.keg_pagu.setText("Total Pagu : Rp. "  + formatMoneyIDR.convertIDR(sum.toString()));
        Log.d("KegiatanAdapter", "Total pagu sekarang : " + total_pagu + " | ");
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    class KegiatanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            private Context context;
            private TextView keg_nama;
            private LinearLayout layout_kegiatan;
            private TextView keg_rek;
            private LinearLayout child_layout;
            private TextView keg_pagu;
            KegiatanViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                keg_nama = itemView.findViewById(R.id.txt_nama_kegiatan);
                keg_pagu = itemView.findViewById(R.id.total_pagu_kegiatan);
                layout_kegiatan = itemView.findViewById(R.id.layout_kegiatan);
                child_layout = itemView.findViewById(R.id.ll_child_items);
                child_layout.setVisibility(View.GONE);
                int intMaxNoOfChild = 0;
                for (int index = 0; index < kegiatanList.size(); index++) {
                    int intMaxSizeTemp = kegiatanList.get(index).getChild().size();
                    if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
                    TextView textView = new TextView(context);
                    textView.setId(index);
                    textView.setText("No data found");
                    textView.setPadding(60, 40, 30, 40);
                    textView.setGravity(Gravity.LEFT);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next, 0, 0, 0);
                    textView.setCompoundDrawablePadding(10);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textView.setOnClickListener(this);

                    child_layout.addView(textView, layoutParams);
                }
                for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                    try{

                        TextView textView = new TextView(context);
                        textView.setId(indexView);
                        textView.setPadding(60, 40, 30, 40);
                        textView.setGravity(Gravity.LEFT);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_next, 0, 0, 0);
                        textView.setCompoundDrawablePadding(10);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textView.setOnClickListener(this);
                        child_layout.addView(textView, layoutParams);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                layout_kegiatan.setOnClickListener(this);
                layout_kegiatan.setOnLongClickListener(this);
            }

        @Override
        public boolean onLongClick(View v) {
            if(v.getId() == R.id.layout_kegiatan){
                if(child_layout.getVisibility() == View.VISIBLE){
                    child_layout.setVisibility(View.VISIBLE);
                }else{
                    child_layout.setVisibility(View.VISIBLE);
                }
            }else{
                Toast.makeText(context, "On long click", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
            public void onClick(View v) {
                if(v.getId() == R.id.layout_kegiatan){
                    if(child_layout.getVisibility() == View.VISIBLE){
                        child_layout.setVisibility(View.GONE);
                    }else{
                        child_layout.setVisibility(View.VISIBLE);
                    }
                }else{
                    TextView textViewClicked = (TextView) v;
                    String[] get_text = textViewClicked.getText().toString().split(" - ");
//                    Toast.makeText(context, "" + get_text[0], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ActivityDetailPaket.class);
                    intent.putExtra("pa_id", get_text[0].toString());
                    intent.putExtra("pa_nama", get_text[1].toString());
                    intent.putExtra("pa_pagu", "");
                    intent.putExtra("ke_id", "");
                    intent.putExtra("request", "main");
                    context.startActivity(intent);
                }
            }
        }
    }
