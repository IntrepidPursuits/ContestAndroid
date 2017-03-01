package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EntryDetailPresenterTest extends BasePresenterTest<EntryDetailPresenter> {
    @Mock
    EntryDetailContract.View mockView;
    @Mock
    Entry mockEntry;
    @Mock
    EntryBallot mockBallot;
    @Mock
    Category mockCategory;
    private List<Category> categories = new ArrayList<>();
    private List<EntryBallot> allBallots = new ArrayList<>();


    @Before
    public void setup() {
        mockEntry.id = UUID.randomUUID();
        initializeContestData();
        when(mockView.getAllBallots()).thenReturn(allBallots);
        when(mockView.getEntryToRate()).thenReturn(mockEntry);
        when(mockView.getEntryBallot()).thenReturn(new EntryBallot(mockEntry.id));
        when(mockView.getCategories()).thenReturn(categories);

        presenter = new EntryDetailPresenter(mockView, testConfiguration);
    }

    private void initializeContestData() {
        for (int i = 0; i < 5; i++) {
            Category category = new Category("TEST " + i, "TESTER");
            categories.add(category);
        }

        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            Score score = new Score(category, 0);
            scores.add(score);
        }

        for (int i = 0; i < 2; i++) {
            allBallots.add(new EntryBallot(UUID.randomUUID()));
        }
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowEntry() {
        presenter.onViewCreated();
        verify(mockView).showEntry(any());
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowCategories() {
        presenter.onViewCreated();
        verify(mockView).showListOfEntryScores(anyList());
    }

    @Test
    public void onScoreChangedShouldCauseViewToSetNextEnabledWhenScoringIsComplete() {
        presenter.onViewCreated();
        for (int i = 0; i < categories.size(); i++) {
            presenter.onScoreChanged(i, 2);
        }
        verify(mockView).setNextEnabled(true);
    }

    private void makeCompletedBallot() {
        List<EntryBallot> completedBallots = new ArrayList<>();
        EntryBallot completeBallot = new EntryBallot(UUID.randomUUID());
        when(mockView.getEntryBallot()).thenReturn(completeBallot);
        completeBallot.addScore(new Score(mockCategory, 1));
        completedBallots.add(completeBallot);
        when(mockView.getAllBallots()).thenReturn(completedBallots);
    }

    private void makeIncompleteBallots() {
        List<EntryBallot> incompleteBallots = new ArrayList<>();
        EntryBallot incompleteBallot = new EntryBallot(UUID.randomUUID());
        when(mockView.getEntryBallot()).thenReturn(incompleteBallot);
        incompleteBallots.add(incompleteBallot);
        when(mockView.getAllBallots()).thenReturn(incompleteBallots);
    }

    @Test
    public void onScoreChangeShouldCauseViewToKeepReviewButtonHiddenIfNotOnLastEntry() {
        when(mockView.getEntryBallot()).thenReturn(mockBallot);
        when(mockBallot.isCompletelyScored()).thenReturn(false);

        presenter.onViewCreated();
        presenter.onScoreChanged(0, 1);

        verify(mockView, never()).setReviewRatingsButtonVisibility(true);
    }

    @Test
    public void onScoreChangeShouldKeepReviewButtonHiddenIfOnLastPageAndStillMissingScores() {
        makeIncompleteBallots();

        presenter.onViewCreated();
        presenter.onScoreChanged(1, 2);

        verify(mockView).setReviewRatingsButtonVisibility(false);
        verify(mockView, never()).setReviewRatingsButtonVisibility(true);
    }

    @Test
    public void onScoreChangedShouldCauseViewToShowReviewButtonWhenAllBallotsAreScored() {
        makeCompletedBallot();

        presenter.onViewCreated();
        presenter.onScoreChanged(0, 1);

        verify(mockView).setReviewRatingsButtonVisibility(true);
    }

    @Test
    public void onEntryScoreReviewClickedShouldCauseViewToReturnToEntriesListPage() {
        presenter.onViewCreated();
        presenter.onEntryScoreReviewClicked();
        verify(mockView).returnToEntriesListPage();
    }
}
