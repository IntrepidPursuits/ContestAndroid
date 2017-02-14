package io.intrepid.contest.screens.contestjudging.scoresubmission;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;

import static io.intrepid.contest.screens.contestjudging.scoresubmission.SubmissionEntriesContract.View;

class SubmissionEntriesPresenter extends BasePresenter<SubmissionEntriesContract.View>
        implements SubmissionEntriesContract.Presenter {

    private final List<Entry> entries;

    SubmissionEntriesPresenter(@NonNull View view,
                               @NonNull PresenterConfiguration configuration, List<Entry> entries) {
        super(view, configuration);
        this.entries = entries;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showSubmissionList(entries);
    }
}
