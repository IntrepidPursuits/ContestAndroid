package io.intrepid.contest.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

public class SharedPreferencesManager implements PersistentSettings {

    private static final String AUTHENTICATION_TOKEN = "authentication_token";
    private static final String CURRENT_CONTEST_ID = "current_contest_id";

    private static SharedPreferencesManager instance;
    private final SharedPreferences preferences;

    private SharedPreferencesManager(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    private SharedPreferencesManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static PersistentSettings getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    @Override
    public String getAuthenticationToken() {
        return preferences.getString(AUTHENTICATION_TOKEN, "");
    }

    @Override
    public void setAuthenticationToken(String authenticationToken) {
        preferences.edit().putString(AUTHENTICATION_TOKEN, authenticationToken).apply();
    }

    @Override
    public UUID getCurrentContestId() {
        return UUID.fromString(preferences.getString(CURRENT_CONTEST_ID, ""));
    }

    @Override
    public void setCurrentContestId(UUID currentContestId) {
        preferences.edit().putString(CURRENT_CONTEST_ID, currentContestId.toString()).apply();
    }
}