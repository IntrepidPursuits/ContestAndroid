package io.intrepid.contest.models;

import com.google.gson.annotations.SerializedName;

public enum ParticipationType {
    CREATOR,
    @SerializedName("contestant")
    CONTESTANT,
    @SerializedName("judge")
    JUDGE;
}
