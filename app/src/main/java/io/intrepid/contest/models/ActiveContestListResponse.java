package io.intrepid.contest.models;

import java.util.List;

public class ActiveContestListResponse {
    private List<Contest> contests;

    public ActiveContestListResponse(List<Contest> contests) {
        this.contests = contests;
    }

    public List<Contest> getContests() {
        return contests;
    }
}
