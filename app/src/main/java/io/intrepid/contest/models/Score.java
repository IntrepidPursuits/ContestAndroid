package io.intrepid.contest.models;

public class Score {
    private int score;
    private String categoryName;
    private String categoryDescription;

    public Score(Category category, int score) {
        this.categoryName = category.getName();
        this.categoryDescription = category.getDescription();
        this.score = score;
    }

    int getScoreValue() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Score)) {
            return false;
        }
        Score other = (Score) obj;
        return score == other.score;
    }

    void setScoreValue(int scoreValue) {
        this.score = scoreValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }
}
