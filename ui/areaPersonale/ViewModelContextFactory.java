package com.example.twittokandroid.ui.areaPersonale;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelContextFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;

    public ViewModelContextFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AreaPersonaleViewModel(context);
    }
}
