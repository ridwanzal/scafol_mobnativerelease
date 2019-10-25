package com.release.interfacemodule;

import android.view.View;

public interface ItemClickListener {
    void onItemClick(View view, int position);
    void onItemClick(View view, int position, String param1);
    boolean onItemLongClick(View view, int position, String id_list);
    void onItemDoubleCLick(View view, int position);
    void onDoubleClick(View view, int position);
}
