package com.release.interfacemodule;

import android.view.View;

public interface ItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
    void onItemDoubleCLick(View view, int position);
    void onDoubleClick(View view, int position);
}
