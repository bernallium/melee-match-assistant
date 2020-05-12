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

import com.example.android.meleematchassistant.PlayersDatabase;
import com.example.android.meleematchassistant.PlayersDatabaseImpl;
import com.example.android.meleematchassistant.R;
import com.example.android.meleematchassistant.ViewModels.MainViewModel;
import com.example.android.meleematchassistant.ViewModels.RockPaperScissorsViewModel;
import com.example.android.meleematchassistant.model.Players;

/**
 * A simple {@link Fragment} subclass.
 */
public class RockPaperScissorsStepFragment extends Fragment {

    private static final String TAG = RockPaperScissorsStepFragment.class.getSimpleName();

    private PlayersDatabase playersDatabase = new PlayersDatabaseImpl();
    private MainViewModel mainViewModel;
    private RockPaperScissorsViewModel rPSViewModel;

    public RockPaperScissorsStepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_rock_paper_scissors_step, container, false);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        rPSViewModel = ViewModelProviders.of(getActivity()).get(RockPaperScissorsViewModel.class);

        final RadioButton playerARadioButton = rootView.findViewById(R.id.player_a_radio_button);
        final RadioButton playerBRadioButton = rootView.findViewById(R.id.player_b_radio_button);

        // Create observer > Observe some LiveData > Update the UI upon change
        rPSViewModel.wasPlayerARPSWinnerChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean wasChecked) {
                if (wasChecked) {
                    playerARadioButton.setChecked(true);
                } else playerARadioButton.setChecked(false);
            }
        });
        rPSViewModel.wasPlayerBRPSWinnerChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean wasChecked) {
                if (wasChecked) {
                    playerBRadioButton.setChecked(true);
                } else playerBRadioButton.setChecked(false);
            }
        });

        // Setup onCheckedChangeListener behaviour
        RadioGroup radioGroup = rootView.findViewById(R.id.rock_paper_scissors_step_radio_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                playersDatabase.setRPSWinnerId(queryCheckedWinner(checkedId));
                playersDatabase.setRPSLoserId(queryUncheckedLoser(checkedId));
                mainViewModel.isRPSStepCompleted().setValue(true); // This step is complete once the user checks one of the RadioButtons
            }
        });
        return rootView;
    }

    // Queries which player won the RPS match and returns that player's ID
    public int queryCheckedWinner(int checkedId) {
        int rPSWinnerId;
        if (checkedId == R.id.player_a_radio_button) {
            rPSWinnerId = Players.PLAYER_A_ID;
            rPSViewModel.wasPlayerARPSWinnerChecked().setValue(true);
        } else {
            rPSWinnerId = Players.PLAYER_B_ID;
            rPSViewModel.wasPlayerBRPSWinnerChecked().setValue(true);
        }
        Log.v(TAG, "RPS Winner: " + playersDatabase.getPlayerStringFromId(rPSWinnerId));
        return rPSWinnerId;
    }

    // Queries which player lost the RPS match and returns that player's ID
    public int queryUncheckedLoser(int checkedId) {
        int rPSLoserId;
        if (checkedId == R.id.player_a_radio_button) {
            rPSLoserId = Players.PLAYER_B_ID;
            rPSViewModel.wasPlayerBRPSWinnerChecked().setValue(false);
        } else {
            rPSLoserId = Players.PLAYER_A_ID;
            rPSViewModel.wasPlayerARPSWinnerChecked().setValue(false);
        }
        return rPSLoserId;
    }
}
