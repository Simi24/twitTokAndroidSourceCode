package com.example.twittokandroid.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity
    public class Profile {
        @PrimaryKey
        public int uid;

        public String name;
        public String picture;
        public int pVersion;


    }

