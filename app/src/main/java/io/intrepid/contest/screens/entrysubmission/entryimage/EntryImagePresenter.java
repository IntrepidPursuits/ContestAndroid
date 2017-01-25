package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View;

public class EntryImagePresenter extends BasePresenter<View> implements Presenter {
    public EntryImagePresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
