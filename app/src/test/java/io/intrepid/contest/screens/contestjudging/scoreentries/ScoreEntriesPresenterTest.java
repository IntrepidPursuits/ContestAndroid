package io.intrepid.contest.screens.contestjudging.scoreentries;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScoreEntriesPresenterTest extends BasePresenterTest<ScoreEntriesPresenter> {
    private static final String TEST_ENTRY_IMAGE =
            "https://www.chowstatic.com/assets/2014/09/30669_spicy_slow_cooker_beef_chili_3000x2000.jpg";
    private final List<Entry> entriesList = makeListOfEntries();
    private final List<Category> categoriesList = makeListOfCategories();
    @Mock
    ScoresEntriesContract.View mockView;
    @Mock
    Entry mockEntry;

    @Before
    public void setup() {
        presenter = new ScoreEntriesPresenter(mockView, testConfiguration);
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
    }

    private List<Entry> makeListOfEntries() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Entry entry = new Entry();
            entry.title = "Test Entry " + i;
            entry.photoUrl = TEST_ENTRY_IMAGE;
            entries.add(entry);
        }
        return entries;
    }

    private List<Category> makeListOfCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            categories.add(new Category("Test category " + i, "Description"));
        }
        return categories;
    }

    private void setupSuccessfulContestDetailsCall() {
        Contest contest = new Contest();
        contest.setTitle("Contest title");
        contest.setEntries(entriesList);
        contest.setCategories(categoriesList);

        ContestWrapper response = new ContestWrapper(contest);
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(response));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
    }

    private void setupFailureContestDetailsCall() {
        when(mockRestApi.getContestDetails(any())).thenReturn(error(new Throwable()));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
    }

    @Test
    public void onViewCreatedShouldTriggerViewToDisplayListOfEntries() {
        setupSuccessfulContestDetailsCall();
        verify(mockView).showEntriesList();
    }

    @Test
    public void fetchEntriesFailureShouldTriggerViewToShowError() {
        setupFailureContestDetailsCall();
        verify(mockView).showMessage(anyInt());
    }

    @Test
    public void onBackPressedShouldTriggerViewToShowEntriesListWhenViewingAnEntry() {
        setupSuccessfulContestDetailsCall();
        presenter.onEntrySelected(entriesList.get(0));
        reset(mockView);

        presenter.onBackPressed();

        verify(mockView).showEntriesList();
    }

    @Test
    public void onBackPressedShouldTriggerViewToCancelScoringEntriesWhenNotViewingAnEntry() {
        presenter.onBackPressed();
        verify(mockView).cancelScoringEntries();
    }

    @Test
    public void onBackPressedShouldTriggerViewToShowEntriesListAfterScoringHasStarted() {
        setupSuccessfulContestDetailsCall();
        presenter.onNextClicked();

        presenter.onBackPressed();

        verify(mockView).showEntriesList();
    }

    @Test
    public void getCategoriesShouldReturnCategoriesWhenSuccessfullyRetrievedFromApiCall() {
        setupSuccessfulContestDetailsCall();
        assertEquals(categoriesList, presenter.getCategories());
    }

    @Test
    public void getEntriesShouldReturnEntriesWhenSuccessfullyRetrievedFromApiCall() {
        setupSuccessfulContestDetailsCall();
        assertEquals(entriesList, presenter.getEntries());
    }

    @Test
    public void getEntryBallotsListShouldReturnBallotsCorrespondingToEntriesSuccessfullyRetrievedFromApiCall() {
        setupSuccessfulContestDetailsCall();
        assertEquals(entriesList.size(), presenter.getEntryBallotsList().size());
    }

    @Test
    public void getCurrentEntryShouldReturnCorrectEntryWhenViewingIt() {
        setupSuccessfulContestDetailsCall();
        presenter.onEntrySelected(entriesList.get(0));

        assertEquals(entriesList.get(0), presenter.getCurrentEntry());
    }

    @Test
    public void getCurrentEntryShouldReturnNullWhenNotViewingAnEntry() {
        setupSuccessfulContestDetailsCall();
        assertNull(presenter.getCurrentEntry());
    }

    @Test
    public void getCurrentEntryBallotShouldReturnCorrectBallotWhenViewingEntry() {
        setupSuccessfulContestDetailsCall();
        presenter.onEntrySelected(entriesList.get(0));
        List<EntryBallot> entryBallotList = presenter.getEntryBallotsList();

        assertEquals(entryBallotList.get(0), presenter.getCurrentEntryBallot());
    }

    @Test
    public void getCurrentEntryBallotShouldReturnNullWhenNotViewingAnEntry() {
        setupSuccessfulContestDetailsCall();
        assertEquals(null, presenter.getCurrentEntryBallot());
    }

    @Test
    public void onNextClickedShouldTriggerViewToShowNextEntryWhenNotViewingLastEntry() {
        setupSuccessfulContestDetailsCall();
        presenter.onEntrySelected(entriesList.get(0));
        reset(mockView);

        presenter.onNextClicked();

        verify(mockView).showEntryDetail(anyInt());
    }

    @Test
    public void onNextClickedShouldTriggerViewToShowEntriesListWhenViewingLastEntry() {
        setupSuccessfulContestDetailsCall();
        presenter.onEntrySelected(entriesList.get(entriesList.size() - 1));
        reset(mockView);

        presenter.onNextClicked();

        verify(mockView).showEntriesList();
    }
}
