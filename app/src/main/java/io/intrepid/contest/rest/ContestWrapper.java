package io.intrepid.contest.rest;

import com.google.gson.annotations.SerializedName;

import io.intrepid.contest.models.Contest;

public class ContestWrapper {

    @SerializedName("contest")
    public Contest contest;

    public ContestWrapper(Contest contest) {
        this.contest = contest;
    }
}
