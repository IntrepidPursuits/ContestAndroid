package io.intrepid.contest.screens.splash;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.splash.SplashContract;
import io.intrepid.contest.splash.SplashPresenter;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {
    @Mock
    SplashContract.View mockView;
    private SplashPresenter splashPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        splashPresenter = new SplashPresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void createContestClicked() {
        splashPresenter.onCreateContestClicked();
        verify(mockView).showCreateContestScreen();
    }

    @Test
    public void joinContestClicked() {
        splashPresenter.onJoinContestClicked();
        verify(mockView).showJoinContestScreen();
    }
}

