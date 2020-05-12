package com.example.android.meleematchassistant;

public interface PlayersDatabase {

    String RPS_WINNER_ID_KEY = "RPS_WINNER_ID_KEY";
    String RPS_LOSER_ID_KEY = "RPS_LOSER_ID_KEY";
    String CHOSEN_CHOICE_ID = "CHOSEN_CHOICE_ID";
    String UNCHOSEN_CHOICE_ID = "UNCHOSEN_CHOICE_ID";

    int getRPSWinnerId();
    void setRPSWinnerId(int rPSWinnerId);
    int getRPSLoserId();
    void setRPSLoserId(int rPSLoserId);
    int getChosenChoiceId();
    void setChosenChoiceId(int chosenChoiceId);
    int getUnchosenChoiceId();
    void setUnchosenChoiceId(int unchosenChoiceId);

    String getPlayerStringFromId(int playerId);
    String getFirstStrikePlayerString();
    String getPortSelectionPlayerString();
}
