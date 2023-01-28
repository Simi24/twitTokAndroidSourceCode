package com.example.twittokandroid.ui.bachecaTwok;

import android.util.Log;

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

public class TwokRepository extends ViewModel {
    private MutableLiveData<List<Twok>> twoksMutableLiveData;
    private ArrayList<Twok> twoksFake;
    private static String sid;
    private MutableLiveData<Boolean> connectionMutableLiveData;

    private Integer tidSequence = -1;

    public TwokRepository(){
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
        Log.d("TAGSID", this.getSid()+"in twok repository");
        Log.d("TAGSID", SidRepository.getSid()+"in twok repository");



        for (int i = 0; i < 6; i++) {
            if(tidSequence >= 0){
                getTwok.setTid(tidSequence);
            }
            Call<Twok> twokCall = RetrofitInstance.getRetrofitInstance().getTwok(getTwok);
            twokCall.enqueue(new Callback<Twok>() {
                @Override
                public void onResponse(Call<Twok> call, Response<Twok> response) {
                    twoksFake.add(response.body());
                    twoksMutableLiveData.setValue(twoksFake);
                    Log.d("INIT", String.valueOf(twoksMutableLiveData.getValue().size()));
                    Log.d("INIT", String.valueOf(response.body()));
                    Log.d("INIT", sid+"sid in init");
                    if(tidSequence >= 0){
                        tidSequence++;
                    }
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

    public void addTwok(){
        if(twoksMutableLiveData.getValue() != null){
            Log.d("ADD", "entro qui");
            GetTwok getTwok = new GetTwok();
            getTwok.setSid(sid);
            if(tidSequence >= 0){
                getTwok.setTid(tidSequence);
            }
            Call<Twok> twokCall = RetrofitInstance.getRetrofitInstance().getTwok(getTwok);
            twokCall.enqueue(new Callback<Twok>() {
                @Override
                public void onResponse(Call<Twok> call, Response<Twok> response) {
                    List<Twok> twokList = new ArrayList<>(twoksMutableLiveData.getValue());
                    twokList.add(response.body());
                    twoksMutableLiveData.setValue(twokList);
                    Log.d("ADDTWOK", String.valueOf(twoksMutableLiveData.getValue().size()) + " " + tidSequence);
                    if(tidSequence >= 0){
                        tidSequence++;
                    }

                }

                @Override
                public void onFailure(Call<Twok> call, Throwable t) {
                    t.printStackTrace();
                    if(connectionMutableLiveData.getValue()){
                        connectionMutableLiveData.setValue(false);
                    }
                }
            });

        }
    }
}
