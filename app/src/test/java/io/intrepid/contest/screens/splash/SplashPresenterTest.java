package io.intrepid.contest.screens.splash;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import io.intrepid.contest.R;
import io.intrepid.contest.models.ActiveContestListResponse;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.User;
import io.intrepid.contest.rest.UserCreationResponse;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest extends BasePresenterTest<SplashPresenter> {
    @Mock
    SplashContract.View mockView;
    @Mock
    ActiveContestListResponse mockActiveContestListResponse;
    @Mock
    Contest mockContest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new SplashPresenter(mockView, testConfiguration);
    }

    private void setupSuccessfulUserCreation() {
        UserCreationResponse UserCreationResponse = new UserCreationResponse();
        UserCreationResponse.user = new User();
        UserCreationResponse.user.setId(UUID.randomUUID());
        when(mockRestApi.createUser()).thenReturn(Observable.just(UserCreationResponse));
    }

    private void setupFailedUserCreation() {
        when(mockRestApi.createUser()).thenReturn(error(new Throwable()));
    }

    @Test
    public void onViewCreatedShouldShowUserButtonsWhenUserCreationIsSuccessful() {
        when(mockRestApi.getActiveContests(any())).thenReturn(Observable.just(mockActiveContestListResponse));
        setupSuccessfulUserCreation();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showUserButtons();
    }

    @Test
    public void onViewCreatedShouldShowUserButtonsWhenUserIsRetrievedFromPersistentSettings() {
        when(mockRestApi.getActiveContests(any())).thenReturn(Observable.just(mockActiveContestListResponse));
        when(mockPersistentSettings.isAuthenticated()).thenReturn(true);

        presenter.onViewCreated();

        verify(mockView).showUserButtons();
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() throws HttpException {
        setupFailedUserCreation();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void createContestClicked() {
        presenter.onCreateContestClicked();
        verify(mockView).showCreateContestScreen();
    }

    @Test
    public void joinContestClicked() {
        presenter.onJoinContestClicked();
        verify(mockView).showJoinContestScreen();
    }

    @Test
    public void onViewCreatedShouldCauseViewToShowOngoingContestsWhenUserIsAuthenticated() {
        when(mockPersistentSettings.isAuthenticated()).thenReturn(true);
        when(mockRestApi.getActiveContests(any())).thenReturn(Observable.just(mockActiveContestListResponse));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showOngoingContests(anyList());
    }

    private void doSuccessfulAuthenticationApiCallAndActiveContestsCallInSequence(boolean successfulActiveContestCall) {
        Observable<ActiveContestListResponse> response = successfulActiveContestCall ?
                Observable.just(mockActiveContestListResponse) :
                error(new Throwable());
        when(mockRestApi.getActiveContests(any())).thenReturn(response);
        setupSuccessfulUserCreation();

        presenter.onViewCreated();
        //Called twice to trigger second api call.
        testConfiguration.triggerRxSchedulers();
        testConfiguration.triggerRxSchedulers();

    }

    @Test
    public void onViewCreatedShouldCauseViewToShowOngoingContestsAfterAuthenticateUserSuccess() {
        doSuccessfulAuthenticationApiCallAndActiveContestsCallInSequence(true);
        verify(mockView).showOngoingContests(anyList());
    }

    @Test
    public void onViewCreatedShouldShowErrorWhenDiscoverOngoingContestReturnsThrowable() {
        doSuccessfulAuthenticationApiCallAndActiveContestsCallInSequence(false);
        verify(mockView).showMessage(R.string.error_api);
    }

    @Test
    public void onContestClickedShouldCauseViewToResumeContest() {
        presenter.onContestClicked(mockContest);
        verify(mockView).resumeContest(mockContest);
    }
}
