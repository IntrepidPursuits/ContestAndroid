package io.intrepid.contest.screens.contestjudging.scoresubmission;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubmissionEntriesPresenterTest extends BasePresenterTest<SubmissionEntriesPresenter> {
    @Mock
    SubmissionEntriesContract.View mockView;
    @Mock
    List<Entry> mockEntries;

    @Before
    public void setup() {
        presenter = new SubmissionEntriesPresenter(mockView, testConfiguration);

        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
    }

    private void setupSuccessfulContestDetailsCall() {
        Contest contest = new Contest();
        contest.setTitle("Contest title");
        contest.setEntries(new ArrayList<>());
        ContestWrapper response = new ContestWrapper(contest);
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(response));
    }

    private void setupFailureContestDetailsCall() {
        when(mockRestApi.getContestDetails(any())).thenReturn(error(new Throwable()));
    }

    @Test
    public void onViewCreatedShouldTriggerViewToDisplayListOfEntries() {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showSubmissionList(anyList());
    }

    @Test
    public void fetchEntriesFailureShouldTriggerViewToShowError() {
        setupFailureContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(anyInt());
    }
}
