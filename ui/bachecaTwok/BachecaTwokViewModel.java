package com.example.twittokandroid.ui.bachecaTwok;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BachecaTwokViewModel extends ViewModel {

    private  MutableLiveData<String> mText;


    public LiveData<String> getText() {
        if(mText == null){
            mText = new MutableLiveData<>();
            mText.setValue("This is home fragment");
            return mText;
        }

        return mText;
    }
}