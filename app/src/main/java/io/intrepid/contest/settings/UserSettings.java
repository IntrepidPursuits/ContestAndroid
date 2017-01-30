package io.intrepid.contest.settings;

public interface UserSettings {
    String getAuthenticationToken();

    void setAuthenticationToken(String authenticationToken);
}