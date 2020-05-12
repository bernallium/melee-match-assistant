package com.example.android.meleematchassistant.Activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.meleematchassistant.Fragments.FirstStrikePortSelectionStepFragment;
import com.example.android.meleematchassistant.Fragments.RockPaperScissorsStepFragment;
import com.example.android.meleematchassistant.Fragments.StageStrikingStepFragment;
import com.example.android.meleematchassistant.PlayersDatabase;
import com.example.android.meleematchassistant.PlayersDatabaseImpl;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.FSPSViewModel;
import com.example.android.meleematchassistant.ViewModels.GameViewModel;
import com.example.android.meleematchassistant.ViewModels.MainViewModel;
import com.example.android.meleematchassistant.model.MatchPreferences;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class MainActivity extends AppCompatActivity implements VerticalStepperForm {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CHAR_SELECTION_STEP_NUM = 0;
    private static final int ROCK_PAPER_SCISSORS_STEP_NUM = 1;
    private static final int FSPS_STEP_NUM = 2;
    private static final int STAGE_STRIKING_STEP_NUM = 3;
    private int numberOfSteps;
    private VerticalStepperFormLayout verticalStepperFormLayout;

    private MatchPreferences matchPreferences;

    private MainViewModel mainViewModel;
    private FSPSViewModel fSPSViewModel;
    private PlayersDatabase playersDatabase = new PlayersDatabaseImpl();
    private GameViewModel gameViewModel;

    private RockPaperScissorsStepFragment rockPaperScissorsStepFragment;
    private FirstStrikePortSelectionStepFragment firstStrikePortSelectionStepFragment;
    private StageStrikingStepFragment stageStrikingStepFragment;

    ////////// GETTERS and SETTERS //////////

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchPreferences = new MatchPreferences(this);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        fSPSViewModel = ViewModelProviders.of(this).get(FSPSViewModel.class);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.best_of) + " " + matchPreferences.getBestOfNum());

        // Create observer > Observe some LiveData > Update the UI upon change
        mainViewModel.isCharSelectionStepCompleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean stepCompleted) {
                updateStepCompletionState(stepCompleted, CHAR_SELECTION_STEP_NUM);
            }
        });
        mainViewModel.isRPSStepCompleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean stepCompleted) {
                updateStepCompletionState(stepCompleted, ROCK_PAPER_SCISSORS_STEP_NUM);
            }
        });
        mainViewModel.isFSPSStepCompleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean stepCompleted) {
                updateStepCompletionState(stepCompleted, FSPS_STEP_NUM);
            }
        });
        mainViewModel.isStageStrikingStepCompleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean stepCompleted) {
                updateStepCompletionState(stepCompleted, STAGE_STRIKING_STEP_NUM);
            }
        });

        String[] stepTitles = {
                getString(R.string.char_selection_step_title),
                getString(R.string.rock_paper_scissors_step_title),
                getString(R.string.first_strike_port_selection_step_title),
                getString(R.string.stage_striking_step_title)
        };

        setNumberOfSteps(stepTitles.length);

        String[] stepSubtitles = {
                "",
                " ",
                " ",
                "",
                ""
        };

            int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        verticalStepperFormLayout = findViewById(R.id.vertical_stepper_form);
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperFormLayout, stepTitles, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(false)
                .stepTitleTextColor(Color.WHITE)
                .showVerticalLineWhenStepsAreCollapsed(false)
                // .stepsSubtitles(stepSubtitles)
                .materialDesignInDisabledSteps(false) // If false, incomplete steps are slightly transparent
                .init();
    }

    // Updates the step completion state  when a step is completed or uncompleted

    public void updateStepCompletionState(Boolean stepCompleted, int stepNumber) {
        if (stepCompleted) {
            verticalStepperFormLayout.setStepAsCompleted(stepNumber);
        } else {
            verticalStepperFormLayout.setStepAsUncompleted(stepNumber);
        }
        setSubsequentStepsAsUncompleted(stepNumber);
    }

    // Marks all steps subsequent to the step indicated as uncompleted
    public void setSubsequentStepsAsUncompleted(int stepNumber) {
        for (int i = (stepNumber + 1); i <= getNumberOfSteps(); i++) {
            verticalStepperFormLayout.setStepAsUncompleted(i);
        }
    }

    /**
     * Called automatically by the system to generate the view of each step.
     * Need to implement the generation of the corresponding step view and return it.
     *
     * @param stepNumber is an int representing the current step number the user is on.
     * @return the step content view that corresponds to the current stepNumber.
     */
    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case CHAR_SELECTION_STEP_NUM:
                view = createCharSelectionStepContent();
                mainViewModel.isCharSelectionStepCompleted().setValue(Boolean.TRUE);
                break;
            case ROCK_PAPER_SCISSORS_STEP_NUM:
                view = createRockPaperScissorsStepContent();
                break;
            case FSPS_STEP_NUM:
                view = createFSPSStepContent();
                break;
            case STAGE_STRIKING_STEP_NUM:
                view = createStageStrikingStepContent();
                break;
        }
        return view;
    }

    private View createCharSelectionStepContent() {
        TextView CharSelectInstructions = new TextView(this);
        CharSelectInstructions.setText(R.string.char_selection_step_instructions);
        return CharSelectInstructions;
    }

    private View createRockPaperScissorsStepContent() {
        rockPaperScissorsFragmentTransaction();
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayoutCompat rockPaperScissorsStepFragmentContainer = (LinearLayoutCompat) inflater.inflate(R.layout.rock_paper_scissors_step_fragment_container, null, false);
        return rockPaperScissorsStepFragmentContainer;
    }

    private View createFSPSStepContent() {
        fSPSFragmentTransaction();
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayoutCompat firstStrikePortSelectionStepFragmentContainer = (LinearLayoutCompat) inflater.inflate(R.layout.first_strike_port_selection_step_fragment_container, null, false);
        return firstStrikePortSelectionStepFragmentContainer;
    }

    private View createStageStrikingStepContent() {
        stageStrikingFragmentTransaction();
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayoutCompat StageStrikingStepFragmentContainer = (LinearLayoutCompat) inflater.inflate(R.layout.stage_striking_step_fragment_container, null, false);
        return StageStrikingStepFragmentContainer;
    }

    private void rockPaperScissorsFragmentTransaction() {
        rockPaperScissorsStepFragment = new RockPaperScissorsStepFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rock_paper_scissors_step_fragment_container, rockPaperScissorsStepFragment)
                .addToBackStack(null) // TODO Figure out why this is needed... it's relevant for when observing
                .commit();
    }

    private void fSPSFragmentTransaction() {
        firstStrikePortSelectionStepFragment = new FirstStrikePortSelectionStepFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.first_strike_port_selection_step_fragment_container, firstStrikePortSelectionStepFragment)
                .addToBackStack(null)
                .commit();
    }

    private void stageStrikingFragmentTransaction() {
        stageStrikingStepFragment = new StageStrikingStepFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.stage_striking_step_fragment_container, stageStrikingStepFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * This method will be called every time a certain step is open
     *
     * @param stepNumber the number of the step
     */
    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case CHAR_SELECTION_STEP_NUM:
                fSPSViewModel.setCanClearCheckedRadioGroup(false); // TODO Find a better place to put this?
                break;
            case ROCK_PAPER_SCISSORS_STEP_NUM:
                fSPSViewModel.setCanClearCheckedRadioGroup(true); // TODO Find a better place to put this?
                break;
            case FSPS_STEP_NUM:
                fSPSViewModel.setCanClearCheckedRadioGroup(false); // TODO Find a better place to put this?
                break;
            case STAGE_STRIKING_STEP_NUM:
                break;
        }
    }

    /**
     * This method will be called when the user press the confirmation button
     */
    @Override
    public void sendData() {
        Intent intent = new Intent(this, GameSummaryActivity.class);
        intent.putExtra("intentTag", TAG);
        intent.putExtra("stageIndex", stageStrikingStepFragment.getRemainingStageIndex());
        startActivity(intent);
    }

    // Temporary AlertDialog to warn user that data does not persist afterwards
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setTitle("Exit current match?")
                .setMessage("Doing so will erase all current match data")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                gameViewModel.deleteAllGames();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
