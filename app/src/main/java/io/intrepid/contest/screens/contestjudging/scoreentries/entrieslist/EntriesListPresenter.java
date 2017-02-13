package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;

class EntriesListPresenter extends BasePresenter<EntriesListContract.View> implements EntriesListContract.Presenter {

    EntriesListPresenter(@NonNull EntriesListContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        boolean nextVisible = determineNextVisibility();
        view.setNextVisible(nextVisible);
        view.showEntriesList();
    }

    private boolean determineNextVisibility() {
        for (Entry entry : view.getEntries()) {
            if (entry.getRatingAverage() == 0) {
                return false;
            }
        }
        return true;
    }
}
