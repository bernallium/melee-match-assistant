package com.example.android.meleematchassistant.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.meleematchassistant.AppRepository;
import com.example.android.meleematchassistant.Game;
import com.example.android.meleematchassistant.model.MatchPreferences;

import java.util.List;
import java.util.concurrent.ExecutionException;

// Use AndroidViewModel in this case since we require the context (in all other cases use ViewModel instead)
public class GameViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private MatchPreferences matchPreferences;

    // Add a private LiveData member variable to cache the current and previous game
    private LiveData<Game> currentGame;
    private LiveData<Game> previousGame;
    private LiveData<List<Integer>> stagesRecentLoserHasWonOn;
    private List<Integer> stageClauseList;
    private LiveData<List<Game>> stagesPlayerHasWonOn;

    // Temporary values before the database gets updated with a new game
    private int tempPlayerAScore;
    private int tempPlayerBScore;
    private String tempWinnerOfGame;
    private String tempLoserOfGame;

    // Constructor gets the reference to the repository and gets the current game from the repository
    public GameViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        matchPreferences = appRepository.getMatchPreferences();
        currentGame = appRepository.getCurrentGame();
        stagesRecentLoserHasWonOn = appRepository.getStagesRecentLoserHasWonOn();
        try {
            stageClauseList = appRepository.getStageClauseList();
        } catch (ExecutionException e) {
            // Do something
        } catch (InterruptedException i) {
            // Do another thing
        }
        // stagesPlayerHasWonOn = appRepository.getStagesPlayerHasWonOn();
    }

    ///// METHODS /////


    public MatchPreferences getMatchPreferences() {
        return matchPreferences;
    }

    public LiveData<Game> getCurrentGame() {
        return currentGame;
    }

    public LiveData<Game> getPreviousGame() {
        return previousGame;
    }

    public LiveData<List<Integer>> getStagesRecentLoserHasWonOn() {
        return stagesRecentLoserHasWonOn;
    }

    public List<Integer> getStageClauseList() {
        return stageClauseList;
    }

    public LiveData<List<Game>> getStagesPlayerHasWonOn() {
        return stagesPlayerHasWonOn;
    }

    public int getTempPlayerAScore() {
        return tempPlayerAScore;
    }

    public void setTempPlayerAScore(int tempPlayerAScore) {
        this.tempPlayerAScore = tempPlayerAScore;
    }

    public int getTempPlayerBScore() {
        return tempPlayerBScore;
    }

    public void setTempPlayerBScore(int tempPlayerBScore) {
        this.tempPlayerBScore = tempPlayerBScore;
    }

    public String getTempWinnerOfGame() {
        return tempWinnerOfGame;
    }

    public void setTempWinnerOfGame(String tempWinnerOfGame) {
        this.tempWinnerOfGame = tempWinnerOfGame;
    }

    public String getTempLoserOfGame() {
        return tempLoserOfGame;
    }

    public void setTempLoserOfGame(String tempLoserOfGame) {
        this.tempLoserOfGame = tempLoserOfGame;
    }

    public void insert(Game game) {
        appRepository.insert(game);
    }
    public void delete(Game game) {
        appRepository.delete(game);
    }
    public void deleteAllGames() {
        appRepository.deleteAllGames();
    }
}
