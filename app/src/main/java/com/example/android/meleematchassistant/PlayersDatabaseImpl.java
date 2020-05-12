package com.example.android.meleematchassistant;

import com.example.android.meleematchassistant.model.Players;

public class PlayersDatabaseImpl implements PlayersDatabase {

    private static Players players = new Players();

    ///// METHODS /////

    @Override
    public int getRPSWinnerId() {
        return players.getRPSWinnerId();
    }

    @Override
    public void setRPSWinnerId(int rPSWinnerId) {
        players.setRPSWinnerId(rPSWinnerId);
    }

    @Override
    public int getRPSLoserId() {
        return players.getRPSLoserId();
    }

    @Override
    public void setRPSLoserId(int rPSLoserId) {
        players.setRPSLoserId(rPSLoserId);
    }

    @Override
    public int getChosenChoiceId() {
        return players.getChosenChoiceId();
    }

    @Override
    public void setChosenChoiceId(int chosenChoiceId) {
        players.setChosenChoiceId(chosenChoiceId);
    }

    @Override
    public int getUnchosenChoiceId() {
        return players.getUnchosenChoiceId();
    }

    @Override
    public void setUnchosenChoiceId(int unchosenChoiceId) {
        players.setUnchosenChoiceId(unchosenChoiceId);
    }

    @Override
    public String getPlayerStringFromId(int playerId) {
        String playerNameFromId = null;
        switch (playerId) {
            case Players.PLAYER_A_ID:
                playerNameFromId = Players.PLAYER_A_STRING;
                break;
            case Players.PLAYER_B_ID:
                playerNameFromId = Players.PLAYER_B_STRING;
        }
        return playerNameFromId;
    }

    @Override
    public String getFirstStrikePlayerString() {
        String firstStrikePlayerString = null;
        if ((getRPSWinnerId() == Players.PLAYER_A_ID && getChosenChoiceId() == Players.FIRST_STRIKE_OPTION_ID) || (getRPSLoserId() == Players.PLAYER_A_ID && getChosenChoiceId() == Players.PORT_SELECTION_OPTION_ID)) { // 00 and 01
            firstStrikePlayerString = Players.PLAYER_A_STRING;
        } else if (getRPSWinnerId() == Players.PLAYER_B_ID && getChosenChoiceId() == Players.FIRST_STRIKE_OPTION_ID || (getRPSLoserId() == Players.PLAYER_B_ID && getChosenChoiceId() == Players.PORT_SELECTION_OPTION_ID)) { // 10 and 11
            firstStrikePlayerString = Players.PLAYER_B_STRING;
        }
        return firstStrikePlayerString;
    }

    @Override
    public String getPortSelectionPlayerString() {
        String portSelectionPlayerString = null;
        if (getRPSWinnerId() == Players.PLAYER_A_ID && getChosenChoiceId() == Players.PORT_SELECTION_OPTION_ID || (getRPSLoserId() == Players.PLAYER_A_ID && getChosenChoiceId() == Players.FIRST_STRIKE_OPTION_ID)) { // 01 and 00
            portSelectionPlayerString = Players.PLAYER_A_STRING;
        } else if (getRPSWinnerId() == Players.PLAYER_B_ID && getChosenChoiceId() == Players.PORT_SELECTION_OPTION_ID || (getRPSLoserId() == Players.PLAYER_B_ID && getChosenChoiceId() == Players.FIRST_STRIKE_OPTION_ID)) { // 11 and 01
            portSelectionPlayerString = Players.PLAYER_B_STRING;
        }
        return portSelectionPlayerString;
    }
}
