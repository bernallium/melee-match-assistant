package com.example.android.meleematchassistant.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.meleematchassistant.Activities.MainActivity;
import com.example.android.meleematchassistant.Activities.StageSelectionActivity;
import com.example.android.meleematchassistant.Game;
import com.example.android.meleematchassistant.PlayersDatabase;
import com.example.android.meleematchassistant.PlayersDatabaseImpl;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.GameViewModel;
import com.example.android.meleematchassistant.ViewModels.MainViewModel;
import com.example.android.meleematchassistant.ViewModels.StageSelectionViewModel;
import com.example.android.meleematchassistant.ViewModels.StageStrikingViewModel;
import com.example.android.meleematchassistant.model.Stages;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StageStrikingStepFragment extends Fragment {

    private static final String TAG = StageStrikingStepFragment.class.getSimpleName();
    private StageStrikingViewModel stageStrikingViewModel;
    private GameViewModel gameViewModel;
    private MainViewModel mainViewModel;
    private StageSelectionViewModel stageSelectionViewModel;
    private PlayersDatabase playersDatabase = new PlayersDatabaseImpl();
    private int numberOfVisibleRevealViews;
    private String remainingStageName;
    private int remainingStageIndex;
    private Animator circularStrikeAnimator;
    private Animator circularStrikeRemovalAnimator;
    private GridLayout stageStrikingGridLayout;

    ////////// GETTERS and SETTERS //////////

    public StageStrikingStepFragment() {
        // Required empty public constructor
    }

    public int getNumberOfVisibleRevealViews() {
        return numberOfVisibleRevealViews;
    }

    public void setNumberOfVisibleRevealViews(int numberOfVisibleRevealViews) {
        this.numberOfVisibleRevealViews = numberOfVisibleRevealViews;
    }

    public String getRemainingStageName() {
        return remainingStageName;
    }

    public void setRemainingStageName(String remainingStageName) {
        this.remainingStageName = remainingStageName;
    }

    public int getRemainingStageIndex() {
        return remainingStageIndex;
    }

    public void setRemainingStageIndex(int remainingStageIndex) {
        this.remainingStageIndex = remainingStageIndex;
    }

    /////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_stage_striking_step, container, false);
        stageStrikingViewModel = ViewModelProviders.of(getActivity()).get(StageStrikingViewModel.class);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        if (getActivity().getClass() == MainActivity.class)
            mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        if (getActivity().getClass() == StageSelectionActivity.class)
            stageSelectionViewModel = ViewModelProviders.of(getActivity()).get(StageSelectionViewModel.class);

        // Create observer > Observe some LiveData > Update the UI upon change
        if (getActivity().getClass() == MainActivity.class) {
            mainViewModel.isRPSStepCompleted().observe(getActivity(), new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean stepCompleted) {
                    unstrikeAllStarterStages();
                }
            });
            mainViewModel.isFSPSStepCompleted().observe(getActivity(), new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean stepCompleted) {
                    unstrikeAllStarterStages();
                    displayText(rootView, getString(R.string.stage_striking_step_instructions),
                            createStrikingOrder(playersDatabase.getFirstStrikePlayerString(), playersDatabase.getPortSelectionPlayerString()));
                }
            });
        }

        if (gameViewModel.getStageClauseList() != null) // Is this line Needed?
        for (Integer stageIndex : gameViewModel.getStageClauseList()) {
            stageStrikingViewModel.stages.getStage(stageIndex).setStagePlayable(false);
            Log.v(TAG, stageStrikingViewModel.stages.getStage(stageIndex).getStageName() + " was marked as unplayable.");
        } else {
            Log.v(TAG, "NOTHING HAPPENED");
        }

        if (getActivity().getClass() == StageSelectionActivity.class && gameViewModel.getMatchPreferences().getBestOfNum() == 5) {
            stageSelectionViewModel.setPlayerHasBanned(true);
        }

        // Use for loop to inflate stageCardViews and add them to the stageStrikingGridLayout
        stageStrikingGridLayout = rootView.findViewById(R.id.stage_striking_grid_layout);
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            // Must inflate the stageCardView and add it to the GridLayout at the ith position

           GridLayout.LayoutParams gridLayoutParams = new GridLayout.LayoutParams();
//                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f), // row
//                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f)); // column
            gridLayoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f);
            gridLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f);
            gridLayoutParams.width = 0;
            gridLayoutParams.height = 0;
//            gridLayoutParams.topMargin = 32;
//            gridLayoutParams.bottomMargin = 32;
//            gridLayoutParams.leftMargin = 32;
//            gridLayoutParams.rightMargin = 32;

            final CardView stageCardView = (CardView) inflater.inflate(R.layout.stage_button_layout, null, false);
            stageCardView.setLayoutParams(gridLayoutParams);
            stageStrikingGridLayout.addView(stageCardView, i);

            // Bottom layer: Contains the stage image
            GifImageView stageImageView = stageCardView.findViewById(R.id.stage_image_view);
            stageImageView.setImageResource(stageStrikingViewModel.stages.getStage(i).getStageImageResource());
            RadioButton baseViewRadioButton = stageCardView.findViewById(R.id.stage_button_layout_radioButton);
            baseViewRadioButton.setVisibility(View.GONE);
            final Drawable stageDrawable = stageCardView.getBackground();
            // quickGrayscale(stageDrawable);

            // Top layer: Red layer revealed using the circular reveal animation
            final CardView revealView = stageCardView.findViewById(R.id.reveal_view);

            final int finalI = i;

            // Create observer > Observe some LiveData > Update the UI upon change
            gameViewModel.getCurrentGame().observe(this, new Observer<Game>() {
                @Override
                public void onChanged(@Nullable final Game currentGame) {
                    // If within the MainActivity and the stage is a starter stage OR if within the StageSelectionActivity and the stage wasn't previously played on
                    // resaturate stage image, prevent red revealView from appearing on top and start the circular reveal animation upon touch
                    if (((getActivity().getClass() == MainActivity.class && stageStrikingViewModel.stages.getStage(finalI).isStarterStage())
                            || getActivity().getClass() == StageSelectionActivity.class && stageStrikingViewModel.stages.getStage(finalI).isStagePlayable())) {
                        // quickSaturate(stageDrawable);
                        revealView.setVisibility(View.INVISIBLE);
                        // In a best of 5 set, there is no stage banning (ie. the loser simply picks a stage)
                        if (getActivity().getClass() == StageSelectionActivity.class && gameViewModel.getMatchPreferences().getBestOfNum() == 5) {
                            revealRadioButtons();
                        }
                        stageCardView.setOnTouchListener(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View touchedView, MotionEvent motionEvent) { // NOTE: The touchedView is the stageCardView
                                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                    startCircularRevealAnimation(revealView, touchedView, stageDrawable, motionEvent);
                                }
                                return false;
                            }
                        });
                        // This line restores previous state of striked stages upon configuration change
                        if (stageStrikingViewModel.stages.getStage(finalI).isStageStriked())
                            revealView.setVisibility(View.VISIBLE);
                        else
                            revealView.setVisibility(View.INVISIBLE); // This line is a bit redundant
                    } else if (!stageStrikingViewModel.stages.getStage(finalI).isStarterStage() && gameViewModel.getMatchPreferences().getBestOfNum() < 5) { // If it's not a starter stage, just display a Toast message upon click
                        // The second conditions is needed so that in a best of 5, if Pokemon Stadium is not playable due to the stage clause, the correct message is displayed
                        stageCardView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int indexOfMyView = ((GridLayout) view.getParent()).indexOfChild(view);
                                Toast toast = Toast.makeText(getContext(), stageStrikingViewModel.stages.getStage(indexOfMyView).getStageName() + " is not a Starter Stage and will become playable after Game 1", Toast.LENGTH_SHORT);
                                shortenDurationOfToast(toast, 1500);
                                toast.show();
                            }
                        });
                    } else { // Default scenario is that the stage is not playable (red revealView remains VISIBLE), which in this case just display a Toast message upon click
                        stageCardView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int indexOfMyView = ((GridLayout) view.getParent()).indexOfChild(view);
                                Toast toast = Toast.makeText(getContext(), stageStrikingViewModel.stages.getStage(indexOfMyView).getStageName() + " is not playable", Toast.LENGTH_SHORT);
                                shortenDurationOfToast(toast, 800);
                                toast.show();
                            }
                        });
                   /*     LiveData<List<Game>> stagesPlayerHasWonOn = gameViewModel.getStagesPlayerHasWonOn();

                        for (Game game: stagesPlayerHasWonOn.getValue()) {
                            // (int i = 0; i < stagesPlayerHasWonOn.getValue().length; i++)
                            game.getStagePlayedOn();
                        }*/
                    }
                }
            });
        }
        return rootView;
    }

    private void startCircularRevealAnimation(final View revealView, View touchedStageCardView,
                                              final Drawable stageDrawable, MotionEvent actionUpMotionEvent) {
        // Common animator parameters:
        int startX = (int) actionUpMotionEvent.getX();
        int startY = (int) actionUpMotionEvent.getY();
        float outerRadius = (float) Math.hypot(touchedStageCardView.getWidth(), touchedStageCardView.getHeight());

        // Striking stuff:
        circularStrikeAnimator = ViewAnimationUtils.createCircularReveal(
                revealView,
                startX,
                startY,
                0f,
                outerRadius);
        circularStrikeAnimator.setInterpolator(new DecelerateInterpolator());
        circularStrikeAnimator.setDuration(325);
        circularStrikeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                revealView.setVisibility(View.VISIBLE);

                // These need to occur at the beginning of the animation to prevent the user from
                // potentially striking the last remaining stage immediately after striking the second last stage.
                queryVisibleRevealViews();
                checkStageStrikingCompletion();
            }
        });

        // Strike removal stuff:
        circularStrikeRemovalAnimator = ViewAnimationUtils.createCircularReveal(
                revealView,
                startX,
                startY,
                outerRadius,
                0f);
        circularStrikeRemovalAnimator.setInterpolator(new AccelerateInterpolator());
        circularStrikeRemovalAnimator.setDuration(325);
        circularStrikeRemovalAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revealView.setVisibility(View.INVISIBLE);
                queryVisibleRevealViews();
                checkStageStrikingCompletion();
            }
        });

        // Setup scenarios for stageButton clicking behaviour based on the Activity context
        // Behaviour within MainActivity:
        if (getActivity().getClass() == MainActivity.class) {
            if (revealView.getVisibility() == View.INVISIBLE && getRemainingStageName() == null) {
                // 1. User is striking an unstriked stage (not including the last stage)
                circularStrikeAnimator.start();
            } else if (revealView.getVisibility() == View.INVISIBLE && getRemainingStageName() != null) {
                // 2. User is clicking the remaining stage --> Don't start the strike animation
                Toast toast = Toast.makeText(getContext(), "Game 1 will proceed on " + getRemainingStageName(), Toast.LENGTH_SHORT);
                shortenDurationOfToast(toast, 800);
                toast.show();
            } else // 3. User is removing the strike from a striked stage
                circularStrikeRemovalAnimator.start();
        }
        // Behaviour within StageSelectionActivity:
        else if (getActivity().getClass() == StageSelectionActivity.class) {
            if (revealView.getVisibility() == View.INVISIBLE && !stageSelectionViewModel.hasPlayerBanned()) {
                // 1. Winner is "banning" a stage
                circularStrikeAnimator.start();
                stageSelectionViewModel.setPlayerHasBanned(true);
                revealRadioButtons();
            } else if (revealView.getVisibility() == View.INVISIBLE) {
                // 2. Loser is picking a stage to for the next game
                stageSelectionViewModel.isStartButtonEnabled().setValue(true);
                int indexOfMyView = ((GridLayout) touchedStageCardView.getParent()).indexOfChild(touchedStageCardView);
                stageSelectionViewModel.setChosenStageIndex(indexOfMyView);
                stageSelectionViewModel.setChosenStageName(stageStrikingViewModel.stages.getStage(stageSelectionViewModel.getChosenStageIndex()).getStageName());
                // Toast toast = Toast.makeText(getContext(), "This Game will proceed on " + stageSelectionViewModel.getChosenStageName(), Toast.LENGTH_SHORT);
                // shortenDurationOfToast(toast, 800);
                // toast.show();
                RadioButton radioButton = touchedStageCardView.findViewById(R.id.stage_button_layout_radioButton);
                checkAndUncheckAppropriateRadioButtons((CardView) touchedStageCardView);
                Log.v(TAG, "This Game will proceed on " + stageSelectionViewModel.getChosenStageName());
            } else { // 3. Stage that was just banned is being unbanned
                circularStrikeRemovalAnimator.start();
                stageSelectionViewModel.setPlayerHasBanned(false);
                stageSelectionViewModel.isStartButtonEnabled().setValue(false);
                // checkAllRadioButtons(); // This line is here to have a nice fill animation before all the RadioButtons are hidden
                uncheckAllRadioButtons();
                hideRadioButtons();
            }
        }
    }

    public void unstrikeAllStarterStages() {
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            stageStrikingViewModel.stages.getStage(i).setStageStriked(false);
            CardView stageCardView = (CardView) stageStrikingGridLayout.getChildAt(i);
            CardView revealView = stageCardView.findViewById(R.id.reveal_view);
            if (stageStrikingViewModel.stages.getStage(i).isStarterStage())
                revealView.setVisibility(View.INVISIBLE);
            setRemainingStageName(null);
            Log.v(TAG, "All starter stages have been unstriked");
        }
    }

    // Counts the number of stages striked and updates the ViewModel
    private void queryVisibleRevealViews() {
        setNumberOfVisibleRevealViews(0); // Reset number so that we can start counting from zero again
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            if ((stageStrikingGridLayout.getChildAt(i)).findViewById(R.id.reveal_view).getVisibility() == View.VISIBLE) {
                setNumberOfVisibleRevealViews(getNumberOfVisibleRevealViews() + 1);
                stageStrikingViewModel.stages.getStage(i).setStageStriked(true);
            } else stageStrikingViewModel.stages.getStage(i).setStageStriked(false);
        }
        Log.v(TAG, "Number of visible revealViews: " + getNumberOfVisibleRevealViews());
    }

    // Identifies the last remaining stage (name and index) and updates the ViewModel
    public void identifyRemainingStage() {
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            if ((stageStrikingGridLayout.getChildAt(i)).findViewById(R.id.reveal_view).getVisibility() == View.INVISIBLE
                    && stageStrikingViewModel.stages.getStageList().length - getNumberOfVisibleRevealViews() <= 1) {
                setRemainingStageIndex(i);
                setRemainingStageName(stageStrikingViewModel.stages.getStage(i).getStageName());
                Log.v(TAG, "Remaining stage: " + getRemainingStageName() + " at index " + i);
            }
        }
    }

    // Reveals RadioButtons on viable stages
    // Should only be used when in the StageSelectionActivity
    public void revealRadioButtons() {
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            CardView stageCardViewAtI = (CardView) stageStrikingGridLayout.getChildAt(i);
            RadioButton radioButton = stageCardViewAtI.findViewById(R.id.stage_button_layout_radioButton);
            if (stageCardViewAtI.findViewById(R.id.reveal_view).getVisibility() == View.INVISIBLE && radioButton.getVisibility() == View.GONE)
                radioButton.setVisibility(View.VISIBLE);
        }
    }

    // Hides RadioButtons on viable stages
    // Should only be used when in the StageSelectionActivity
    public void hideRadioButtons() {
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            CardView stageCardViewAtI = (CardView) stageStrikingGridLayout.getChildAt(i);
            final RadioButton radioButton = stageCardViewAtI.findViewById(R.id.stage_button_layout_radioButton);
            radioButton.setVisibility(View.GONE);
        }
    }

    // Checks the radioButton of the touchedStageCardView and unchecks all other radioButtons
    public void checkAndUncheckAppropriateRadioButtons(CardView touchedStageCardView) {
        RadioButton radioButtonOfTouchedStageCardView = touchedStageCardView.findViewById(R.id.stage_button_layout_radioButton);
        if (!radioButtonOfTouchedStageCardView.isChecked()) {
            for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
                CardView stageCardViewAtI = (CardView) stageStrikingGridLayout.getChildAt(i);
                RadioButton radioButton = stageCardViewAtI.findViewById(R.id.stage_button_layout_radioButton);
                radioButton.setChecked(false);
            }
        }
        radioButtonOfTouchedStageCardView.setChecked(true);
    }

    // Checks all RadioButtons
    public void checkAllRadioButtons() {
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            CardView stageCardViewAtI = (CardView) stageStrikingGridLayout.getChildAt(i);
            RadioButton radioButton = stageCardViewAtI.findViewById(R.id.stage_button_layout_radioButton);
            radioButton.setChecked(true);
        }
    }

    // Unchecks all RadioButtons
    public void uncheckAllRadioButtons() {
        for (int i = 0; i < stageStrikingViewModel.stages.getStageList().length; i++) {
            CardView stageCardViewAtI = (CardView) stageStrikingGridLayout.getChildAt(i);
            RadioButton radioButton = stageCardViewAtI.findViewById(R.id.stage_button_layout_radioButton);
            radioButton.setChecked(false);
        }
    }

    // Call this after queryVisibleRevealViews()
    public boolean checkStageStrikingCompletion() {
        // Step is completed once only 1 stage remains
        if (stageStrikingViewModel.stages.getStageList().length - getNumberOfVisibleRevealViews() <= 1) {
            if (getActivity().getClass() == MainActivity.class)
                mainViewModel.isStageStrikingStepCompleted().setValue(true);
            identifyRemainingStage();
            return true;
        } else {
            // Mark step as uncompleted and reset remaining stage state
            if (getActivity().getClass() == MainActivity.class)
                mainViewModel.isStageStrikingStepCompleted().setValue(false);
            if (getRemainingStageName() != null) {
                setRemainingStageName(null);
                Log.v(TAG, "Remaining stage was removed. Please continue striking stages until only one stage remains.");
            }
        }
        return false;
    }

    // Should only be used when in the MainActivity --> See implementation in onCreate()
    public String createStrikingOrder(String firstStageStrikePlayerString, String
            portSelectionPlayerString) {
        String[] arrayFirstStageStrikePlayerString = firstStageStrikePlayerString.split(" ", 2);
        String shortenedFirstStageStrikePlayerString = arrayFirstStageStrikePlayerString[1];
        String[] arrayPortSelectionPlayerString = portSelectionPlayerString.split(" ", 2);
        String shortenedPortSelectionPlayerString = arrayPortSelectionPlayerString[1];
        return "Striking order: " + shortenedFirstStageStrikePlayerString + " > " + shortenedPortSelectionPlayerString + " > " + shortenedPortSelectionPlayerString + " > " + shortenedFirstStageStrikePlayerString;
    }

    // Displays the instruction and striking order for this step
    // Should only be used when in the MainActivity --> See implementation in onCreate()
    public void displayText(View rootView, String instructions, String strikingOrder) {
        TextView instructionsTextView = rootView.findViewById(R.id.stage_striking_step_instructions_text_view);
        instructionsTextView.setVisibility(View.VISIBLE);
        instructionsTextView.setText(instructions);
        TextView strikingOrderTextView = rootView.findViewById(R.id.stage_striking_step_striking_order_text_view);
        strikingOrderTextView.setVisibility(View.VISIBLE);
        strikingOrderTextView.setText(strikingOrder);
    }

    public void quickGrayscale(Drawable drawable) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        drawable.setColorFilter(colorMatrixColorFilter);
    }

    public void quickSaturate(Drawable drawable) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(1);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        drawable.setColorFilter(colorMatrixColorFilter);
    }

    public void animateGrayscaleConversion(final Drawable drawable) {
        final ColorMatrix colorMatrix = new ColorMatrix();
        // For some reason can pass in 0 or 1 and result is the same...
        colorMatrix.setSaturation(0);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(625);
        valueAnimator.setRepeatCount(1);
        valueAnimator.setCurrentFraction(1f);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        // TODO Use LinearInterpolator until you can confirm if REVERSE alters the interpolator too
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                colorMatrix.setSaturation(valueAnimator.getAnimatedFraction());
                ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                drawable.setColorFilter(colorMatrixColorFilter);
            }
        });
        valueAnimator.start();
    }

    public void animateSaturationConversion(final Drawable drawable) {
        final ColorMatrix colorMatrix = new ColorMatrix();
        // For some reason can pass in 0 or 1 and result is the same...
        colorMatrix.setSaturation(0);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(625);
        // valueAnimator.setRepeatCount(1);
        // valueAnimator.setCurrentFraction(1f);
        // valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        // TODO Use LinearInterpolator until you can confirm if REVERSE alters the interpolator too
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                colorMatrix.setSaturation(valueAnimator.getAnimatedFraction());
                ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                drawable.setColorFilter(colorMatrixColorFilter);
            }
        });
        valueAnimator.start();
    }

    // Decreases the duration of the toast message to be ever shorter than LENGTH_SHORT (2.5s)
    public void shortenDurationOfToast(final Toast toast, long delayMillis) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, delayMillis);
    }
}


