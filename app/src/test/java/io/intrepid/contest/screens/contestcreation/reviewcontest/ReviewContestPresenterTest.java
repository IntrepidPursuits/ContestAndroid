package io.intrepid.contest.screens.contestcreation.reviewcontest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReviewContestPresenterTest extends BasePresenterTest<ReviewContestPresenter> {
    @Mock
    ReviewContestContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;
    @Mock
    Contest mockContest;

    @Before
    public void setup() {
        presenter = new ReviewContestPresenter(mockView,
                                               testConfiguration,
                                               mockContestBuilder);
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowReviewPage() {
        presenter.onViewCreated();
        verify(mockView).displayReviewPageContent(mockContestBuilder);
    }

    @Test
    public void onContestTitleSelectedShouldCauseViewToShowEditTitlePage() {
        presenter.onContestTitleSelected();
        verify(mockView).showEditTitlePage(mockContestBuilder);
    }

    @Test
    public void onContestDescriptionSelectedShouldCauseViewToShowEditDescriptionPage() {
        presenter.onContestDescriptionSelected();
        verify(mockView).showEditDescriptionPage(mockContestBuilder);
    }


    @Test
    public void onPageSelectedShouldDisplayReviewPageContent() {
        presenter.onPageSelected();
        verify(mockView).displayReviewPageContent(mockContestBuilder);
    }
}

