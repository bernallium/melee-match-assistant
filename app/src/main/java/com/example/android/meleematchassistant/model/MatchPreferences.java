package com.example.android.meleematchassistant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MatchPreferences {

    private static final String NUM_OF_WINS_NEEDED_KEY = "NUM_OF_WINS_NEEDED";
    private static final int DEFAULT_NUM_OF_WINS = 3;

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public MatchPreferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public int getNumOfWinsNeeded() {
        return sharedPreferences.getInt(NUM_OF_WINS_NEEDED_KEY, DEFAULT_NUM_OF_WINS);
    }

    public int getBestOfNum() { // y = 2x - 1, where 'x' is the number of wins needed
        int bestOfNum = 2 * sharedPreferences.getInt(NUM_OF_WINS_NEEDED_KEY, DEFAULT_NUM_OF_WINS) - 1;
        return bestOfNum;
    }

    public void setNumOfWinsNeeded(int numOfWinsNeeded) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUM_OF_WINS_NEEDED_KEY, numOfWinsNeeded);
        editor.commit();
    }
}
