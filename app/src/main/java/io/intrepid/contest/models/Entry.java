package io.intrepid.contest.models;

import android.util.SparseIntArray;

import java.util.Date;
import java.util.UUID;

public class Entry {
    public UUID id;
    public String title;
    public Date createdAt;
    public Date updatedAt;
    public String photoUrl;
    private transient SparseIntArray mapOfIndexToScore = new SparseIntArray();
    private transient int categoriesSize;

    public Entry() {
        id = UUID.randomUUID();
    }

    public boolean isCompletelyScored() {
        return mapOfIndexToScore.size() == categoriesSize;
    }

    public int getRatingAverage() {
        int sum = 0;
        for (int index = 0; index < mapOfIndexToScore.size(); index++) {
            sum += mapOfIndexToScore.get(index);
        }
        return sum / categoriesSize;
    }

    public void setCategoriesSize(int categoriesSize) {
        this.categoriesSize = categoriesSize;
    }

    public void acceptScore(int position, int newRating) {
        mapOfIndexToScore.put(position, newRating);
    }
}
