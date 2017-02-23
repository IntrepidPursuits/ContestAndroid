package io.intrepid.contest.rest;

import java.util.List;

import io.intrepid.contest.models.ContestResult;
import io.intrepid.contest.models.RankedEntryResult;

public class ContestResultResponse {
    public ContestResult contestResults;

    public ContestResultResponse() {
    }

    public ContestResultResponse(List<RankedEntryResult> rankedScoredEntries) {
        this.contestResults = new ContestResult();
        contestResults.rankedScoredEntries = rankedScoredEntries;
    }
}
