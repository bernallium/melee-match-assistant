package com.example.android.meleematchassistant.Activities;

import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.meleematchassistant.Fragments.StageStrikingStepFragment;
import com.example.android.meleematchassistant.Game;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.GameViewModel;
import com.example.android.meleematchassistant.ViewModels.StageSelectionViewModel;

public class StageSelectionActivity extends AppCompatActivity {

    private static final String TAG = StageSelectionActivity.class.getSimpleName();

    private GameViewModel gameViewModel;
    private StageSelectionViewModel stageSelectionViewModel;

    private Toolbar toolbar;
    private Button continueButton;
    private TextView banPickInstructionsTextView;
    private TextView characterSelectionOrderInstructionsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_selection);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        stageSelectionViewModel = ViewModelProviders.of(this).get(StageSelectionViewModel.class);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.stage_selection_fragment_container, new StageStrikingStepFragment())
                .commit();

        // Add up button to ToolBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(R.string.stage_selection_string);

        continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StageSelectionActivity.this, GameSummaryActivity.class);
                intent.putExtra("intentTag", TAG);
                intent.putExtra("stageIndex", stageSelectionViewModel.getChosenStageIndex());
                startActivity(intent);
            }
        });

        continueButton.setEnabled(false);
        continueButton.setAlpha(0.25f);
        stageSelectionViewModel.isStartButtonEnabled().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean playerHasChosenStage) {
                continueButton.setEnabled(false);
                ObjectAnimator objectDisabledAnimator = ObjectAnimator.ofFloat(continueButton,"alpha",0.25f);
                objectDisabledAnimator.setDuration(300)
                        .start();
                if (playerHasChosenStage) {
                    continueButton.setEnabled(true);
                    ObjectAnimator objectEnabledAnimator = ObjectAnimator.ofFloat(continueButton,"alpha",1);
                    objectEnabledAnimator.setDuration(300)
                            .start();
                }
                else continueButton.setEnabled(false);
            }
        });

        // Updates the UI whenever changes to the previous game occurs
        gameViewModel.getCurrentGame().observe(this, new Observer<Game>() {
            @Override
            public void onChanged(@Nullable Game currentGame) {
                if (currentGame != null) { // Prevents NullPointerException when the user presses back from and there is currently only 1 game stored in the database
                    // Dynamic action bar
                    int gameNumber;
                    if (currentGame != null) {
                        gameNumber = currentGame.getGameNumber() + 1; // Increase by 1 since it's for the game to be inserted
                    } else {
                        gameNumber = 1;
                    }
                    stageSelectionViewModel.setActionBarString(getString(R.string.game) + " " + gameNumber);
                    getSupportActionBar().setTitle(stageSelectionViewModel.getActionBarString());

                    // Set instructions:
                    // 1. Player X bans a stage
                    // 2. Player Y picks a stage
                    banPickInstructionsTextView = findViewById(R.id.ban_pick_instructions_text_view);
                    banPickInstructionsTextView.setText(String.format("1. %s %s\n2. %s %s",
                            currentGame.getWinnerOfGame(), getString(R.string.bans_a_stage), currentGame.getLoserOfGame(), getString(R.string.picks_a_remaining_stage)));
                    // 3. Player X selects their character first
                    // 4. Player Y selects their character second
                    characterSelectionOrderInstructionsTextView = findViewById(R.id.character_selection_order_instructions_text_view);
                    characterSelectionOrderInstructionsTextView.setText(String.format("3. %s %s\n4. %s %s",
                            currentGame.getWinnerOfGame(), getString(R.string.selects_their_character_first), currentGame.getLoserOfGame(), getString(R.string.selects_their_character_second)));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.active_game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                        .setTitle(R.string.stage_selection_alert_dialog_title)
                        .setMessage(R.string.stage_selection_alert_dialog_body)
                        .show();
                return true;
            case R.id.quit_match:
                Intent intent = new Intent(StageSelectionActivity.this, MatchStartActivity.class);
                gameViewModel.deleteAllGames();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.pending_intent:
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                Intent gameSummaryIntent = new Intent(this, GameSummaryActivity.class);
                Intent stageSelectionActivityIntent = new Intent(this, StageSelectionActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(mainActivityIntent);
                stackBuilder.addNextIntent(stageSelectionActivityIntent);
                stackBuilder.addNextIntent(gameSummaryIntent);
                stackBuilder.startActivities();

                gameViewModel.delete(gameViewModel.getCurrentGame().getValue());
                Log.v(TAG, "Game " + gameViewModel.getCurrentGame().getValue().getGameNumber() + " was deleted.");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameViewModel.delete(gameViewModel.getCurrentGame().getValue());
        Log.v(TAG, "Game " + gameViewModel.getCurrentGame().getValue().getGameNumber() + " was deleted.");
    }
}