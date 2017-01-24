package io.intrepid.contest.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    private String name;
    private String description;
    private float scoringWeight;

    protected Category(Parcel in) {
        name = in.readString();
        description = in.readString();
        scoringWeight = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(scoringWeight);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
