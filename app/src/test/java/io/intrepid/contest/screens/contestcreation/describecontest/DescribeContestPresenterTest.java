package io.intrepid.contest.screens.contestcreation.describecontest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
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
    public void onViewCreatedShouldCauseViewToDisableNext() {
        describeContestPresenter.onViewCreated();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onNextClickShouldTriggerViewToShowNextScreen() {
        String VALID_TEXT = "Valid Contest Name";
        describeContestPresenter.onNextClicked(VALID_TEXT);
        verify(mockView).showNextScreen();
    }

    @Test
    public void onNextValidatedShouldCauseViewToEnableNext() {
        describeContestPresenter.onNextValidated();
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onNextInValidatedShouldCauseViewToDisableNext() {
        describeContestPresenter.onNextInvalidated();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onTextChangedShouldCauseViewToEnableNext() {
        String validText = " A";

        describeContestPresenter.onTextChanged(validText);

        assertFalse(describeContestPresenter.isEmpty(validText));
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onTextChangedShouldCauseViewToDisableNextWhenEmptyTextIsEntered() {
        String emptyText = "      ";
        describeContestPresenter.onTextChanged(emptyText);

        assertTrue(describeContestPresenter.isEmpty(emptyText));
        verify(mockView).setNextEnabled(false);
    }
}

