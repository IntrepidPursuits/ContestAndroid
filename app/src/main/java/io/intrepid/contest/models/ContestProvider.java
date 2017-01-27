package io.intrepid.contest.models;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ContestProvider {

    private static ContestProvider instance;
    private static List<Contest> contests;
    private static Contest.Builder temporaryContest;

    private ContestProvider(Contest.Builder contest) {
        temporaryContest = contest;
    }

    public static ContestProvider getInstance() {
        if (instance == null) {
            temporaryContest = new Contest.Builder();
            instance = new ContestProvider(temporaryContest);
            contests = new ArrayList<>();
        }
        return instance;
    }

    public Contest.Builder getLastEditedContest() {
        return temporaryContest;
    }

    public void updateTemporaryContest(Contest.Builder contest) {
        Timber.d("Updating contest " + contest);
        temporaryContest = contest;
    }

    public void add(Contest contestSubmission) {
        Timber.d("Adding contest " + contestSubmission);
        contests.add(contestSubmission);
    }

    public void editDescription(String descriptionName) {
        temporaryContest.setDescription(descriptionName);
    }
}
