package io.intrepid.contest.base;

import android.support.annotation.StringRes;

public interface TextValidatableView {

    boolean areAllFieldValid();

    @StringRes
    int errorMessage();

    void submitText();
}
