package com.example.twittokandroid.ui.bachecaPersonale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.Twok;

public class AdapterBachecaPersonale extends RecyclerView.Adapter<ViewHolderBachecaPersonale> {

    private LayoutInflater inflater;
    private BachecaPersonaleViewModel model;

    public AdapterBachecaPersonale(Context context, BachecaPersonaleViewModel modelInstance){
        this.inflater = LayoutInflater.from(context);
        this.model = modelInstance;
    }

    @NonNull
    @Override
    public ViewHolderBachecaPersonale onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_utente, parent, false);
        return new ViewHolderBachecaPersonale(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBachecaPersonale holder, int position) {
        Twok twok = model.getTwokInList(position);
        holder.updateContent(twok);

    }

    @Override
    public int getItemCount() {
        if(model.getTwoksMutableLiveData().getValue() != null){
            return model.getSize();
        }
        return 0;
    }
}
