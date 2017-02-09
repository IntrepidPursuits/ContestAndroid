package io.intrepid.contest.models;

import android.support.annotation.StringRes;

public class ScoreWeight {

    private int weightValue;
    @StringRes
    private int weightName;

    public ScoreWeight(int weight, @StringRes int weightName) {
        this.weightValue = weight;
        this.weightName = weightName;
    }

    public int getWeightValue() {
        return weightValue;
    }

    public int getWeightName() {
        return weightName;
    }
}
