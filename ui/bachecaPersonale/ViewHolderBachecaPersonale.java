package com.example.twittokandroid.ui.bachecaPersonale;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.Twok;

import java.util.ArrayList;

public class ViewHolderBachecaPersonale extends RecyclerView.ViewHolder {

    View viewRow;
    private ImageButton imageButton;

    private final ArrayList<Integer> textSize = new ArrayList<Integer>();
    private final ArrayList<Typeface> textStyle = new ArrayList<>();
    private final ArrayList<Integer> textVerticalAlign = new ArrayList<>();
    private final ArrayList<Integer> textHorizontalAlign = new ArrayList<>();

    private TextView twokText;

    public ViewHolderBachecaPersonale(@NonNull View itemView) {
        super(itemView);

        viewRow = itemView;
        imageButton = itemView.findViewById(R.id.imageButtonPosition);

        twokText = itemView.findViewById(R.id.textViewSingleRowUtente);

        textSize.add(15);
        textSize.add(30);
        textSize.add(50);

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
        twokText.setText(twok.getText());
        setTwokColor(twok.getFontcol());
        setFontSize(twok.getFontsize());
        setBgCol(twok.getBgcol());
        setFontStyle(twok.getFonttype());
        setTextPosition(twok.getHalign(), twok.getValign());
        setImageButton(twok);
    }

    private void setImageButton(Twok twok){
        Double lat = twok.getLat();
        Double lon = twok.getLon();
        Log.d("POSITION", twok.getLat() + " " + twok.getLon() + " latitudine e longitudine del twok " + twok.getName());
        if(lat != null && lon != null){
            Bundle bundle = new Bundle();
            bundle.putDouble("lat", lat);
            bundle.putDouble("lon", lon);
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener( l-> {
                Log.d("POSITION", "bottone premuto " + lat + " " + lon);
                Navigation.findNavController(itemView).navigate(R.id.action_bachecaPersonale_to_mapsFragment, bundle);
            });
        } else {
            imageButton.setVisibility(View.GONE);
        }

    }


    private void setTwokColor(String textColorTwok){
        try{
            String textColor = "#"+textColorTwok;
            Log.d("COLOR", textColor);
            twokText.setTextColor(Color.parseColor(textColor));
        }catch (Exception e){
            twokText.setTextColor(Color.parseColor("#000000"));
            e.printStackTrace();
        }

    }

    private void setBgCol(String bgcolTwok){
        try{
            String bgcol = "#"+bgcolTwok;
            viewRow.setBackgroundColor(Color.parseColor(bgcol));
        }catch (Exception e){
            viewRow.setBackgroundColor(Color.parseColor("#ffffff"));
            e.printStackTrace();
        }
    }

    private void setFontSize(Integer fontSizeTwok){
        try {
            Integer realFontSize = textSize.get(fontSizeTwok);
            twokText.setTextSize(realFontSize);
        } catch (Exception e){
            twokText.setTextSize(30);
            e.printStackTrace();
        }
    }

    private void setFontStyle(Integer fontStyleTwok){
        try{
            Typeface realFontStyle = textStyle.get(fontStyleTwok);
            twokText.setTypeface(realFontStyle);
        }catch (Exception e){
            twokText.setTypeface(Typeface.DEFAULT);
            e.printStackTrace();
        }
    }

    private void setTextPosition(Integer halignTwok, Integer valignTwok){
        Log.d("POSIZIONE", valignTwok + " " + valignTwok + "");
        try{
            twokText.setGravity(textHorizontalAlign.get(halignTwok) + textVerticalAlign.get(valignTwok));
        }catch (Exception e){
            twokText.setGravity(Gravity.CENTER_VERTICAL + Gravity.CENTER_HORIZONTAL);
        }

    }
}
