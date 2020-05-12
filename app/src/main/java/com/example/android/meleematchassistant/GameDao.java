package com.example.android.meleematchassistant;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

// DAOs (Data Access Object) are the main component of ROOM that are responsible for defining methods used for querying the database
// Annotated methods are used to generate the corresponding SQL (parsed and validated!) at compile time
// Must be an interface or defined as abstract

// It is recommended to have multiple Dao classes in your codebase depending on the tables they touch

@Dao
public interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Game game);

    @Update
    void update(Game game);

    // @Query("SELECT winnerOfGame, stagePlayedOnIndex, stagePlayedOn FROM Games WHERE winnerOfGame = :gameWinner")
    // LiveData<List<Game>> getStagesPlayerHasWonOn(String gameWinner);

    @Query("SELECT * FROM games_table WHERE gameNumber = (SELECT MAX(gameNumber) FROM games_table)")
    LiveData<Game> getCurrentGame();

    @Query("SELECT * FROM games_table WHERE gameNumber = (SELECT MAX(gameNumber - 1) FROM games_table)")
    LiveData<Game> getPreviousGame();

    @Query("SELECT * FROM games_table ORDER BY gameNumber ASC")
    LiveData<List<Game>> getAllGames();

    // Get stages that the most recent loser has won on
    @Query("SELECT stagePlayedOnIndex FROM games_table WHERE loserOfGame = (SELECT loserOfGame FROM games_table WHERE gameNumber = (SELECT MAX(gameNumber - 1) FROM games_table))")
    LiveData<List<Integer>> getStagesRecentLoserHasWonOn();

    @Query("SELECT stagePlayedOnIndex FROM games_table WHERE loserOfGame = (SELECT loserOfGame FROM games_table WHERE gameNumber = (SELECT MAX(gameNumber - 1) FROM games_table))")
    List<Integer> getStageClauseList();

    @Delete
    void delete(Game game);

    @Query("DELETE FROM games_table")
    void deleteAllGames();
}
