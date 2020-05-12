package com.example.android.meleematchassistant.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class StageSelectionViewModel extends ViewModel {

    private String actionBarString;
    private boolean playerHasBanned;
    private String chosenStageName;
    private int chosenStageIndex;
    private MutableLiveData<Boolean> startButtonEnabled;

    public String getActionBarString() {
        return actionBarString;
    }

    public void setActionBarString(String actionBarString) {
        this.actionBarString = actionBarString;
    }

    public boolean hasPlayerBanned() {
        return playerHasBanned;
    }

    public void setPlayerHasBanned(boolean playerHasBanned) {
        this.playerHasBanned = playerHasBanned;
    }

    public String getChosenStageName() {
        return chosenStageName;
    }

    public void setChosenStageName(String chosenStageName) {
        this.chosenStageName = chosenStageName;
    }

    public int getChosenStageIndex() {
        return chosenStageIndex;
    }

    public void setChosenStageIndex(int chosenStageIndex) {
        this.chosenStageIndex = chosenStageIndex;
    }

    public MutableLiveData<Boolean> isStartButtonEnabled() {
        if (startButtonEnabled == null) startButtonEnabled = new MutableLiveData<>();
        return startButtonEnabled;
    }
}
