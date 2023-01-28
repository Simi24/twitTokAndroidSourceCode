package com.example.twittokandroid.DataSources;

import com.example.twittokandroid.Repositories.Profile;

import java.util.ArrayList;
import java.util.List;

public class FollowedList {
    private List<Profile> followed;

    public FollowedList() {
    }

    public List<Profile> getFollowed() {
        return followed;
    }

    public void setFollowed(ArrayList<Profile> followed) {
        this.followed = followed;
    }
}
