package com.example.twittokandroid.ui.bachecaTwok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twittokandroid.Interface.OnRecyclerViewClickListener;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.Twok;

import java.util.List;

public class AdapterTwok extends RecyclerView.Adapter<ViewHolderTwok> {

    private LayoutInflater inflater;
    private TwokRepository twokRepository;
    private List<Twok> list;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;
    private Context context;

    public AdapterTwok(Context context, OnRecyclerViewClickListener recyclerViewClickListener, TwokRepository twokRepositoryIntance) {
        this.inflater = LayoutInflater.from(context);
        this.twokRepository = twokRepositoryIntance;
        this.onRecyclerViewClickListener = recyclerViewClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderTwok onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row, parent, false);
        return new ViewHolderTwok(view, onRecyclerViewClickListener, context, twokRepository);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTwok holder, int position) {
        Twok twok = twokRepository.getTwokInList(position);
        //Log.d("TAG", twok.getText());
        holder.updateContent(twok);
    }

    @Override
    public int getItemCount() {
        if(twokRepository.getTwoksMutableLiveData() != null){
            return twokRepository.getSize();
        }
        return 0;
    }

    public void updateList(List<Twok> newList){
        this.notifyDataSetChanged();
    }

}
