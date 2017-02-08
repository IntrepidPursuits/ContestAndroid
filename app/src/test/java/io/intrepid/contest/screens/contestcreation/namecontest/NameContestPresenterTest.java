package io.intrepid.contest.screens.contestcreation.namecontest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NameContestPresenterTest {
    @Mock
    NameContestContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;
    private NameContestContract.Presenter nameContestPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        nameContestPresenter = new NameContestPresenter(mockView, TestPresenterConfiguration.createTestConfiguration(),
                                                        mockContestBuilder);
    }

    @Test
    public void onContestTitleUpdatedShouldCauseViewToShowNextScreen() {
        String VALID_TEXT = "ContestName";
        nameContestPresenter.onContestTitleUpdated(VALID_TEXT);
        verify(mockView).showNextScreen();
    }

    @Test
    public void onViewCreatedShouldTriggerViewToHideNextButton(){
        nameContestPresenter.onViewCreated();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onNextValidatedShouldCauseViewToEnableNext() {
        nameContestPresenter.onNextValidated();
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onNextInValidatedShouldCauseViewToDisableNext() {
        nameContestPresenter.onNextInvalidated();
        verify(mockView).setNextEnabled(false);
    }
}

