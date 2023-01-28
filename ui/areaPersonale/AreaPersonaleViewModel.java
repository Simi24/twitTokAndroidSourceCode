package com.example.twittokandroid.ui.areaPersonale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.DataSources.SidUid;
import com.example.twittokandroid.Repositories.Profile;
import com.example.twittokandroid.Repositories.SidRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AreaPersonaleViewModel extends ViewModel {

    private static MutableLiveData<String> userName;
    private static MutableLiveData<String> profilePicture;
    private MutableLiveData<Boolean> connectionMutableLiveData;
    private Context context;

    public AreaPersonaleViewModel(Context context) {
        getNameMutableLiveData();
        this.context = context;
    }

    public MutableLiveData<String> getProfilePictureMutableLiveData() {
        if(profilePicture == null){
            profilePicture = new MutableLiveData<>();
            getUserPicture();
        }
        return profilePicture;
    }

    public MutableLiveData<String> getNameMutableLiveData() {
        if (userName == null){
            userName = new MutableLiveData<>();
            getUserName();
        }
        return userName;

    }

    public MutableLiveData<Boolean> getConnectionMutableLiveData(){
        if(connectionMutableLiveData == null){
            connectionMutableLiveData = new MutableLiveData<>();
            connectionMutableLiveData.setValue(true);
            return connectionMutableLiveData;
        }
        return connectionMutableLiveData;
    }

    private void getUserName() {
        SidUid sidUid = new SidUid();
        sidUid.setSid(SidRepository.getSid().getSid());
        Call<Profile> profileCall = RetrofitInstance.getRetrofitInstance().getProfile(sidUid);
        profileCall.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.d("PROFILE", response.body().getName() + "il mio nome utente");
                userName.setValue(response.body().getName());

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                if(connectionMutableLiveData.getValue()){
                    connectionMutableLiveData.setValue(false);
                }
                t.printStackTrace();
            }
        });

    }

    private void getUserPicture(){
        SidUid sidUid = new SidUid();
        sidUid.setSid(SidRepository.getSid().getSid());
        Call<Profile> profileCall = RetrofitInstance.getRetrofitInstance().getProfile(sidUid);
        profileCall.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                //Log.d("PROFILE", response.body().getPicture().length() + " l'immagine del profilo");
                if(response.body().getPicture() != null){
                    byte[] decodedString = Base64.decode(response.body().getPicture(), Base64.DEFAULT);
                    profilePicture.setValue(response.body().getPicture());
                } else {
                    profilePicture.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                if(connectionMutableLiveData.getValue()){
                    connectionMutableLiveData.setValue(false);
                }
                t.printStackTrace();
            }
        });
    }


    public static void setUserName(String newUserName){
        userName.setValue(newUserName);
    }

    public static void setProfilePicture(String newProfilePicture){
        profilePicture.setValue(newProfilePicture);
    }
}