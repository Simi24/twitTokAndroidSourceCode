package com.example.twittokandroid.ui.seguiti;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.Repositories.Profile;
import com.example.twittokandroid.Repositories.SidRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO: il sid è null
public class SeguitiViewModel extends ViewModel {
    private AdapterSeguiti adapterSeguiti;
    private static MutableLiveData<List<Profile>> seguitiMutableLiveData;
    private static MutableLiveData<Boolean> connectionMutableLiveData;

    public SeguitiViewModel() {
        getSeguitiMutableLiveData();
    }

    public static MutableLiveData<List<Profile>> getSeguitiMutableLiveData() {
        if(seguitiMutableLiveData == null){
            seguitiMutableLiveData = new MutableLiveData<>();
            init();
            return seguitiMutableLiveData;
        }
        return seguitiMutableLiveData;
    }

    public MutableLiveData<Boolean> getConnectionMutableLiveData(){
        if(connectionMutableLiveData == null){
            connectionMutableLiveData = new MutableLiveData<>();
            connectionMutableLiveData.setValue(true);
            return connectionMutableLiveData;
        }
        return connectionMutableLiveData;
    }

    public static void init(){
        Log.d("SEGUITI", SidRepository.getSid() + "");
        Call<List<Profile>> followedListCall = RetrofitInstance.getRetrofitInstance().getFollowed(SidRepository.getSid());
        followedListCall.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                //Log.d("SEGUITI", response.body().size() + " viene chiamato il metodo init");
                seguitiMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                /*if(connectionMutableLiveData.getValue()){
                    connectionMutableLiveData.setValue(false);
                }*/
                t.printStackTrace();

            }
        });
    }

    public static void setSeguitiMutableLiveData(){
        Call<List<Profile>> followedListCall = RetrofitInstance.getRetrofitInstance().getFollowed(SidRepository.getSid());
        followedListCall.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                List<Profile> updatedFollowedUser = response.body();

                seguitiMutableLiveData.setValue(updatedFollowedUser);
                Log.d("SEGUITI", seguitiMutableLiveData.getValue().size() + " aggiorniamo la lista dei seguiti");
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                if(connectionMutableLiveData.getValue()){
                    connectionMutableLiveData.setValue(false);
                }
                t.printStackTrace();
            }
        });
    }

    public Profile getProfileInList(int index){
        if(this.getSeguitiMutableLiveData().getValue() != null){
            return getSeguitiMutableLiveData().getValue().get(index);
        }
        return null;
    }

    public int getSize(){
        if(this.getSeguitiMutableLiveData().getValue() != null){
            return getSeguitiMutableLiveData().getValue().size();
        }
        return 0;
    }

}
