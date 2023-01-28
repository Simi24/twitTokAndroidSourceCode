package com.example.twittokandroid.Room;


import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

@Dao
public interface UserDao {

    @Upsert
    void insertAll(Profile... profile);

    @Update
    void updateUser(Profile... profiles);

    @Query("SELECT * FROM profile WHERE uid = :uid")
    Profile getUser(int uid);


    @Query("SELECT picture FROM profile WHERE uid = :uid")
    String getUserPicture(int uid);

}
