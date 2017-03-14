package io.intrepid.contest.screens.conteststatus;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.ContestStatus;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.intrepid.contest.rest.ContestWrapper;
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

public class ContestStatusPresenterTest extends BasePresenterTest<ContestStatusPresenter> {
    /*
     * API call interval is shorter for dev builds, a little longer for QA builds, and longest for release
     */
    private static final int API_CALL_INTERVAL = (BuildConfig.DEV_BUILD ? 3 : (BuildConfig.DEBUG ? 10 : 2));
    private static final TimeUnit API_CALL_INTERVAL_UNIT = (BuildConfig.DEBUG ? TimeUnit.SECONDS : TimeUnit.MINUTES);

    @Mock
    View mockView;

    private Throwable throwable;

    @Before
    public void setup() {
        throwable = new Throwable();
        presenter = new ContestStatusPresenter(mockView, testConfiguration, false);

        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
    }

    private void getContestStatusResponseWaitingForSubmissions() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = new ContestStatus();
        response.contestStatus.setSubmissionData(false, 0, 5);
        response.contestStatus.setJudgeData(false, 0, 1);
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
    }

    private void getContestStatusResponseWaitingForScores() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = new ContestStatus();
        response.contestStatus.setSubmissionData(false, 5, 5);
        response.contestStatus.setJudgeData(false, 0, 1);
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
    }

    private void getContestStatusResponseResultsAvailable() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = new ContestStatus();
        response.contestStatus.setSubmissionData(true, 5, 5);
        response.contestStatus.setJudgeData(true, 1, 1);
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
    }

    @Test
    public void onViewCreatedShouldShowAdminStatusPageIfParticipationTypeIsCreator() {
        getContestStatusResponseWaitingForSubmissions();
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.CREATOR);

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showAdminStatusPage();
    }

    @Test
    public void onViewCreatedShouldShowResultsWhenJudgeHasSubmittedScoreAndResultsAreAvailable() {
        getContestStatusResponseResultsAvailable();
        presenter = new ContestStatusPresenter(mockView, testConfiguration, true);
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.JUDGE);

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showResultsAvailableFragment();
    }

    @Test
    public void onViewCreatedShouldShowStatusWaitingFragmentIfJudgeJustVoted() {
        getContestStatusResponseWaitingForScores();
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.JUDGE);

        presenter = new ContestStatusPresenter(mockView, testConfiguration, true);
        presenter.onViewCreated();

        testConfiguration.triggerRxSchedulers();
        verify(mockView).showStatusWaitingFragment();
    }

    @Test
    public void onViewCreatedShouldShowStatusWaitingFragmentWhenStatusIsWaitingForSubmissions() {
        getContestStatusResponseWaitingForSubmissions();
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.CONTESTANT);

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showStatusWaitingFragment();
        verify(mockView, never()).showResultsAvailableFragment();
        verify(mockView, never()).showContestOverviewPage();
    }

    @Test
    public void onViewCreatedShouldShowStatusWaitingFragmentWhenWaitingForScoresAndParticipantIsContestant() {
        getContestStatusResponseWaitingForScores();

        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.CONTESTANT);

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showStatusWaitingFragment();
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
        verify(mockView, never()).showStatusWaitingFragment();
        verify(mockView, never()).showResultsAvailableFragment();
    }

    @Test
    public void onViewCreatedShouldShowResultsAvailableFragmentWhenStatusIsResultsAvailable() {
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.CONTESTANT);
        getContestStatusResponseResultsAvailable();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showResultsAvailableFragment();
        verify(mockView, never()).showStatusWaitingFragment();
        verify(mockView, never()).showContestOverviewPage();
    }

    @Test
    public void onViewCreatedShouldRefreshWaitingSubmissionsFragmentPeriodically() {
        getContestStatusResponseWaitingForSubmissions();
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.CONTESTANT);

        presenter.onViewCreated();
        testConfiguration.getIoScheduler().advanceTimeBy(API_CALL_INTERVAL, API_CALL_INTERVAL_UNIT);
        testConfiguration.triggerRxSchedulers();

        verify(mockView, times(2)).showStatusWaitingFragment();
    }

    @Test
    public void onViewCreatedShouldRefreshOverviewPageWhenParticipationTypeIsJudge() {
        getContestStatusResponseWaitingForSubmissions();
        when(mockPersistentSettings.getCurrentParticipationType()).thenReturn(ParticipationType.JUDGE);

        presenter.onViewCreated();
        testConfiguration.getIoScheduler().advanceTimeBy(API_CALL_INTERVAL, API_CALL_INTERVAL_UNIT);
        testConfiguration.triggerRxSchedulers();

        verify(mockView, times(2)).showContestOverviewPage();
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
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(new ContestWrapper(new Contest())));

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
