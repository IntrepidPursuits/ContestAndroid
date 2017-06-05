package io.intrepid.contest.rest;

import java.util.List;

import io.intrepid.contest.models.Adjudication;
import io.intrepid.contest.models.EntryBallot;

public class AdjudicateRequest {
    public Adjudication adjudication;

    public AdjudicateRequest(List<EntryBallot> entryBallots) {
        adjudication = new Adjudication();
        adjudication.setEntryBallots(entryBallots);
    }
}
