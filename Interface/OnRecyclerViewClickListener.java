package com.example.twittokandroid.Interface;

import android.view.View;

import com.example.twittokandroid.Repositories.Twok;

public interface OnRecyclerViewClickListener {
    public void onRecyclerViewClick(View v, int position, Twok twok);
}
