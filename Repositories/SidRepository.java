package com.example.twittokandroid.Repositories;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.DataSources.Sid;
import com.example.twittokandroid.Interface.SidErrorListener;
import com.example.twittokandroid.Interface.SidGetFromServerListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SidRepository {
    private static Sid sid = null;
    private static String SIDSHEREDPREFERENCES = "MySid";
    private static String SIDTABLE = "SID";
    private static SharedPreferences prefs;


    public static void initSid(Context context , SidGetFromServerListener sidGetFromServerListener, SidErrorListener sidErrorListener){
        //Log.d("TESTSID", "funzione chiamata");
         //check if sid is in shared preferences
        prefs = context.getSharedPreferences(SIDSHEREDPREFERENCES, MODE_PRIVATE);
        if (!prefs.contains(SIDTABLE)){
            //Log.d("TESTSID", "entriamo nel primo if");

            Call<Sid> sidCall = RetrofitInstance.getRetrofitInstance().getSid();
            sidCall.enqueue(new Callback<Sid>() {
                @Override
                public void onResponse(Call<Sid> call, Response<Sid> response) {
                    assert response.body() != null;
                    sid = new Sid();
                    sid.setSid(response.body().getSid());
                    Log.d("TAGSID", sid+"nella callback");
                    prefs.edit().putString(SIDTABLE, response.body().getSid()).commit();
                    sidGetFromServerListener.onSidInizialise(sid.getSid());
                }

                @Override
                public void onFailure(Call<Sid> call, Throwable t) {
                    sidErrorListener.onSidErrorListener(t);
                    t.printStackTrace();
                }
            });
        } else {
            sidGetFromServerListener.onSidInizialise(prefs.getString(SIDTABLE, ""));
            sid = new Sid();
            sid.setSid(prefs.getString(SIDTABLE, ""));
            Log.d("TAGSID", sid.getSid()+"nel'else");
        }
    }

    public static Sid getSid(){
        Log.d("TAGSID", sid+"in get sid");
        return sid;
    }
}
