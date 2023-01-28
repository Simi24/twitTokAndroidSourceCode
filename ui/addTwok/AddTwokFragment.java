package com.example.twittokandroid.ui.addTwok;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.twittokandroid.DataSources.AddTwok;
import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.databinding.FragmentDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTwokFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private TextView twokPreview;
    private AddTwok twokToAdd;
    private final ArrayList<Integer> textVerticalAlign = new ArrayList<>();
    private final ArrayList<Integer> textHorizontalAlign = new ArrayList<>();
    private final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddTwokViewModel dashboardViewModel =
                new ViewModelProvider(this).get(AddTwokViewModel.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        textVerticalAlign.add(Gravity.TOP);
        textVerticalAlign.add(Gravity.CENTER_VERTICAL);
        textVerticalAlign.add(Gravity.BOTTOM);

        textHorizontalAlign.add(Gravity.START);
        textHorizontalAlign.add(Gravity.CENTER_HORIZONTAL);
        textHorizontalAlign.add(Gravity.END);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        twokPreview = binding.twokPreviewTextView;
        resetTwokPreview();
        twokToAdd = new AddTwok();
        twokToAdd.setSid(SidRepository.getSid().getSid());

        //MI SERVE PER TOGLIERE DALLO SCHERMO LA PREVIEW QUANDO DIGITO IL TESTO
        binding.layoutAddTwok.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom < oldBottom){
                    twokPreview.setVisibility(View.GONE);
                } else {
                    twokPreview.setVisibility(View.VISIBLE);
                }
            }
        });




        binding.textInputEditTwok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                twokPreview.setText("Vuoto");
                twokToAdd.setText("Vuoto");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                twokPreview.setText(s);
                twokToAdd.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setSfondoSpinner();
        setColoreTestoSpinner();
        setDimensioneTestoSpinner();
        setPosizioneOrizzontaleSpinner();
        setPosizioneVerticaleSpinner();
        setFontSpinner();
        setPosizioneSwitch();

        binding.addTwokButton.setOnClickListener( l ->{
            binding.addTwokButton.setEnabled(false);
            Log.d("SPINNER", twokToAdd.toString());
            if(!(twokToAdd.getText().length() > 100)){
                Call<Void> addTwokCall = RetrofitInstance.getRetrofitInstance().addTwok(twokToAdd);
                addTwokCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("SPINNER", "twok aggiunto correttamente");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Twok aggiunto correttamente!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                                setSfondoSpinner();
                                setColoreTestoSpinner();
                                setDimensioneTestoSpinner();
                                setPosizioneOrizzontaleSpinner();
                                setPosizioneVerticaleSpinner();
                                setFontSpinner();
                                binding.switchPosizione.setChecked(false);
                                binding.textInputEditTwok.setText("Vuoto");
                                resetTwokPreview();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        binding.addTwokButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Abbiamo dei problemi di connessione, riprova!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                                setSfondoSpinner();
                                setColoreTestoSpinner();
                                setDimensioneTestoSpinner();
                                setPosizioneOrizzontaleSpinner();
                                setPosizioneVerticaleSpinner();
                                setFontSpinner();
                                binding.switchPosizione.setChecked(false);
                                binding.textInputEditTwok.setText("vuoto");
                                resetTwokPreview();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        binding.addTwokButton.setEnabled(true);
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Il testo del twok dev'essere più corto di 100 caratteri!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                binding.addTwokButton.setEnabled(true);
            }


        });




        return root;
    }

    private void setSfondoSpinner(){
        twokToAdd.setBgcol("FFFFFF");
        List<Colore> colori = Arrays.asList(
                new Colore("Bianco", "FFFFFF"),
                new Colore("Rosso", "FF0000"),
                new Colore("Verde", "00FF00"),
                new Colore("Blu", "0000FF"),
                new Colore("Nero", "000000")
        );

        final String[] colorNames = new String[colori.size()];
        for(int i = 0; i<colori.size(); i++){
            colorNames[i] = colori.get(i).getNome();
        }



        Button button = binding.buttonSfondo;
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleziona il colore di sfondo");
            builder.setItems(colorNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        String textColor = "#"+colori.get(which).getCodiceEsadecimale();
                        twokPreview.setBackgroundColor(Color.parseColor(textColor));
                        twokToAdd.setBgcol(colori.get(which).getCodiceEsadecimale());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        });
        /*ColorArrayAdapter adapter = new ColorArrayAdapter(getContext(), colori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    String textColor = "#"+colori.get(position).getCodiceEsadecimale();
                    twokPreview.setBackgroundColor(Color.parseColor(textColor));
                    twokToAdd.setBgcol(colori.get(position).getCodiceEsadecimale());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private void setColoreTestoSpinner(){
        twokToAdd.setFontcol("000000");
        List<Colore> colori = Arrays.asList(
                new Colore("Nero", "000000"),
                new Colore("Rosso", "FF0000"),
                new Colore("Verde", "00FF00"),
                new Colore("Blu", "0000FF"),
                new Colore("Bianco", "FFFFFF")
        );

        final String[] colorNames = new String[colori.size()];
        for(int i = 0; i<colori.size(); i++){
            colorNames[i] = colori.get(i).getNome();
        }

        Button button = binding.buttonColoreTesto;
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleziona il colore del testo");
            builder.setItems(colorNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        String textColor = "#"+colori.get(which).getCodiceEsadecimale();
                        twokPreview.setTextColor(Color.parseColor(textColor));
                        twokToAdd.setFontcol(colori.get(which).getCodiceEsadecimale());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        });

    }

    private void setDimensioneTestoSpinner(){
        twokToAdd.setFontsize(0);
        final ArrayList<Integer> textSize = new ArrayList<Integer>();
        textSize.add(15);
        textSize.add(30);
        textSize.add(50);

        Button button = binding.buttonDimensione;
        String[] items = new String[]{"piccolo" ,"medio", "grosso"};
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleziona a dimensione del testo");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        twokPreview.setTextSize(textSize.get(which));
                        twokToAdd.setFontsize(which);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        });
    }

    private void setPosizioneOrizzontaleSpinner(){
        twokToAdd.setHalign(0);
        Button button = binding.buttonOrizzontale;
        String[] items = new String[]{"sinistra" ,"centro", "destra"};
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleziona la posizione orizzontale");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        if(twokToAdd.getValign() != null){
                            twokPreview.setGravity(textHorizontalAlign.get(which) + textVerticalAlign.get(twokToAdd.getValign()) );
                        } else {
                            twokPreview.setGravity(textHorizontalAlign.get(which) );
                        }

                        twokToAdd.setHalign(which);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        });

    }

    private void setPosizioneVerticaleSpinner(){
        twokToAdd.setValign(0);
        Button button = binding.buttonVerticale;
        String[] items = new String[]{"alto" ,"centro", "basso"};
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleziona la posizione verticale");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        if(twokToAdd.getHalign() != null){
                            twokPreview.setGravity(textVerticalAlign.get(which) + textHorizontalAlign.get(twokToAdd.getHalign()));

                        } else {
                            twokPreview.setGravity(textVerticalAlign.get(which));
                        }

                        twokToAdd.setValign(which);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        });

    }

    private void setFontSpinner(){
        twokToAdd.setFonttype(0);
        final ArrayList<Typeface> textStyle = new ArrayList<>();
        textStyle.add(Typeface.DEFAULT);
        textStyle.add(Typeface.MONOSPACE);
        textStyle.add(Typeface.SANS_SERIF);

        Button button = binding.buttonFont;
        String[] items = new String[]{"default" ,"monospace", "sans_serif"};

        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleziona il font");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        twokPreview.setTypeface(textStyle.get(which));
                        twokToAdd.setFonttype(which);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        });
    }

    private void setPosizioneSwitch(){
        Switch switchPosizione = binding.switchPosizione;
        switchPosizione.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.addTwokButton.setEnabled(false);
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        Log.d("SPINNER", "dobbiamo richiedere i permessi");
                        //I permessi non sono ancora stati concessi, richiediamoli:
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                        /*ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);*/

                    } else {
                        Log.d("SPINNER", "I permessi sono già stati concessi");
                        //I permessi sono stati concessi, richiediamo la posizione e mettiamo le coordinate nel twok
                        getLastLocation();
                        binding.addTwokButton.setEnabled(true);
                    }

                    Log.d("SPINNER", "bisogna mettere la posizione");
                }else{
                    Log.d("SPINNER", "bisogna togliere la posizione");
                    twokToAdd.setLat(null);
                    twokToAdd.setLon(null);
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("SPINNER", requestCode + " requestCode");
        switch (requestCode){
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permessi concessi, prendiamo la posizione e inseriamola nel twok
                    Log.d("SPINNER", grantResults[0] + "");
                    getLastLocation();
                    binding.addTwokButton.setEnabled(true);
                } else {
                    Log.d("SPINNER", grantResults[0] + "");
                    //la posizione non è stata concessa, alert dicendo che abbiamo bisogno dei permessi per inserire la posizione
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Per inserire la posizione devi concedere i permessi!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                            binding.switchPosizione.setChecked(false);
                            twokToAdd.setLat(null);
                            twokToAdd.setLon(null);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    Log.d("POSITION", "la posizione è stata ottenuta " + location.getLatitude() + location.getLongitude());
                    twokToAdd.setLat(location.getLatitude());
                    twokToAdd.setLon(location.getLongitude());
                    //Log.d("POSITION", twokToAdd.getLat().toString() + twokToAdd.getLon().toString() + " posizione nel twok se non abbiamo concesso i permessi");
                } else {
                    //la posizione non è disponibile, alert in cui lo diciamo
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Non siamo riusciti a prendere la posizione, riprova!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void resetTwokPreview(){
        twokPreview.setBackgroundColor(Color.parseColor("#FFFFFF"));
        twokPreview.setTypeface(Typeface.DEFAULT);
        twokPreview.setGravity(textVerticalAlign.get(0));
        twokPreview.setGravity(textHorizontalAlign.get(0));
        twokPreview.setTextSize(30);
        twokPreview.setTextColor(Color.parseColor("#000000"));

    }
}