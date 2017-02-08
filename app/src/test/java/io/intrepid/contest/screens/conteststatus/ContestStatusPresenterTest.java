package io.intrepid.contest.screens.conteststatus;

import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.intrepid.contest.screens.conteststatus.ContestStatusContract.Presenter;
import io.intrepid.contest.screens.conteststatus.ContestStatusContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContestStatusPresenterTest extends BasePresenterTest<ContestStatusPresenter> {
    @Mock
    View mockView;

    private Presenter presenter;
    private Throwable throwable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        throwable = new Throwable();
        presenter = new ContestStatusPresenter(mockView, testConfiguration);

        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
    }

    @NonNull
    private ContestStatusResponse getContestStatusResponseWaitingForSubmissions() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.setSubmissionData(false, 0, 5);
        response.setJudgeData(false, 0, 1);
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
        return response;
    }

    @NonNull
    private ContestStatusResponse getContestStatusResponseWaitingForScores() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.setSubmissionData(true, 5, 5);
        response.setJudgeData(false, 0, 1);
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
        return response;
    }

    @NonNull
    private ContestStatusResponse getContestStatusResponseResultsAvailable() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.setSubmissionData(true, 5, 5);
        response.setJudgeData(true, 1, 1);
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
        return response;
    }

    @Test
    public void onViewCreatedShouldShowWaitingSubmissionsFragmentWhenStatusIsWaitingForSubmissions() {
        ContestStatusResponse response = getContestStatusResponseWaitingForSubmissions();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showWaitingSubmissionsFragment(response.getNumSubmissionsMissing());
        verify(mockView, never()).showResultsAvailableFragment();
        verify(mockView, never()).showContestOverviewPage();
    }

    @Test
    public void onViewCreatedShouldShowContestOverviewWhenWaitingForScoresAndParticipantIsJudge() {
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.JUDGE);
        getContestStatusResponseWaitingForScores();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestOverviewPage();
        verify(mockView, never()).showWaitingSubmissionsFragment(anyInt());
        verify(mockView, never()).showResultsAvailableFragment();
    }

    @Test
    public void onViewCreatedShouldShowResultsAvailableFragmentWhenStatusIsResultsAvailable() {
        getContestStatusResponseResultsAvailable();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showResultsAvailableFragment();
        verify(mockView, never()).showWaitingSubmissionsFragment(anyInt());
        verify(mockView, never()).showContestOverviewPage();
    }

    @Test
    public void onViewCreatedShouldRefreshWaitingSubmissionsFragmentEveryThreeSeconds() {
        // TODO: change test to 2 minutes for release
        ContestStatusResponse response = getContestStatusResponseWaitingForSubmissions();

        presenter.onViewCreated();
        testConfiguration.getIoScheduler().advanceTimeBy(3, TimeUnit.SECONDS);
        testConfiguration.triggerRxSchedulers();

        verify(mockView, times(2)).showWaitingSubmissionsFragment(response.getNumSubmissionsMissing());
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() throws HttpException {
        when(mockRestApi.getContestStatus(any())).thenReturn(error(throwable));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(anyInt());
    }

    @Test
    public void onRequestContestDetailsShouldCallResponseConsumerWhenApiCallDoesNotThrowError() throws Exception {
        Consumer<ContestWrapper> responseConsumer = mock(Consumer.class);
        Consumer<Throwable> throwableConsumer = mock(Consumer.class);
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(new ContestWrapper(any())));

        presenter.requestContestDetails(responseConsumer, throwableConsumer);
        testConfiguration.triggerRxSchedulers();

        verify(responseConsumer).accept(any());
        verify(throwableConsumer, never()).accept(any(Throwable.class));
    }

    @Test
    public void onRequestContestDetailsShouldCallThrowableConsumerWhenApiCallThrowsError() throws Exception {
        Consumer<ContestWrapper> responseConsumer = mock(Consumer.class);
        Consumer<Throwable> throwableConsumer = mock(Consumer.class);
        when(mockRestApi.getContestDetails(any())).thenReturn(error(throwable));

        presenter.requestContestDetails(responseConsumer, throwableConsumer);
        testConfiguration.triggerRxSchedulers();

        verify(throwableConsumer).accept(any(Throwable.class));
        verify(responseConsumer, never()).accept(any());
    }
}
