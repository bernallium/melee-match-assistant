package com.example.android.meleematchassistant.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> charSelectionStepCompleted; // TRUE
    private MutableLiveData<Boolean> rPSStepCompleted; // FALSE
    private MutableLiveData<Boolean> fSPSStepCompleted; // FALSE
    private MutableLiveData<Boolean> stageStrikingStepCompleted; // FALSE

    public MutableLiveData<Boolean> isCharSelectionStepCompleted() {
        if (charSelectionStepCompleted == null) charSelectionStepCompleted = new MutableLiveData<>();
        return charSelectionStepCompleted;
    }

    public MutableLiveData<Boolean> isRPSStepCompleted() {
        if (rPSStepCompleted == null) rPSStepCompleted = new MutableLiveData<>();
        return rPSStepCompleted;
    }

    public MutableLiveData<Boolean> isFSPSStepCompleted() {
        if (fSPSStepCompleted == null) fSPSStepCompleted = new MutableLiveData<>();
        return fSPSStepCompleted;
    }

    public MutableLiveData<Boolean> isStageStrikingStepCompleted() {
        if (stageStrikingStepCompleted == null) stageStrikingStepCompleted = new MutableLiveData<>();
        return stageStrikingStepCompleted;
    }
}

