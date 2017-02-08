package io.intrepid.contest.screens.splash;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import io.intrepid.contest.models.User;
import io.intrepid.contest.rest.UserCreationResponse;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest extends BasePresenterTest<SplashPresenter> {
    @Mock
    SplashContract.View mockView;
    private SplashContract.Presenter splashPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        splashPresenter = new SplashPresenter(mockView, testConfiguration);
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
        setupSuccessfulUserCreation();

        splashPresenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showUserButtons();
    }

    @Test
    public void onViewCreatedShouldShowUserButtonsWhenUserIsRetrievedFromPersistentSettings() {
        when(mockPersistentSettings.isAuthenticated()).thenReturn(true);
        splashPresenter.onViewCreated();
        verify(mockView).showUserButtons();
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() throws HttpException {
        setupFailedUserCreation();

        splashPresenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void createContestClicked() {
        setupSuccessfulUserCreation();
        splashPresenter.onCreateContestClicked();
        verify(mockView).showCreateContestScreen();
    }

    @Test
    public void joinContestClicked() {
        setupSuccessfulUserCreation();
        splashPresenter.onJoinContestClicked();
        verify(mockView).showJoinContestScreen();
    }
}

