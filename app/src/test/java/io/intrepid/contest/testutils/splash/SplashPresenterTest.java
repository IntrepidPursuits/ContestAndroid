package io.intrepid.contest.testutils.splash;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.splash.SplashContract;
import io.intrepid.contest.splash.SplashPresenter;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {

    SplashPresenter presenter;
    @Mock SplashContract.View mockView;

    @Before
    public void setup(){
        presenter = new SplashPresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void exampleButtonClickedShouldShowSuccessInView(){
        presenter.exampleButtonClicked();
        verify(mockView).showMessage("Fail");
    }

}
