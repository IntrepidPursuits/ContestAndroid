package io.intrepid.contest.screens.contestjudging.expandablerecycler;

import android.support.annotation.StringRes;

interface ExpandableViewHolder {

    void toggle();

    boolean isExpanded();

    void showTemporaryScoreMessage(@StringRes int message);
}
