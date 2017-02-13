package io.intrepid.contest.rest;

import io.intrepid.contest.models.Contest;

public class ContestWrapper {

    public Contest contest;

    public ContestWrapper(Contest contest) {
        this.contest = contest;
    }
}
