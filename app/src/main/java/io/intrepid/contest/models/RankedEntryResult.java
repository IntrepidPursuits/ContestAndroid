package io.intrepid.contest.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RankedEntryResult implements Parcelable {
    private String title;
    private String photoUrl;
    private float overallScore;
    private int rank;

    public RankedEntryResult(){

    }

    protected RankedEntryResult(Parcel in) {
        title = in.readString();
        photoUrl = in.readString();
        overallScore = in.readFloat();
        rank = in.readInt();
    }

    public static final Creator<RankedEntryResult> CREATOR = new Creator<RankedEntryResult>() {
        @Override
        public RankedEntryResult createFromParcel(Parcel in) {
            return new RankedEntryResult(in);
        }

        @Override
        public RankedEntryResult[] newArray(int size) {
            return new RankedEntryResult[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(float overallScore) {
        this.overallScore = overallScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(photoUrl);
        dest.writeFloat(overallScore);
        dest.writeInt(rank);
    }
}
