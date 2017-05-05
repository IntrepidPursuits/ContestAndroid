package io.intrepid.contest.screens.contestcreation;

import io.intrepid.contest.models.Contest;

public interface EditContestContract {

    void onNextPageEnabledChanged(boolean enabled);

    void showAddCategoryScreen();

    Contest.Builder getContestBuilder();

    void showNextScreen();
}
