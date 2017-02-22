package io.intrepid.contest.settings;

import java.util.UUID;

import io.intrepid.contest.models.ParticipationType;

public interface PersistentSettings {
    boolean isAuthenticated();

    String getAuthenticationToken();

    void setAuthenticationToken(String authenticationToken);

    UUID getCurrentContestId();

    void setCurrentContestId(UUID currentContestId);

    ParticipationType getCurrentParticipationType();

    void setCurrentParticipationType(ParticipationType participationType);

    void clear();
}