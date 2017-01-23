package io.intrepid.contest.base;

import android.support.annotation.NonNull;

import io.intrepid.contest.rest.RestApi;

/**
 * Wrapper class for common dependencies that all presenters are expected to have
 */
public class PresenterConfiguration {
    @NonNull
    private final RestApi restApi;


    public PresenterConfiguration(@NonNull RestApi restApi) {
        this.restApi = restApi;
    }

    @NonNull
    public RestApi getRestApi() {
        return restApi;
    }
}
