package io.intrepid.contest.utils;

import android.support.annotation.Nullable;

import io.reactivex.disposables.Disposable;

class RxUtils {
    static void unsubscribeDisposable(@Nullable Disposable disposable) {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
