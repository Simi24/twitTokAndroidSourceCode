package com.example.twittokandroid.ui.bachecaTwok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twittokandroid.Interface.OnRecyclerViewClickListener;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.PictureRepository;
import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.Repositories.Twok;
import com.example.twittokandroid.ui.bachecaTwok.TwokRepository;

import java.util.ArrayList;


public class ViewHolderTwok extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView nameTextView;
    TextView twokTextView;
    ImageView imageView;
    View viewRow;
    View viewHeder;
    Button positionButton;

    private final Context context;
    private final TwokRepository twokRepository;

    private final ArrayList<Integer> textSize = new ArrayList<Integer>();
    private final ArrayList<Typeface> textStyle = new ArrayList<>();
    private final ArrayList<Integer> textVerticalAlign = new ArrayList<>();
    private final ArrayList<Integer> textHorizontalAlign = new ArrayList<>();
    private final OnRecyclerViewClickListener mRecyclerViewClickListener;


    public ViewHolderTwok(@NonNull View itemView, OnRecyclerViewClickListener onRecyclerViewClickListener, Context context, TwokRepository twokRepository) {
        super(itemView);
        this.context = context;
        this.twokRepository = twokRepository;

        textSize.add(15);
        textSize.add(30);
        textSize.add(50);

        positionButton = itemView.findViewById(R.id.buttonPosizione);

        nameTextView = itemView.findViewById(R.id.name_row);
        twokTextView = itemView.findViewById(R.id.twok_row);
        imageView = itemView.findViewById(R.id.imageView2);
        viewRow = itemView.findViewById(R.id.twok);
        viewHeder = itemView.findViewById(R.id.viewHeader);

        itemView.setOnClickListener(this::onClick);

        mRecyclerViewClickListener = onRecyclerViewClickListener;

        textStyle.add(Typeface.DEFAULT);
        textStyle.add(Typeface.MONOSPACE);
        textStyle.add(Typeface.SANS_SERIF);

        textVerticalAlign.add(Gravity.TOP);
        textVerticalAlign.add(Gravity.CENTER_VERTICAL);
        textVerticalAlign.add(Gravity.BOTTOM);

        textHorizontalAlign.add(Gravity.START);
        textHorizontalAlign.add(Gravity.CENTER_HORIZONTAL);
        textHorizontalAlign.add(Gravity.END);


    }

    public void updateContent(Twok twok){
        try{
            setHeader(twok);
            setTwokText(twok.getText());
            setTwokColor(twok.getFontcol());
            setFontSize(twok.getFontsize());
            setBgCol(twok.getBgcol());
            setFontStyle(twok.getFonttype());
            setTextPosition(twok.getHalign(), twok.getValign());


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setHeader(Twok twok){
        viewHeder.setBackgroundColor(Color.parseColor("#B2BEB5"));
        nameTextView.setText(twok.getName());
        setImage(twok);
        setPositionButton(twok);
    }


    private void setImage(Twok twok){

        Log.d("PROVADB", twok.getName());
        PictureRepository.getUserPicture(context, SidRepository.getSid().getSid(), twok.getName(), twok.getUid(), twok.getPversion(), image -> {
            //Log.d("PROVADB", "il risultato della callback: "+image);
            if(image != null){
                try{
                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Log.d("IMAGEWIDTH", decodedImage.getWidth() + " " + twok.getName());
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(decodedImage);
                        }
                    });
                }catch (Exception e){
                    Log.d("SETPICTURE", "errore durante il decoding dell'immagine");
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageResource(R.drawable.placeholder_girl);
                        }
                    });
                }

            } else {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.placeholder_girl);
                    }
                });
            }

        }, e -> {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(R.drawable.placeholder_girl);
                }
            });
        });

    }

    private void setPositionButton(Twok twok){
        Double lat = twok.getLat();
        Double lon = twok.getLon();
        Log.d("POSITION", twok.getLat() + " " + twok.getLon() + " latitudine e longitudine del twok " + twok.getName());
        if(lat != null && lon != null){
            Bundle bundle = new Bundle();
            bundle.putDouble("lat", lat);
            bundle.putDouble("lon", lon);
            positionButton.setVisibility(View.VISIBLE);
            positionButton.setOnClickListener( l-> {
                Log.d("POSITION", "bottone premuto " + lat + " " + lon);
                Navigation.findNavController(itemView).navigate(R.id.action_navigation_bacheca_twok_to_mapsFragment, bundle);
            });
        } else {
            positionButton.setVisibility(View.GONE);
        }

    }

    private void setTwokText(String twokText){
        twokTextView.setText(twokText);
    }

    private void setTwokColor(String textColorTwok){
        try{
            String textColor = "#"+textColorTwok;
            Log.d("COLOR", textColor);
            twokTextView.setTextColor(Color.parseColor(textColor));
        }catch (Exception e){
            e.printStackTrace();
            twokTextView.setTextColor(Color.parseColor("#000000"));
        }

    }

    private void setBgCol(String bgcolTwok){
        try{
            String bgcol = "#"+bgcolTwok;
            viewRow.setBackgroundColor(Color.parseColor(bgcol));
        }catch (Exception e){
            e.printStackTrace();
            viewRow.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    private void setFontSize(Integer fontSizeTwok){
        try {
            Integer realFontSize = textSize.get(fontSizeTwok);
            twokTextView.setTextSize(realFontSize);
        } catch (Exception e){
            e.printStackTrace();
            twokTextView.setTextSize(30);
        }
    }

    private void setFontStyle(Integer fontStyleTwok){
        try{
            Typeface realFontStyle = textStyle.get(fontStyleTwok);
            twokTextView.setTypeface(realFontStyle);
        }catch (Exception e){
            e.printStackTrace();
            twokTextView.setTypeface(Typeface.DEFAULT);
        }
    }

    private void setTextPosition(Integer halignTwok, Integer valignTwok){
        try{
            twokTextView.setGravity(textHorizontalAlign.get(halignTwok) + textVerticalAlign.get(valignTwok));
        }catch (Exception e){
            twokTextView.setGravity(Gravity.CENTER_VERTICAL + Gravity.CENTER_HORIZONTAL);
        }

    }

    @Override
    public void onClick(View view) {
        Twok twok = twokRepository.getTwokInList(getAdapterPosition());
        Log.d("ONCLICK", twok+"");
        mRecyclerViewClickListener.onRecyclerViewClick(view, getAdapterPosition(), twok);
    }


}
