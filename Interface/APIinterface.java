package com.example.twittokandroid.Interface;

import com.example.twittokandroid.DataSources.AddTwok;
import com.example.twittokandroid.DataSources.Followed;
import com.example.twittokandroid.DataSources.FollowedList;
import com.example.twittokandroid.DataSources.GetTwok;
import com.example.twittokandroid.DataSources.Sid;
import com.example.twittokandroid.DataSources.SidUid;
import com.example.twittokandroid.Repositories.Picture;
import com.example.twittokandroid.Repositories.Profile;
import com.example.twittokandroid.Repositories.Twok;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIinterface {

    @POST("register")
    Call<Sid> getSid();

    @POST("getProfile")
    Call<Profile> getProfile(@Body SidUid sidUid);

    @POST("getPicture")
    Call<Picture> getPicture(@Body SidUid sidUid);

    @POST("getTwok")
    Call<Twok> getTwok(@Body GetTwok getTwok);

    @POST("setProfile")
    Call<Void> setProfile(@Body Profile profile);

    @POST("addTwok")
    Call<Void> addTwok(@Body AddTwok twok);

    @POST("follow")
    Call<Void> follow(@Body SidUid sidUid);

    @POST("unFollow")
    Call<Void> unFollow(@Body SidUid sidUid);

    @POST("getFollowed")
    Call<List<Profile>> getFollowed(@Body Sid sid);

    @POST("isFollowed")
    Call<Followed> isFollowed(@Body SidUid sidUid);
}
