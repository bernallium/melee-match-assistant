package com.example.android.meleematchassistant.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.GameViewModel;

public class MatchStartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MatchStartActivity.class.getSimpleName();
    private GameViewModel gameViewModel;
    private Toolbar toolbar;
    private Button bestOf1Button;
    private Button bestOf3Button;
    private Button bestOf5Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_start);

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bestOf1Button = findViewById(R.id.best_of_1_button);
        bestOf3Button = findViewById(R.id.best_of_3_button);
        bestOf5Button = findViewById(R.id.best_of_5_button);
        bestOf1Button.setOnClickListener(this);
        bestOf3Button.setOnClickListener(this);
        bestOf5Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int numOfWinsNeeded = 1; // Default value
        switch (view.getId()) {
            case R.id.best_of_1_button:
                numOfWinsNeeded = 1;
                break;
            case R.id.best_of_3_button:
                numOfWinsNeeded = 2;
                break;
            case R.id.best_of_5_button:
                numOfWinsNeeded = 3;
                break;
        }
        gameViewModel.getMatchPreferences().setNumOfWinsNeeded(numOfWinsNeeded);
        gameViewModel.deleteAllGames(); // Make sure to clear any leftover games from the database
        Intent intent = new Intent(MatchStartActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
