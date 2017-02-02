package io.intrepid.contest.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;

public class SharedPreferencesManager implements UserSettings {

    private static final String AUTHENTICATION_TOKEN = "authentication_token";

    private static SharedPreferencesManager instance;
    private final SharedPreferences preferences;

    private SharedPreferencesManager(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    private SharedPreferencesManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static UserSettings getInstance(Context context) {
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
}
