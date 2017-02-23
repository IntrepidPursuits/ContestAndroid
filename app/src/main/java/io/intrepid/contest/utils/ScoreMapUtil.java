package io.intrepid.contest.utils;

import android.support.annotation.StringRes;

import io.intrepid.contest.R;

public class ScoreMapUtil {

    @StringRes
    public static int getRatingTextForScore(int score) {
        switch (score) {
            case 1:
                return R.string.poor;
            case 2:
                return R.string.average;
            case 3:
                return R.string.good;
            case 4:
                return R.string.great;
            case 5:
                return R.string.excellent;
        }
        return R.string.common_error;
    }
}
