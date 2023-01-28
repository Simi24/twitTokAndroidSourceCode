package com.example.twittokandroid.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Profile.class}, version=1)
public abstract class ProfileRepository extends RoomDatabase {
    private static ProfileRepository profileRepositoryInstance;

    public abstract UserDao userDao();

    public static synchronized ProfileRepository getInstance(Context context){
        if(profileRepositoryInstance == null){
            profileRepositoryInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileRepository.class,
                    "users")
                    .build();
        }
        return profileRepositoryInstance;
    }
}
