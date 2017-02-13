package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EntryDetailPresenterTest extends BasePresenterTest<EntryDetailPresenter> {
    @Mock
    EntryDetailContract.View mockView;
    @Mock
    Entry mockEntry;
    @Mock
    EntryBallot mockEntryBallot;
    private List<Category> categories;
    private List<Score> scores;

    @Before
    public void setup() {
        categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category category = new Category("TEST " + i, "TESTER");
            categories.add(category);
        }
        scores = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            Score score = new Score(category, 0);
            scores.add(score);
        }
        when(mockView.getEntryToRate()).thenReturn(new Entry());
        when(mockView.getCategories()).thenReturn(categories);
        presenter = new EntryDetailPresenter(mockView, testConfiguration);
        presenter.onViewCreated();
    }

    @Test
    public void onCategoryRateChangeShouldCauseEntryToAddScore() {
        presenter.onViewCreated();
        presenter.currentEntryBallot = mockEntryBallot;
        presenter.onScoreChanged(0, 2);
        verify(mockEntryBallot).setScore(0, 2);
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowEntry() {
        verify(mockView).showEntry(any());
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowCategories() {
        verify(mockView).showListOfEntryScores(anyList());
    }

    @Test
    public void onScoreChangedShouldCauseViewToSetNextEnabledWhenScoringIsComplete() {
        for (int i = 0; i < categories.size(); i++) {
            presenter.onScoreChanged(i, 2);
        }
        verify(mockView).setNextEnabled(true);
    }
}
