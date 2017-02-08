package io.intrepid.contest.screens.contestoverview;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestResponse;
import io.intrepid.contest.rest.ContestStatusResponse;
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
        initialSetupOfOnViewCreatedApiCalls();
    }

    private void initialSetupOfOnViewCreatedApiCalls() {
        setupSuccessfulContestDetailsCall();
        setupSuccessfulContestStatusCall();
    }

    private void setupSuccessfulContestDetailsCall() {
        ContestResponse response = new ContestResponse();
        response.contest = new Contest();
        response.contest.setTitle("Contest title");
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(response));
    }

    private void setupFailedContestDetailsCall() {
        when(mockRestApi.getContestDetails(any())).thenReturn(error(new Throwable()));
    }

    private void setupSuccessfulContestStatusCall() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.numSubmissionsMissing = 5;
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
    }

    private void setupFailedContestStatusCall() {
        when(mockRestApi.getContestStatus(any())).thenReturn(error(new Throwable()));
    }

    @Test
    public void onViewCreatedShouldShowContestNameWhenSuccessfullyRetrievedContestDetails() throws Exception {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestName(any());
    }

    @Test
    public void onViewCreatedShouldShowNumSubmissionsMissingWhenSuccessfullyRetrievedContestStatus() throws Exception {
        setupSuccessfulContestStatusCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showNumSubmissionsMissing(any(int.class));
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenContestDetailsCallThrowsError() throws Exception {
        setupFailedContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenContestStatusCallThrowsError() throws Exception {
        setupFailedContestStatusCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }
}
