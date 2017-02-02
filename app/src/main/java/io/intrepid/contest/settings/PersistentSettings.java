package io.intrepid.contest.settings;

import java.util.UUID;

public interface PersistentSettings {
    String getAuthenticationToken();

    void setAuthenticationToken(String authenticationToken);

    UUID getCurrentContestId();

    void setCurrentContestId(UUID currentContestId);
}