package com.example.twittokandroid.ui.seguiti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twittokandroid.Interface.OnRecyclerViewSeguitiClickListener;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.PictureRepository;
import com.example.twittokandroid.Repositories.Profile;
import com.example.twittokandroid.Repositories.SidRepository;


public class SeguitiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView userName;
    private OnRecyclerViewSeguitiClickListener onRecyclerViewSeguitiClickListener;
    private SeguitiViewModel model;
    private ImageView imageView;
    private Context context;

    public SeguitiViewHolder(@NonNull View itemView, OnRecyclerViewSeguitiClickListener onRecyclerViewSeguitiClickListener, SeguitiViewModel model, Context context) {
        super(itemView);
        userName = itemView.findViewById(R.id.nomeSeguito);
        itemView.findViewById(R.id.imageViewSingleRowSeguiti).setOnClickListener(this::onClick);
        this.onRecyclerViewSeguitiClickListener = onRecyclerViewSeguitiClickListener;
        this.model = model;
        this.imageView = itemView.findViewById(R.id.imageViewSingleRowSeguiti);
        this.context = context;
    }

    public void updateContent(Profile profile){

        userName.setText(profile.getName());
        PictureRepository.getUserPicture(context, SidRepository.getSid().getSid(), profile.getName(), profile.getUid(), 0, image -> {
            if(image != null){
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(decodedImage);
                    }
                });
            } else {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.placeholder_girl);
                    }
                });
            }
        }, t ->{
            t.printStackTrace();
        });
    }

    @Override
    public void onClick(View v) {
        Profile profile = model.getProfileInList(getAdapterPosition());
        onRecyclerViewSeguitiClickListener.onRecyclerViewClick(v, getAdapterPosition(), profile);
    }
}
