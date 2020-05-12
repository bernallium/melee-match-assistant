package com.example.android.meleematchassistant.model;

import com.example.android.meleematchassistant.R;

public class Stages {

    private Stage[] stageList;

    public Stage[] getStageList() {
        return stageList;
    }

    public Stage getStage(int stageId) {
        return stageList[stageId];
    }

    // TODO Use string resources for stage names
    // Stages constructor
    public Stages() {
        stageList = new Stage[6];

        stageList[0] = new Stage(
                "Battlefield",
                R.drawable.battlefield,
                true,
                true,
                false);

        stageList[1] = new Stage(
                "Yoshi's Story",
                R.drawable.yoshis_story,
                true,
                true,
                false);

        stageList[2] = new Stage(
                "Dream Land",
                R.drawable.dream_land,
                true,
                true,
                false);

        stageList[3] = new Stage(
                "Fountain of Dreams",
                R.drawable.fountain_of_dreams,
                true,
                true,
                false);

        stageList[4] = new Stage(
                "Final Destination",
                R.drawable.final_destination,
                true,
                true,
                false);

        stageList[5] = new Stage(
                "Pokemon Stadium",
                R.drawable.pokemon_stadium,
                false,
                true,
                false);
    }

    // Stage innerclass
    public class Stage {

        private final String stageName;
        private final int stageImageResource;
        private final boolean starterStage;
        private boolean stagePlayable;
        private boolean stageStriked;

        public String getStageName() {
            return stageName;
        }

        public int getStageImageResource() {
            return stageImageResource;
        }

        public boolean isStarterStage() {
            return starterStage;
        }

        public boolean isStagePlayable() {
            return stagePlayable;
        }

        public void setStagePlayable(boolean stagePlayable) {
            this.stagePlayable = stagePlayable;
        }

        public boolean isStageStriked() {
            return stageStriked;
        }

        public void setStageStriked(boolean stageStriked) {
            this.stageStriked = stageStriked;
        }

        // Stage constructor
        public Stage(String stageName, int stageImageResource, boolean starterStage, boolean stagePlayable, boolean stageStriked) {
            this.stageName = stageName;
            this.stageImageResource = stageImageResource;
            this.starterStage = starterStage;
            this.stagePlayable = stagePlayable;
            this.stageStriked = stageStriked;
        }
    }
}
