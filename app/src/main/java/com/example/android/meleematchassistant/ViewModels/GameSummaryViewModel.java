package com.example.android.meleematchassistant.ViewModels;

import android.arch.lifecycle.ViewModel;

public class GameSummaryViewModel extends ViewModel {

    private String actionBarString;
    private boolean actionBarWasSet;

    public String getActionBarString() {
        return actionBarString;
    }

    public void setActionBarString(String actionBarString) {
        this.actionBarString = actionBarString;
    }

    public boolean wasActionBarSet() {
        return actionBarWasSet;
    }

    public void setActionBarWasSet(boolean actionBarWasSet) {
        this.actionBarWasSet = actionBarWasSet;
    }
}
