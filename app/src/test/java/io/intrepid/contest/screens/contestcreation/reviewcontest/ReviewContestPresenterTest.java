package io.intrepid.contest.screens.contestcreation.reviewcontest;

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
public class ReviewContestPresenterTest {
    @Mock
    ReviewContestContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;
    @Mock
    Contest mockContest;
    private ReviewContestContract.Presenter reviewContestPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        reviewContestPresenter = new ReviewContestPresenter(mockView,
                                                            TestPresenterConfiguration.createTestConfiguration(),
                                                            mockContestBuilder);
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowReviewPage() {
        reviewContestPresenter.onViewCreated();
        verify(mockView).showReviewPage(mockContestBuilder);
    }

    @Test
    public void onContestTitleSelectedShouldCauseViewToShowEditTitlePage() {
        reviewContestPresenter.onContestTitleSelected();
        verify(mockView).showEditTitlePage(mockContestBuilder);
    }

    @Test
    public void onContestDescriptionSelectedShouldCauseViewToShowEditDescriptionPage() {
        reviewContestPresenter.onContestDescriptionSelected();
        verify(mockView).showEditDescriptionPage(mockContestBuilder);
    }
}

