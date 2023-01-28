package com.example.twittokandroid.Repositories;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.DataSources.SidUid;
import com.example.twittokandroid.Interface.ErrorUserPictureListener;
import com.example.twittokandroid.Interface.UserPictureListener;
import com.example.twittokandroid.Room.Profile;
import com.example.twittokandroid.Room.ProfileRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureRepository {


    //TODO: questo metodo non funziona, mi dice che delle immagini sono codificate male anche se non è vero
    public static Boolean validPicture(String picture){
        final String regex = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        final Pattern p = Pattern.compile(regex);

        if(picture == null){
            return false;
        }
        Matcher m = p.matcher(picture);

        // Return if the username
        // matched the ReGex
        return m.matches();
    }

    public static void getUserPicture(Context context, String sid, String name, Integer uid, Integer pversion, UserPictureListener userPictureListener, ErrorUserPictureListener errorUserPictureListener){
        ProfileRepository myDB = ProfileRepository.getInstance(context);
        //Controlla se l'immagine se c'è sul DB.
        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile profile = myDB.userDao().getUser(uid);
                //Controlla se non abbiamo l'utente oppure che il nome e la pversion non siano cambiati.
                if(profile == null || profile.pVersion < pversion || !profile.name.equals(name)){
                    Log.d("PICTUREDB", "Non abbiamo l'immagine, dobbiamo scaricarla: " + uid + name);
                    SidUid sidUid = new SidUid();
                    sidUid.setSid(sid);
                    sidUid.setUid(uid);
                    Call<Picture> pictureCall = RetrofitInstance.getRetrofitInstance().getPicture(sidUid);
                    //Scarica il nuovo utente dalla rete
                    pictureCall.enqueue(new Callback<Picture>() {
                        @Override
                        public void onResponse(Call<Picture> call, Response<Picture> response) {
                            com.example.twittokandroid.Room.Profile profile = new com.example.twittokandroid.Room.Profile();

                            if(response.body().getPicture() != null){
                                try{
                                    byte[] decodedBytes = Base64.decode(response.body().getPicture(),Base64.DEFAULT);
                                    profile.picture = response.body().getPicture();
                                    Log.d("VALIDPICTURE", "immagine valida" + response.body().getName());
                                }catch (IllegalArgumentException e){
                                    profile.picture = null;
                                    Log.d("VALIDPICTURE", "immagine non valida " + response.body().getName());
                                }
                            } else {
                                profile.picture = response.body().getPicture();
                            }


                            profile.name = response.body().getName();
                            profile.uid = response.body().getUid();
                            profile.pVersion = response.body().getPversion();

                            //Salva l'immagine nuova sul DB
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    myDB.userDao().insertAll(profile);
                                }
                            }).start();
                            //Chiama la funzione di callback per passare l'immagine quando è pronta
                            userPictureListener.onUserPictureReady(response.body().getPicture());
                        }

                        @Override
                        public void onFailure(Call<Picture> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    //se l'immagine c'è chiama chiama la fenuzione di callback
                    Log.d("PICTUREDB", "abbiamo l'immagine nel db" + uid + name);
                    userPictureListener.onUserPictureReady(profile.picture);
                }
            }
        }).start();


    }


}
