package io.intrepid.contest.rest;

public class ContestStatus {
    private boolean submissionsEnded;
    private int entriesSubmitted;
    private int totalEntries;

    private boolean contestEnded;
    private int judgesSubmitted;
    private int totalJudges;

    public void setSubmissionData(boolean submissionsEnded, int entriesSubmitted, int totalEntries) {
        this.submissionsEnded = submissionsEnded;
        this.entriesSubmitted = entriesSubmitted;
        this.totalEntries = totalEntries;
    }

    public void setJudgeData(boolean contestEnded, int judgesSubmitted, int totalJudges) {
        this.contestEnded = contestEnded;
        this.judgesSubmitted = judgesSubmitted;
        this.totalJudges = totalJudges;
    }

    public int getNumSubmissionsMissing() {
        return totalEntries - entriesSubmitted;
    }

    public int getNumScoresMissing() {
        return totalJudges - judgesSubmitted;
    }

    public boolean haveSubmissionsEnded() {
        return submissionsEnded;
    }

    public boolean hasContestEnded() {
        return contestEnded;
    }
}
