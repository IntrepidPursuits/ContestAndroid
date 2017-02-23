package io.intrepid.contest.models;

import com.google.gson.annotations.SerializedName;

public enum ParticipationType {
    CREATOR("CREATOR"),
    @SerializedName("contestant")
    CONTESTANT("CONTESTANT"),
    @SerializedName("judge")
    JUDGE("JUDGE");

    private final String value;

    ParticipationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
