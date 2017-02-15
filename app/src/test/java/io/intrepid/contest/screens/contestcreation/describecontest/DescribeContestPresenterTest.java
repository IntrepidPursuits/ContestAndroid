package io.intrepid.contest.screens.contestcreation.describecontest;

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
public class DescribeContestPresenterTest {
    @Mock
    DescribeContestContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;
    private DescribeContestPresenter describeContestPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        describeContestPresenter = new DescribeContestPresenter(mockView,
                                                                TestPresenterConfiguration.createTestConfiguration(),
                                                                mockContestBuilder);
    }

    @Test
    public void onViewCreatedShouldCauseViewToEnableNext() {
        describeContestPresenter.onViewCreated();
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onNextClickShouldTriggerViewToShowNextScreen() {
        String VALID_TEXT = "Valid Contest Name";
        describeContestPresenter.onNextClicked(VALID_TEXT);
        verify(mockView).showNextScreen();
    }
}
