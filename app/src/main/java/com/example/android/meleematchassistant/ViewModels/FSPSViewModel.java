package com.example.android.meleematchassistant.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class FSPSViewModel extends ViewModel {

    private MutableLiveData<Boolean> firstStrikeChecked;
    private MutableLiveData<Boolean> portSelectionChecked;
    private boolean canClearCheckedRadioGroup;

    public MutableLiveData<Boolean> wasFirstStrikeChecked() {
        if (firstStrikeChecked == null) firstStrikeChecked = new MutableLiveData<>();
        return firstStrikeChecked;
    }

    public MutableLiveData<Boolean> wasPortSelectionChecked() {
        if (portSelectionChecked == null) portSelectionChecked = new MutableLiveData<>();
        return portSelectionChecked;
    }

    public boolean isCanClearCheckedRadioGroup() {
        return canClearCheckedRadioGroup;
    }

    public void setCanClearCheckedRadioGroup(boolean canClearCheckedRadioGroup) {
        this.canClearCheckedRadioGroup = canClearCheckedRadioGroup;
    }
}

