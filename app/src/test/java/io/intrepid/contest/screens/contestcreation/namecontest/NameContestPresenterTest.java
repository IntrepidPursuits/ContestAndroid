package io.intrepid.contest.screens.contestcreation.namecontest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NameContestPresenterTest {
    @Mock
    NameContestContract.View mockView;
    private NameContestContract.Presenter nameContestPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        nameContestPresenter = new NameContestPresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void emptyContestNameShouldYieldError(){
        String EMPTY_TEXT = "";
        nameContestPresenter.onContestNameUpdate(EMPTY_TEXT);
        verify(mockView).showError();

        EMPTY_TEXT = "        ";
        nameContestPresenter.onContestNameUpdate(EMPTY_TEXT);
        verify(mockView).showError();
    }

    @Test
    public void validTestShouldTriggerSuccess(){
        String VALID_TEXT = "ContestName";
        nameContestPresenter.onContestNameUpdate(VALID_TEXT);
        verify(mockView).saveEnteredName(VALID_TEXT);
    }
}

