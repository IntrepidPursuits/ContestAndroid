package io.intrepid.contest.screens.contestjudging.submissionentrieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Entry;

class SubmissionEntriesContract {
    public interface View extends BaseContract.View {
        void showSubmissionList(List<Entry> submissionEntries);
    }

    public interface Presenter extends BaseContract.Presenter<View> {

    }
}
