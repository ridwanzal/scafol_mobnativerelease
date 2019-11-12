package com.release.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.model.Kegiatan;
import com.release.model.KegiatanTree;

import java.util.ArrayList;
import java.util.List;

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
//        holder.keg_rek.setText(kegiatanList.get(position).getKeNoRekening());

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
        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
            TextView currentTextView = (TextView) holder.child_layout.getChildAt(textViewIndex);
            currentTextView.setText(kegiatanTree.getChild().get(textViewIndex).getPaJudul());
                /*currentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "" + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/
        }
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    class KegiatanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private Context context;
            private TextView keg_nama;
            private LinearLayout layout_kegiatan;
            private TextView keg_rek;
            private LinearLayout child_layout;
            KegiatanViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                keg_nama = itemView.findViewById(R.id.txt_nama_kegiatan);
                layout_kegiatan = itemView.findViewById(R.id.layout_kegiatan);
                child_layout = itemView.findViewById(R.id.ll_child_items);
                child_layout.setVisibility(View.GONE);
                int intMaxNoOfChild = 0;
                for (int index = 0; index < kegiatanList.size(); index++) {
                    int intMaxSizeTemp = kegiatanList.get(index).getChild().size();
                    if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
                }
                for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                    TextView textView = new TextView(context);
                    textView.setId(indexView);
                    textView.setPadding(60, 40, 0, 40);
                    textView.setGravity(Gravity.LEFT);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_right_black_24dp, 0, 0, 0);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textView.setOnClickListener(this);
                    child_layout.addView(textView, layoutParams);
                }
                layout_kegiatan.setOnClickListener(this);
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
                    Toast.makeText(context, "" + textViewClicked.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
