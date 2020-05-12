package com.example.android.meleematchassistant;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

// A table is generated within the database for each listed entity
@Database(entities = {Game.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Provide abstract "getter" methods for each DAO
    // Must have an abstract method that has 0 arguments and returns the class that is annotated with Dao.
    public abstract GameDao gameDao();

    // Follow the singleton design pattern when instantiating an AppDatabase object, to prevent having multiple instances
    // of the database opened at the same time as each RoomDatabase instance is fairly expensive, and you rarely need access to multiple instances.
    private static AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "game_database")
                    // INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}