package io.intrepid.contest.screens.contestoverview;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.screens.contestoverview.ContestOverviewContract.Presenter;
import io.intrepid.contest.screens.contestoverview.ContestOverviewContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContestOverviewPresenterTest extends BasePresenterTest<ContestOverviewPresenter> {
    @Mock
    View mockView;

    private Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new ContestOverviewPresenter(mockView, testConfiguration);

        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
    }

    private void setupSuccessfulContestDetailsCall() {
        Contest contest = new Contest();
        contest.setTitle("Contest title");
        ContestWrapper response = new ContestWrapper(contest);
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(response));
    }

    private void setupFailedContestDetailsCall() {
        when(mockRestApi.getContestDetails(any())).thenReturn(error(new Throwable()));
    }

    @Test
    public void onViewCreatedShouldShowContestNameWhenSuccessfullyRetrievedContestDetails() throws Exception {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestName(any());
    }

    @Test
    public void onViewCreatedShouldShowRatingGuide() throws Exception {
        //Not a pre-condition for this test but is required to make sure onViewCreated does not fail
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showRatingGuide();
    }

    @Test
    public void showContestDescription() throws Exception {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestDescription(any());
    }

    @Test
    public void showContestCategories() throws Exception {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showCategories(any());
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenContestDetailsCallThrowsError() throws Exception {
        setupFailedContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }
}
