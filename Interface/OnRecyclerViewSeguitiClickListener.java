package com.example.twittokandroid.Interface;

import android.view.View;

import com.example.twittokandroid.Repositories.Profile;

public interface OnRecyclerViewSeguitiClickListener {
    void onRecyclerViewClick(View v, int position, Profile profile);
}
