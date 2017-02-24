package io.intrepid.contest.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntryBallot {
    private final List<Score> scores = new ArrayList<>();
    private UUID entryId;

    public EntryBallot(UUID entryId) {
        this.entryId = entryId;
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public void setScore(int scoreIndex, int newRating) {
        Score score = scores.get(scoreIndex);
        score.setScoreValue(newRating);
    }

    public List<Score> getScores() {
        return scores;
    }

    public boolean isCompletelyScored() {
        for (Score score : scores) {
            if (score.getScoreValue() == 0) {
                return false;
            }
        }
        return true;
    }

    public float getScoreAverage() {
        int scoreSum = 0;
        for (Score score : scores) {
            scoreSum += score.getScoreValue();
        }
        return scoreSum / scores.size();
    }
}
