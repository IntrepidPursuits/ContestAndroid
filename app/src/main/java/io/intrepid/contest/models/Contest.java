package io.intrepid.contest.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

public class Contest {
    private UUID contestId;
    private UUID creatorId;
    private Date creationDate;
    private Date lastUpdatedDate;
    private String title;
    private String description;
    private List<Category> categories;

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
                " /n Last updated " + lastUpdatedDate;
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

        public Builder() {
            this(new UUID(UUID_MIN_LIMIT, UUID_MAX_LIMIT));
        }

        Builder(UUID creatorId) {
            this.creatorId = creatorId;
            this.contestId = new UUID(UUID_MIN_LIMIT, UUID_MAX_LIMIT);
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
            contest.title = this.title;
            contest.description = this.description;
            contest.categories = this.categories;
            return contest;
        }
    }
}
