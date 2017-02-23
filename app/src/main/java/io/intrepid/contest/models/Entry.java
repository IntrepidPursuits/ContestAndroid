package io.intrepid.contest.models;

import java.util.Date;
import java.util.UUID;

public class Entry{
    public UUID id;
    public String title;
    public Date createdAt;
    public Date updatedAt;
    public String photoUrl;
    private float ratingAverage;

    public Entry() {
        id = UUID.randomUUID();
    }

    public float getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(float ratingAverage) {
        this.ratingAverage = ratingAverage;
    }
}
