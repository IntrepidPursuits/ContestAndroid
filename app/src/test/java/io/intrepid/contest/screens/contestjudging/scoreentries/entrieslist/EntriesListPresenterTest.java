package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Entry;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EntriesListPresenterTest extends BasePresenterTest<EntriesListPresenter> {
    @Mock
    EntriesListContract.View mockView;
    private List<Entry> entries;

    @Before
    public void setup() {
        entries = new ArrayList<>();
        entries.add(new Entry());
        when(mockView.getEntries()).thenReturn(entries);

        presenter = new EntriesListPresenter(mockView, testConfiguration);
        presenter.onViewCreated();
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowEntriesList() {
        verify(mockView).showEntriesList();
    }

    @Test
    public void onViewCreatedShouldEnableNextWhenEntriesAreCompletelyRated() {
        for (Entry entry : entries) {
            entry.setRatingAverage(1);
        }
        presenter.onViewCreated();
        verify(mockView).setNextVisible(true);
    }

    @Test
    public void onViewCreatedShouldDisableNextWhenEntriesAreNotCompletelyRated() {
        when(mockView.getEntries()).thenReturn(makeCompletedListOfEntries());
        presenter.onViewCreated();
        verify(mockView).setNextVisible(false);
    }

    private List<Entry> makeCompletedListOfEntries() {
        List<Entry> entries = new ArrayList<>();
        for (Entry entry : entries) {
            entry.setRatingAverage(anyInt() + 1);
        }
        return entries;
    }
}
