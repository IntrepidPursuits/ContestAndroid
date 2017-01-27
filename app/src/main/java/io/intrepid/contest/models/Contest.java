package io.intrepid.contest.models;

import android.os.Parcel;

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
    private Date endedDate;
    private String title;
    private String description;
    private List<Category> categories;

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof Contest) || (obj instanceof Contest.Builder)
                && this.contestId == ((Contest) obj).contestId
                && this.creatorId == ((Contest) obj).creatorId
                && this.creationDate == ((Contest) obj).creationDate);
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

    public static class Builder {
        public static final int UUID_MIN_LIMIT = 0;
        public static final int UUID_MAX_LIMIT = Integer.MAX_VALUE;
        public List<Category> categories = new ArrayList<>();
        public UUID contestId;
        public UUID creatorId;
        public Date creationDate;
        public Date lastUpdatedDate;
        public Date endedDate;
        public String title;
        public String description;

        //Intended use - for editing an existing contest. May be overkill
        public Builder(Contest contest) {
            this.contestId = contest.contestId;
            this.creatorId = contest.creatorId;
            this.lastUpdatedDate = contest.lastUpdatedDate;
            this.endedDate = contest.endedDate;
            this.creationDate = contest.creationDate;
            this.title = contest.title;
            this.description = contest.description;
            this.categories = contest.categories;
        }

        public Builder() {
            this(new UUID(UUID_MIN_LIMIT, UUID_MAX_LIMIT));
        }

        public Builder(UUID creatorId) {  //fixme
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

        public Builder addCategory(Category category) {
            this.categories.add(category);
            return this;
        }

        public Builder removeCategory(Category category) {
            this.categories.remove(category);
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

    private static class ContestMalFormedException extends IllegalArgumentException {
        private static final String CONTEST_ERROR_MSG = "Please specify all required Contest fields";

        public ContestMalFormedException() {
            super(CONTEST_ERROR_MSG);
        }
    }
}
