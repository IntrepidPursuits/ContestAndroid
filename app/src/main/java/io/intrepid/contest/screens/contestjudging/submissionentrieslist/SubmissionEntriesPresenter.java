package io.intrepid.contest.screens.contestjudging.submissionentrieslist;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

import static io.intrepid.contest.screens.contestjudging.submissionentrieslist.SubmissionEntriesContract.View;

class SubmissionEntriesPresenter extends BasePresenter<SubmissionEntriesContract.View>
        implements SubmissionEntriesContract.Presenter {

    SubmissionEntriesPresenter(@NonNull View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
