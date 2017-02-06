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

import io.intrepid.contest.rest.ContestStatusResponse;
import io.intrepid.contest.screens.conteststatus.ContestStatusContract.Presenter;
import io.intrepid.contest.screens.conteststatus.ContestStatusContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
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
    }

    @NonNull
    private ContestStatusResponse getContestStatusResponseWaitingForSubmissions() {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        ContestStatusResponse response = new ContestStatusResponse();
        response.waitingForSubmissions = true;
        response.numSubmissionsMissing = 3;
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response));
        return response;
    }

    @Test
    public void onViewCreatedShouldShowWaitingSubmissionsFragmentWhenStatusIsWaitingForSubmissions() {
        ContestStatusResponse response = getContestStatusResponseWaitingForSubmissions();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showWaitingSubmissionsFragment(response.numSubmissionsMissing);
    }

    @Test
    public void onViewCreatedShouldShouldRefreshWaitingSubmissionsFragmentEveryTwoMinutes() {
        ContestStatusResponse response = getContestStatusResponseWaitingForSubmissions();

        presenter.onViewCreated();
        testConfiguration.getIoScheduler().advanceTimeBy(2, TimeUnit.MINUTES);
        testConfiguration.triggerRxSchedulers();

        verify(mockView, times(2)).showWaitingSubmissionsFragment(response.numSubmissionsMissing);
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() throws HttpException {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.getContestStatus(any())).thenReturn(error(throwable));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }
}
