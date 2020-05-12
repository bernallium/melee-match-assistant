package com.example.android.meleematchassistant.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class RockPaperScissorsViewModel extends ViewModel {

    private MutableLiveData<Boolean> playerARPSWinnerChecked;
    private MutableLiveData<Boolean> playerBRPSWinnerChecked;

    public MutableLiveData<Boolean> wasPlayerARPSWinnerChecked() {
        if (playerARPSWinnerChecked == null) playerARPSWinnerChecked = new MutableLiveData<>();
        return playerARPSWinnerChecked;
    }

    public MutableLiveData<Boolean> wasPlayerBRPSWinnerChecked() {
        if (playerBRPSWinnerChecked == null) playerBRPSWinnerChecked = new MutableLiveData<>();
        return playerBRPSWinnerChecked;
    }
}
