package com.example.android.meleematchassistant.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.android.meleematchassistant.Game;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.GameViewModel;

public class MatchSummaryActivity extends AppCompatActivity {

    private static final String TAG = MatchSummaryActivity.class.getSimpleName();

    private GameViewModel gameViewModel;
    private TextView winnerTextView;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_summary);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        winnerTextView = findViewById(R.id.winner_text_view);
        scoreTextView = findViewById(R.id.score_text_view);

        // Add up button to ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final AppCompatButton continueButton = findViewById(R.id.finish_button);
        continueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MatchSummaryActivity.this, MatchStartActivity.class);
                gameViewModel.getCurrentGame().removeObservers(MatchSummaryActivity.this); // Need remove the observers so that the setText() method is not triggered
                gameViewModel.deleteAllGames();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // TODO Do I need to call this?
            }
        });

        gameViewModel.getCurrentGame().observe(this, new Observer<Game>() {
            @Override
            public void onChanged(@Nullable Game game) {
                // Need this for the getValue() method, used in onBackPressed(), to work properly (assuming that the subsequent code was not needed as well)
                winnerTextView.setText(game.getWinnerOfGame() + " wins!");
                scoreTextView.setText(game.getPlayerAScore() + " - " + game.getPlayerBScore());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameViewModel.getCurrentGame().removeObservers(MatchSummaryActivity.this); // Need remove the observers so that the setText() method is not triggered
        gameViewModel.delete(gameViewModel.getCurrentGame().getValue());
        Log.v(TAG, "Current game deleted");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
