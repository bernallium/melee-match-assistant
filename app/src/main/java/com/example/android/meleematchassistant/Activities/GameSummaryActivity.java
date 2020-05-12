package com.example.android.meleematchassistant.Activities;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.android.meleematchassistant.Game;
import com.example.android.meleematchassistant.PlayersDatabase;
import com.example.android.meleematchassistant.PlayersDatabaseImpl;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.GameSummaryViewModel;
import com.example.android.meleematchassistant.ViewModels.GameViewModel;
import com.example.android.meleematchassistant.model.Players;
import com.example.android.meleematchassistant.model.Stages;

import pl.droidsonroids.gif.GifImageView;

public class GameSummaryActivity extends AppCompatActivity implements OnCheckedChangeListener {

    private static final String TAG = GameSummaryActivity.class.getSimpleName();
    private PlayersDatabase playersDatabase = new PlayersDatabaseImpl();
    private Stages stages = new Stages();
    private GameSummaryViewModel gameSummaryViewModel;
    private GameViewModel gameViewModel;
    private Game currentGame;
    private String intentTag;
    private int stageIndex;

    private Toolbar toolbar;
    private GifImageView stageGifImageView;
    private RadioButton playerARadioButton;
    private RadioButton playerBRadioButton;
    private TextSwitcher playerAScoreTextSwitcher;
    private TextSwitcher playerBScoreTextSwitcher;
    private CardView playerACardView;
    private CardView playerBCardView;
    private Button continueButton;

    ////////// GETTERS and SETTERS //////////

    public String getIntentTag() {
        return intentTag;
    }

    public void setIntentTag(String intentTag) {
        this.intentTag = intentTag;
    }

    public int getStageIndex() {
        return stageIndex;
    }

    public void setStageIndex(int stageIndex) {
        this.stageIndex = stageIndex;
    }

    /////////////////////////////////////////

    // NOTE: The UI doesn't reflect the actual "current game", but the game that is about to be added to the database (game number, scores, etc.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_summary);

        gameSummaryViewModel = ViewModelProviders.of(this).get(GameSummaryViewModel.class);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        // Receive intent and extract any bundles
        Intent intent = getIntent();
        if (intent.hasExtra("stageIndex")) {
            setStageIndex(intent.getIntExtra("stageIndex", -1));
        } else {
            setStageIndex(1);
        }
        if (intent.hasExtra("intentTag")) {
            setIntentTag(intent.getStringExtra("intentTag"));
        } else {
            setIntentTag("test");
        }

        // Add up button to ToolBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(R.string.in_progess);

//        // Set stage image
//        stageGifImageView = findViewById(R.id.stage_gif_image_view);
//        stageGifImageView.setBackgroundResource(stages.getStage(getStageIndex()).getStageImageResource());

        // Set Listener on to RadioButtons
        playerARadioButton = findViewById(R.id.player_a_radio_button);
        playerBRadioButton = findViewById(R.id.player_b_radio_button);
        playerARadioButton.setOnCheckedChangeListener(this);
        playerBRadioButton.setOnCheckedChangeListener(this);

        // Set up TextSwitchers
        playerAScoreTextSwitcher = findViewById(R.id.player_a_score_text_switcher);
        playerBScoreTextSwitcher = findViewById(R.id.player_b_score_text_switcher);
        setUpTextSwitcher(playerAScoreTextSwitcher, 48, getResources().getColor(R.color.playerATextColor));
        setUpTextSwitcher(playerBScoreTextSwitcher, 48, getResources().getColor(R.color.playerBTextColor));

        // Need to reset it to false for when the device configuration changes
        gameSummaryViewModel.setActionBarWasSet(false);

        // Create observer > Observe some LiveData > Update the UI upon change
        // Updates the UI whenever changes to the most recent game occur
        gameViewModel.getCurrentGame().observe(this, new Observer<Game>() {
            @Override
            public void onChanged(@Nullable Game currentGame) {
                // Initialize the class currentGame variable with database values or a dummy game
                // Setup dynamic action bar
                if (currentGame != null) { // If there is a game in the database already, use it as a reference for the new game
                    GameSummaryActivity.this.currentGame = currentGame;
                    gameSummaryViewModel.setActionBarString(getString(R.string.game) + " " + (currentGame.getGameNumber() + 1)); // We increase it by 1 because the UI represents values for a game which is about to be inserted
                } else { // If there is no game stored in the database (i.e. the beginning of the match), initialize with a dummy game --> NOTE: This exact game instance won't be saved to the database
                    currentGame = new Game(1, null, -1, 0, 0, null, null); // Dummy game values don't all need to make sense
                    GameSummaryActivity.this.currentGame = currentGame;
                    gameSummaryViewModel.setActionBarString(getString(R.string.game) + " " + currentGame.getGameNumber());
                }

                if (!gameSummaryViewModel.wasActionBarSet())
                    getSupportActionBar().setTitle(gameSummaryViewModel.getActionBarString());
                gameSummaryViewModel.setActionBarWasSet(true);

                // Set stage image
                stageGifImageView = findViewById(R.id.stage_gif_image_view);
                stageGifImageView.setBackgroundResource(stages.getStage(getStageIndex()).getStageImageResource());

                // Update the score UI using the database query and reading the current score --> This UI will be updated with values from the ViewModel when the user checks a RadioButton
                if (gameViewModel.getTempWinnerOfGame() != null) {
                    playerAScoreTextSwitcher.setText(String.valueOf(gameViewModel.getTempPlayerAScore()));
                    playerBScoreTextSwitcher.setText(String.valueOf(gameViewModel.getTempPlayerBScore()));
                } else { // Sets the score if using a dummy game
                    playerAScoreTextSwitcher.setText(String.valueOf(currentGame.getPlayerAScore()));
                    playerBScoreTextSwitcher.setText(String.valueOf(currentGame.getPlayerBScore()));
                }
            }
        });

        // Set instructions for player with port selection priority
        TextView portSelectionPlayerInstructionsTextView = findViewById(R.id.port_selection_player_instructions);
        String portSelectionPlayerInstructions;
        if (getIntentTag().equals(MainActivity.class.getSimpleName())) {
            portSelectionPlayerInstructions = playersDatabase.getPortSelectionPlayerString() + " " + getString(R.string.stage_striking_step_instructions_player_with_port_selection_priority);
        } else {
            portSelectionPlayerInstructions = getString(R.string.game_summary_port_selection_instructions);
        }
        portSelectionPlayerInstructionsTextView.setText(portSelectionPlayerInstructions);

        // Set up CONTINUE button behaviour
        continueButton = findViewById(R.id.continue_button);
        continueButton.setEnabled(false); // CONTINUE button starts off as disabled --> Becomes enabled once a RadioButton is checked (i.e. when a winner is chosen)
        continueButton.setAlpha(0.25f);
        continueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert a new game, passing in the stage parameters from the intent and the scores cached in the ViewModel
                if (getIntentTag().equals(MainActivity.class.getSimpleName())) { // Need this so that the default insert case doesn't skip inserting game 1 (since game 1 was a dummy game)
                    gameViewModel.insert(new Game(1, stages.getStage(getStageIndex()).getStageName(), getStageIndex(),
                            gameViewModel.getTempPlayerAScore(), gameViewModel.getTempPlayerBScore(), gameViewModel.getTempWinnerOfGame(), gameViewModel.getTempLoserOfGame()));
                } else { // Inserts a new game while incrementing the gameNumber (NOTE: Do not increment from the dummy game)
                    gameViewModel.insert(new Game(currentGame.getGameNumber() + 1, stages.getStage(getStageIndex()).getStageName(), getStageIndex(),
                            gameViewModel.getTempPlayerAScore(), gameViewModel.getTempPlayerBScore(), gameViewModel.getTempWinnerOfGame(), gameViewModel.getTempLoserOfGame()));
                }

                Intent intent;
                if ((gameViewModel.getTempPlayerAScore() >= gameViewModel.getMatchPreferences().getNumOfWinsNeeded())
                        || (gameViewModel.getTempPlayerBScore() >= gameViewModel.getMatchPreferences().getNumOfWinsNeeded())) { // End of match --> Go to MatchSummaryActivity
                    intent = new Intent(GameSummaryActivity.this, MatchSummaryActivity.class);
                } else {
                    intent = new Intent(GameSummaryActivity.this, StageSelectionActivity.class);
                }
                startActivity(intent);
            }
        });

        playerACardView = findViewById(R.id.player_a_card_view);
        playerBCardView = findViewById(R.id.player_b_card_view);
        playerACardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playerARadioButton.setChecked(true);
            }
        });
        playerBCardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playerBRadioButton.setChecked(true);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int checkedId = compoundButton.getId();
        if (isChecked) {
            if (checkedId == R.id.player_a_radio_button) {
                playerBRadioButton.setChecked(false);
            }
            if (checkedId == R.id.player_b_radio_button) {
                playerARadioButton.setChecked(false);
            }
            queryCheckedGameWinner(checkedId);
            playerAScoreTextSwitcher.setText(String.valueOf(gameViewModel.getTempPlayerAScore()));
            playerBScoreTextSwitcher.setText(String.valueOf(gameViewModel.getTempPlayerBScore()));
            continueButton.setEnabled(true);
            ObjectAnimator.ofFloat(continueButton, "alpha", 1)
                    .setDuration(300)
                    .start();
            Log.v(TAG, "Score (A-B): " + gameViewModel.getTempPlayerAScore() + " - " + gameViewModel.getTempPlayerBScore());
        }
    }

    // Set up TextSwitcher and TextView for player scores
    private void setUpTextSwitcher(TextSwitcher textSwitcher, final int textSize, final int textColor) {
        long fadeDuration = 135;
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeInAnimation.setDuration(fadeDuration);
        fadeOutAnimation.setDuration(fadeDuration);

        textSwitcher.setInAnimation(fadeInAnimation);
        textSwitcher.setOutAnimation(fadeOutAnimation);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView playerScoreTextView = new TextView(GameSummaryActivity.this);
                playerScoreTextView.setTextSize(textSize);
                playerScoreTextView.setTextColor(textColor);
                return playerScoreTextView;
            }
        });
    }

    // Queries which player won the current game
    public void queryCheckedGameWinner(int checkedId) {
        int checkedPlayerId;
        int uncheckedPlayerId;
        if (checkedId == R.id.player_a_radio_button) { // User checked "Player A" option
            checkedPlayerId = Players.PLAYER_A_ID;
            uncheckedPlayerId = Players.PLAYER_B_ID;
            gameViewModel.setTempPlayerAScore(currentGame.getPlayerAScore() + 1);
            gameViewModel.setTempPlayerBScore(currentGame.getPlayerBScore());
        } else { // User checked "Player B" option
            checkedPlayerId = Players.PLAYER_B_ID;
            uncheckedPlayerId = Players.PLAYER_A_ID;
            gameViewModel.setTempPlayerBScore(currentGame.getPlayerBScore() + 1);
            gameViewModel.setTempPlayerAScore(currentGame.getPlayerAScore());
        }
        gameViewModel.setTempWinnerOfGame(playersDatabase.getPlayerStringFromId(checkedPlayerId));
        gameViewModel.setTempLoserOfGame(playersDatabase.getPlayerStringFromId(uncheckedPlayerId));
        Log.v(TAG, "Winner of Game " + currentGame.getGameNumber() + ": " + gameViewModel.getTempWinnerOfGame());
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
                        .setTitle(R.string.game_summary_alert_dialog_title)
                        .setMessage(R.string.game_summary_alert_dialog_body)
                        .show();
                return true;
            case R.id.quit_match:
                Intent intent = new Intent(GameSummaryActivity.this, MatchStartActivity.class);
                gameViewModel.deleteAllGames();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
