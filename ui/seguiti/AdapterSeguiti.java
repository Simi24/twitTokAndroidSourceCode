package com.example.twittokandroid.ui.seguiti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twittokandroid.Interface.OnRecyclerViewSeguitiClickListener;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.Profile;

public class AdapterSeguiti extends RecyclerView.Adapter<SeguitiViewHolder> {

    private LayoutInflater inflater;
    private SeguitiViewModel model;
    private OnRecyclerViewSeguitiClickListener onRecyclerViewSeguitiClickListener;
    private Context context;

    public AdapterSeguiti(Context context, SeguitiViewModel seguitiViewModel, OnRecyclerViewSeguitiClickListener onRecyclerViewSeguitiClickListener){
        this.inflater = LayoutInflater.from(context);
        this.model = seguitiViewModel;
        this.onRecyclerViewSeguitiClickListener = onRecyclerViewSeguitiClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public SeguitiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_seguiti, parent, false);
        return new SeguitiViewHolder(view, onRecyclerViewSeguitiClickListener, model, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SeguitiViewHolder holder, int position) {
        Profile profile = model.getProfileInList(position);
        holder.updateContent(profile);
    }

    @Override
    public int getItemCount() {
        return model.getSize();
    }

    public void updateList(){
        this.notifyDataSetChanged();
    }
}
