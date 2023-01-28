package com.example.twittokandroid.ui.bachecaPersonale;

import android.app.Application;
import android.util.Log;
import android.widget.ImageButton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.twittokandroid.DataSources.GetTwok;
import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.Repositories.Twok;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BachecaPersonaleViewModel extends ViewModel {

    private MutableLiveData<List<Twok>> twoksMutableLiveData;
    private ArrayList<Twok> twoksFake;
    private MutableLiveData<Boolean> connectionMutableLiveData;
    private static String sid;
    private static Integer uid;

    public BachecaPersonaleViewModel(Integer uid){
        this.uid = uid;
        Log.d("BACHECAPERSONALEVIEWMODEL", this.uid+"");
        getTwoksMutableLiveData();
    }


    public MutableLiveData<List<Twok>> getTwoksMutableLiveData() {
        if(twoksMutableLiveData == null){
            twoksMutableLiveData = new MutableLiveData<>();
            init();
            return twoksMutableLiveData;
        }
        return twoksMutableLiveData;
    }

    public MutableLiveData<Boolean> getConnectionMutableLiveData(){
        if(connectionMutableLiveData == null){
            connectionMutableLiveData = new MutableLiveData<>();
            connectionMutableLiveData.setValue(true);
            return connectionMutableLiveData;
        }
        return connectionMutableLiveData;
    }

    public void setSid(String sidFromSharedPreferences){
        sid = sidFromSharedPreferences;
    }
    public String getSid(){return sid;};

    public void init(){

        twoksFake = new ArrayList<>();


        GetTwok getTwok = new GetTwok();
        getTwok.setSid(SidRepository.getSid().getSid());
        getTwok.setUid(uid);
        Log.d("TAGSID", this.getSid()+"in twok repository");
        Log.d("TAGSID", SidRepository.getSid()+"in twok repository");
        for (int i = 0; i < 20; i++) {
            Call<Twok> twokCall = RetrofitInstance.getRetrofitInstance().getTwok(getTwok);
            twokCall.enqueue(new Callback<Twok>() {
                @Override
                public void onResponse(Call<Twok> call, Response<Twok> response) {
                    twoksFake.add(response.body());
                    twoksMutableLiveData.setValue(twoksFake);
                    Log.d("INITBACHECAPERSONALE", String.valueOf(twoksMutableLiveData.getValue().size()));
                    Log.d("INITBACHECAPERSONALE", response.body().getName() + response.body().getUid());
                }

                @Override
                public void onFailure(Call<Twok> call, Throwable t) {
                    if(connectionMutableLiveData.getValue()){
                        connectionMutableLiveData.setValue(false);
                    }
                    t.printStackTrace();
                }
            });
        }
    }


    public Twok getTwokInList(int index){
        if(this.getTwoksMutableLiveData().getValue() != null){
            Twok twok = getTwoksMutableLiveData().getValue().get(index);
            return twok;
        }
        return null;
    }

    public int getSize(){
        if(this.getTwoksMutableLiveData().getValue() != null){

            return twoksMutableLiveData.getValue().size();
        }
        return 0;
    }
}
