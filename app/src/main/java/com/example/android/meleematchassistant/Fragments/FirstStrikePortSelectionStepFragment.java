package com.example.android.meleematchassistant.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.android.meleematchassistant.PlayersDatabase;
import com.example.android.meleematchassistant.PlayersDatabaseImpl;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.FSPSViewModel;
import com.example.android.meleematchassistant.ViewModels.MainViewModel;
import com.example.android.meleematchassistant.model.Players;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstStrikePortSelectionStepFragment extends Fragment {

    private static final String TAG = FirstStrikePortSelectionStepFragment.class.getSimpleName();

    private OnCheckedChangeListener onCheckedChangeListener;
    private PlayersDatabase playersDatabase = new PlayersDatabaseImpl();
    private MainViewModel mainViewModel;
    private FSPSViewModel fSPSViewModel;

    private TextView instructionsTextView;



    public FirstStrikePortSelectionStepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_first_strike_port_selection_step, container, false);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        fSPSViewModel = ViewModelProviders.of(getActivity()).get(FSPSViewModel.class);

        final RadioGroup radioGroup = rootView.findViewById(R.id.first_strike_port_selection_step_radio_group);
        final RadioButton firstStrikeRadioButton = rootView.findViewById(R.id.first_strike_radio_button);
        final RadioButton portSelectionRadioButton = rootView.findViewById(R.id.port_selection_radio_button);

        // Create observer > Observe some LiveData > Update the UI upon change
        mainViewModel.isRPSStepCompleted().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean stepCompleted) {

                instructionsTextView = rootView.findViewById(R.id.first_strike_port_selection_step_instructions_text_view);
                instructionsTextView.setText(createInstructions());
                // Even though the rPSStepCompletion value gets updated (to the same value) when the device is rotated,
                // the user should not expect the radioGroup to get cleared. Therefore enable this state only when this step is open (do so in MainActivity)
                if (fSPSViewModel.isCanClearCheckedRadioGroup()) clearCheckedRadioGroup(radioGroup, onCheckedChangeListener);
            }
        });

        fSPSViewModel.wasFirstStrikeChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean wasChecked) {
                radioGroup.setOnCheckedChangeListener(null);
                if (wasChecked) {
                    firstStrikeRadioButton.setChecked(true);
                } else firstStrikeRadioButton.setChecked(false);
                radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });
        fSPSViewModel.wasPortSelectionChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean wasChecked) {
                radioGroup.setOnCheckedChangeListener(null);
                if (wasChecked) {
                    portSelectionRadioButton.setChecked(true);
                } else portSelectionRadioButton.setChecked(false);
                radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });

        // Setup onCheckedChangeListener behaviour
        onCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                playersDatabase.setChosenChoiceId(queryCheckedOption(checkedId));
                playersDatabase.setUnchosenChoiceId(queryUncheckedOption(checkedId));
                mainViewModel.isFSPSStepCompleted().setValue(true); // This step is complete once the user checks one of the RadioButtons

            }
        };
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        return rootView;
    }

    // Clears the checked state of the RadioGroup (without triggering the listener)
    public void clearCheckedRadioGroup(RadioGroup radioGroup, OnCheckedChangeListener onCheckedChangeListener) {
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup.clearCheck();
        fSPSViewModel.wasFirstStrikeChecked().setValue(false);
        fSPSViewModel.wasPortSelectionChecked().setValue(false);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        mainViewModel.isFSPSStepCompleted().setValue(Boolean.FALSE);
        Log.v(TAG, "RadioGroup cleared");
    }

    // Queries which option is checked and returns that option's ID
    public int queryCheckedOption(int checkedId) {
        int checkedOptionId;
        if (checkedId == R.id.first_strike_radio_button) {
            checkedOptionId = Players.FIRST_STRIKE_OPTION_ID;
            fSPSViewModel.wasFirstStrikeChecked().setValue(true);
            Log.v(TAG, getString(R.string.first_strike_button_string) + " selected");
        } else {
            checkedOptionId = Players.PORT_SELECTION_OPTION_ID;
            fSPSViewModel.wasPortSelectionChecked().setValue(true);
            Log.v(TAG, getString(R.string.port_selection_button_string) + " selected");
        }
        return checkedOptionId;
    }

    // Queries which option is unchecked and returns that option's ID
    public int queryUncheckedOption(int checkedId) {
        int uncheckedOptionId;
        if (checkedId == R.id.first_strike_radio_button) {
            uncheckedOptionId = Players.PORT_SELECTION_OPTION_ID;
            fSPSViewModel.wasPortSelectionChecked().setValue(false);
        } else {
            uncheckedOptionId = Players.FIRST_STRIKE_OPTION_ID;
            fSPSViewModel.wasFirstStrikeChecked().setValue(false);
        }
        return uncheckedOptionId;
    }

    // Create the instructions for this step
    public String createInstructions() {
        String instructions = String.format("%s %s.\n%s %s",
                playersDatabase.getPlayerStringFromId(playersDatabase.getRPSWinnerId()), getString(R.string.first_strike_port_selection_step_instructions_rps_winner),
                playersDatabase.getPlayerStringFromId(playersDatabase.getRPSLoserId()), getString(R.string.first_strike_port_selection_step_instructions_rps_loser));
        return instructions;
    }
}