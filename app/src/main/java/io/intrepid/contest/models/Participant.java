package io.intrepid.contest.models;

import java.util.UUID;

public class Participant {
    private ParticipationType participationType;
    private UUID contestId;

    public ParticipationType getParticipationType() {
        return participationType;
    }

    public void setParticipationType(ParticipationType participationType) {
        this.participationType = participationType;
    }

    public UUID getContestId() {
        return contestId;
    }

    public void setContestId(UUID contestId) {
        this.contestId = contestId;
    }
}
