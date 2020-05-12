package com.example.android.meleematchassistant.model;

public class Players {

    // Constants
    public static final int PLAYER_A_ID = 0;
    public static final int PLAYER_B_ID = 1;
    public static final int FIRST_STRIKE_OPTION_ID = 0;
    public static final int PORT_SELECTION_OPTION_ID = 1;
    public static final String PLAYER_A_STRING = "Player A";  // TODO Remove hardcoded String
    public static final String PLAYER_B_STRING = "Player B"; // TODO Remove hardcoded String

    private int rPSWinnerId;
    private int rPSLoserId;
    private int chosenChoiceId;  // 0 = FIRST STRIKE selected, 1 = PORT SELECTION selected
    private int unchosenChoiceId; // 0 = FIRST STRIKE selected, 1 = PORT SELECTION selected

    ////////// GETTERS and SETTERS //////////

    public int getRPSWinnerId() {
        return rPSWinnerId;
    }

    public void setRPSWinnerId(int rPSWinnerId) {
        this.rPSWinnerId = rPSWinnerId;
    }

    public int getRPSLoserId() {
        return rPSLoserId;
    }

    public void setRPSLoserId(int rPSLoserId) {
        this.rPSLoserId = rPSLoserId;
    }

    public int getChosenChoiceId() {
        return chosenChoiceId;
    }

    public void setChosenChoiceId(int chosenChoiceId) {
        this.chosenChoiceId = chosenChoiceId;
    }

    public int getUnchosenChoiceId() {
        return unchosenChoiceId;
    }

    public void setUnchosenChoiceId(int unchosenChoiceId) {
        this.unchosenChoiceId = unchosenChoiceId;
    }

    /////////////////////////////////////////
}

