package io.intrepid.contest.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Adjudication {
    @SerializedName("entries")
    public List<EntryBallot> entryBallots;
}
