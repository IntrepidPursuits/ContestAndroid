package io.intrepid.contest.screens.contestcreation.describecontest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DescribeContestPresenterTest {
    @Mock
    DescribeContestContract.View mockView;
    private DescribeContestContract.Presenter describeContestPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        describeContestPresenter = new DescribeContestPresenter(mockView,
                                                                TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void emptyContestDescriptionShouldYieldError() {
        String EMPTY_TEXT = "";
        describeContestPresenter.onNextClicked(EMPTY_TEXT);
        verify(mockView).showError();
    }

    @After
    public void validContestDescriptionShouldYieldError() {
        String VALID_TEXT = "Valid Contest Name";
        describeContestPresenter.onNextClicked(VALID_TEXT);
        verify(mockView).saveContestDescription(VALID_TEXT);
    }
}

