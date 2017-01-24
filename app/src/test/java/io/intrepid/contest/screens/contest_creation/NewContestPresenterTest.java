package io.intrepid.contest.screens.contest_creation;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.screens.contestcreation.NewContestContract;
import io.intrepid.contest.screens.contestcreation.NewContestPresenter;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class NewContestPresenterTest{
    @Mock
    NewContestContract.View mockView;
    private NewContestContract.Presenter newContestPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        newContestPresenter = new NewContestPresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void emptyContestNameShouldTriggerFailure(){
        mockView.acceptContestName("");
        boolean canAdvance = newContestPresenter.canAdvanceToNextScreen();
    }


}
