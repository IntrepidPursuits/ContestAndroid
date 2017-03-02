package io.intrepid.contest.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

public class Contest {
    @SerializedName("id")
    private UUID contestId;
    private UUID creatorId;
    @SerializedName("created_at")
    private Date creationDate;
    @SerializedName("updated_at")
    private Date lastUpdatedDate;
    @SerializedName("ended_at")
    private Date endedDate;
    private String title;
    private String description;
    @SerializedName("scoring_categories")
    private List<Category> categories;
    private List<Entry> entries;

    public Contest() {
        categories = new ArrayList<>();
    }

    @Override
    public String toString() {
        return title + "/n" +
                description + "/n" +
                contestId + "/n" +
                creatorId + " " + creationDate +
                " /n size of categories was " + categories.size() +
                "categories " + categories +
                " /n Last updated " + lastUpdatedDate +
                " /n Ended Data " + endedDate;
    }

    public UUID getId() {
        return contestId;
    }

    public void setId(UUID id) {
        this.contestId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void setEndedData(Date endedData) {
        this.endedDate = endedData;
    }

    public static class Builder {
        static final int UUID_MIN_LIMIT = 0;
        static final int UUID_MAX_LIMIT = Integer.MAX_VALUE;
        public List<Category> categories = new ArrayList<>();
        public String title;
        public String description;
        UUID contestId;
        UUID creatorId;
        Date creationDate;
        Date lastUpdatedDate;
        Date endedDate;

        public Builder() {
            this(new UUID(UUID_MIN_LIMIT, UUID_MAX_LIMIT));
        }

        Builder(UUID creatorId) {
            this.creatorId = creatorId;
            this.contestId = new UUID(UUID_MIN_LIMIT, UUID_MAX_LIMIT);
        }

        protected Builder(Parcel in) {
            title = in.readString();
            description = in.readString();
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Contest build() {
            if (creationDate == null) {
                creationDate = new Date();
            }
            if (lastUpdatedDate == null) {
                lastUpdatedDate = new Date();
            }
            Timber.d(this.toString());
            Contest contest = new Contest();
            contest.contestId = this.contestId;
            contest.creatorId = this.creatorId;
            contest.lastUpdatedDate = new Date();
            contest.creationDate = this.creationDate;
            contest.endedDate = this.endedDate;
            contest.title = this.title;
            contest.description = this.description;
            contest.categories = this.categories;
            return contest;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public Builder setCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }
    }
}
