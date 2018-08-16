package com.adnheim.mynotesapp;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallback onItemClickCallBack;

    public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallBack) {
        this.position = position;
        this.onItemClickCallBack = onItemClickCallBack;
    }
    @Override
    public void onClick(View v) {

        onItemClickCallBack.onItemClicked(v, position);
    }
    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
