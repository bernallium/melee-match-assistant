package com.example.android.meleematchassistant;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

// A local schema
// An entity is essentially a database row
// A database table will be created for each entity specified in your database class to hold each entity

@Entity(tableName = "games_table")
public class Game {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int gameNumber;

    private String stagePlayedOn;

    private int stagePlayedOnIndex;

    private int playerAScore;

    private int playerBScore;

    private String winnerOfGame;

    private String loserOfGame;

    public Game(int gameNumber, String stagePlayedOn, int stagePlayedOnIndex, int playerAScore, int playerBScore, String winnerOfGame, String loserOfGame) {
        this.gameNumber = gameNumber;
        this.stagePlayedOn = stagePlayedOn;
        this.stagePlayedOnIndex = stagePlayedOnIndex;
        this.playerAScore = playerAScore;
        this.playerBScore = playerBScore;
        this.winnerOfGame = winnerOfGame;
        this.loserOfGame = loserOfGame;
    }

    ////////// GETTERS and SETTERS //////////

    @NonNull
    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(@NonNull int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getStagePlayedOn() {
        return stagePlayedOn;
    }

    public void setStagePlayedOn(String stagePlayedOn) {
        this.stagePlayedOn = stagePlayedOn;
    }

    public int getStagePlayedOnIndex() {
        return stagePlayedOnIndex;
    }

    public void setStagePlayedOnIndex(int stagePlayedOnIndex) {
        this.stagePlayedOnIndex = stagePlayedOnIndex;
    }

    public int getPlayerAScore() {
        return playerAScore;
    }

    public void setPlayerAScore(int playerAScore) {
        this.playerAScore = playerAScore;
    }

    public int getPlayerBScore() {
        return playerBScore;
    }

    public void setPlayerBScore(int playerBScore) {
        this.playerBScore = playerBScore;
    }

    public String getWinnerOfGame() {
        return winnerOfGame;
    }

    public void setWinnerOfGame(String winnerOfGame) {
        this.winnerOfGame = winnerOfGame;
    }

    public String getLoserOfGame() {
        return loserOfGame;
    }

    public void setLoserOfGame(String loserOfGame) {
        this.loserOfGame = loserOfGame;
    }
}
