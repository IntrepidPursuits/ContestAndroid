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

import static org.mockito.ArgumentMatchers.anyList;
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

    @Before
    public void setup() {
        mockEntry.id = UUID.randomUUID();
        when(mockView.getEntryToRate()).thenReturn(mockEntry);
        presenter = new EntryDetailPresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowEntries() {
        presenter.onViewCreated();
        verify(mockView).showEntries(anyList());
    }

    private void makeCompletedBallot() {
        EntryBallot completeBallot = new EntryBallot(UUID.randomUUID());
        when(mockView.getEntryBallot()).thenReturn(completeBallot);
        completeBallot.addScore(new Score(mockCategory, 1));

        List<EntryBallot> completedBallots = new ArrayList<>();
        completedBallots.add(completeBallot);
        when(mockView.getAllBallots()).thenReturn(completedBallots);
    }

    private void makeIncompleteBallots() {
        List<EntryBallot> incompleteBallots = new ArrayList<>();
        EntryBallot incompleteBallot = new EntryBallot(UUID.randomUUID());
        incompleteBallot.addScore(new Score(new Category("TEST", "TEST"), 0));
        incompleteBallot.addScore(new Score(new Category("TESTER", "TEST"), 0));
        incompleteBallots.add(incompleteBallot);

        when(mockView.getEntryBallot()).thenReturn(incompleteBallot);
        when(mockView.getAllBallots()).thenReturn(incompleteBallots);
    }

    @Test
    public void onPageSwipedShouldCauseViewToUpdateToolbarTitle() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry());
        entries.add(new Entry());
        when(mockView.getAllEntries()).thenReturn(entries);
        presenter.onViewCreated();

        presenter.onSnapViewSwiped(1);

        verify(mockView).onPageSwipedTo(1);
    }

    @Test
    public void onScoreChangedShouldCauseViewToShowReviewButtonWhenAllBallotsAreScored() {
        makeCompletedBallot();

        presenter.onViewCreated();
        presenter.onScoreChanged(0, 1);

        verify(mockView).setReviewRatingsButtonVisibility(true);
    }

    @Test
    public void onScoreChangedShouldCauseViewToDoNothingWhenAllBallotsAreNotScored() {
        makeIncompleteBallots();

        presenter.onViewCreated();
        presenter.onScoreChanged(0, 1);

        verify(mockView).setReviewRatingsButtonVisibility(false);
    }

    @Test
    public void onEntryScoreReviewClickedShouldCauseViewToReturnToEntriesListPage() {
        presenter.onViewCreated();
        presenter.onEntryScoreReviewClicked();
        verify(mockView).returnToEntriesListPage(true);
    }

    @Test
    public void onPageScrolledShouldCauseViewToSetNextInvisible() {
        when(mockEntry.isCompletelyScored()).thenReturn(false);
        presenter.onPageScrolled();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onPageScrolledShouldCauseViewToSetNextVisibleWhenBallotIsScored() {
        when(mockEntry.isCompletelyScored()).thenReturn(true);
        presenter.onPageScrolled();
        verify(mockView).setNextEnabled(true);
    }
}
