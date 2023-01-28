package com.example.twittokandroid.ui.modificaProfilo;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.Profile;
import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.databinding.FragmentModificaProfiloBinding;
import com.example.twittokandroid.ui.areaPersonale.AreaPersonaleViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificaProfilo extends Fragment {

    private FragmentModificaProfiloBinding binding;
    private static final int PICK_IMAGE_REQUEST = -1;

    private ActivityResultRegistry activityResultRegistry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentModificaProfiloBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonInserisciNome.setOnClickListener(l ->{
            binding.buttonInserisciNome.setEnabled(false);
            EditText editText = binding.editText;
            String text = editText.getText().toString();
            Log.d("UPDATEPROFILE", text);
            if(text.length() < 20){
                Profile profile = new Profile();
                profile.setSid(SidRepository.getSid().getSid());
                profile.setName(text);
                Call<Void> profileCall = RetrofitInstance.getRetrofitInstance().setProfile(profile);
                profileCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        AreaPersonaleViewModel.setUserName(text);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Nome inserito correttamente!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                                Navigation.findNavController(root).navigate(R.id.action_modificaProfilo_to_navigation_area_personale);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        binding.buttonInserisciNome.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Qualcosa è andato storto, riprova!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        binding.buttonInserisciNome.setEnabled(true);
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Il nome che hai inserito è troppo lungo!");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                binding.buttonInserisciNome.setEnabled(true);
            }

        });

        binding.buttonScegliImmagine.setOnClickListener( l -> {
            binding.buttonScegliImmagine.setEnabled(false);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("UPDATEPROFILE", "entro dopo aver scelto l'immagine "+ (resultCode == RESULT_OK) + data + data.getData());
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();
            Log.d("UPDATEPROFILE", imageUri.toString());
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);

                String imageBase64 = encodeImage(selectedImage);

                if(imageBase64.length() < 137000){
                    Profile profile = new Profile();
                    profile.setSid(SidRepository.getSid().getSid());
                    profile.setPicture(imageBase64);

                    Call<Void> setProfileCall = RetrofitInstance.getRetrofitInstance().setProfile(profile);
                    setProfileCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("UPDATEPROFILE", "aggiornata l'immagine");
                            AreaPersonaleViewModel.setProfilePicture(imageBase64);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Immagine inserita correttamente!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_modificaProfilo_to_navigation_area_personale);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            binding.buttonScegliImmagine.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Qualcosa è andato storto, riprova!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            binding.buttonScegliImmagine.setEnabled(true);
                        }
                    });
                } else {
                    Log.d("UPDATEPROFILE", "l'immagine è troppo lunga! " + imageBase64.length());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("L'immagine che hai inserito è troppo grande!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    binding.buttonScegliImmagine.setEnabled(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
}