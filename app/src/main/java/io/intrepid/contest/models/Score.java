package io.intrepid.contest.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Score {
    private int score;
    @SerializedName("scoring_category_id")
    private UUID categoryId;
    private transient String categoryName;
    private transient String categoryDescription;

    public Score(Category category, int score) {
        this.categoryId = category.getId();
        this.categoryName = category.getName();
        this.categoryDescription = category.getDescription();
        this.score = score;
    }

    public int getScoreValue() {
        return score;
    }

    void setScoreValue(int scoreValue) {
        this.score = scoreValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Score)) {
            return false;
        }
        Score other = (Score) obj;
        return score == other.score;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }
}
