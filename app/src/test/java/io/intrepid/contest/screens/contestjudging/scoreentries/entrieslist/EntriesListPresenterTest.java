package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.models.Entry;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockView.getEntries()).thenReturn(entries);

        presenter = new EntriesListPresenter(mockView, testConfiguration);
        presenter.onViewCreated();
    }

    @Test
    public void onViewCreatedShouldTriggerViewToShowEntriesList() {
        verify(mockView).showEntriesList();
    }

    @Test
    public void onViewCreatedShouldShowSubmitButtonWhenEntriesAreCompletelyRated() {
        for (Entry entry : entries) {
            entry.setRatingAverage(1);
        }
        presenter.onViewCreated();
        verify(mockView).showSubmitButton(true);
    }

    @Test
    public void onViewCreatedShouldHideSubmitButtonWhenEntriesAreNotCompletelyRated() {
        when(mockView.getEntries()).thenReturn(makeCompletedListOfEntries());
        presenter.onViewCreated();
        verify(mockView).showSubmitButton(false);
    }

    @Test
    public void onSubmitButtonClickedShouldShowContestStatusScreenWhenApiCallIsSuccessful() {
        when(mockRestApi.adjudicate(anyString(), any())).thenReturn(Observable.just(Response.success(null)));
        presenter.onViewCreated();

        presenter.onSubmitButtonClicked();
        testConfiguration.triggerRxSchedulers();

        mockView.showContestStatusScreen();
    }

    @Test
    public void onSubmitButtonClickedShouldShowErrorMessageWhenApiCallIsNotSuccessful() {
        when(mockRestApi.adjudicate(anyString(), any()))
                .thenReturn(Observable.just(Response.error(HttpURLConnection.HTTP_NOT_FOUND,
                                                           ResponseBody.create(MediaType.parse("application/json"),
                                                                               "{\"errors\":[\"Error\"]}"))));
        presenter.onViewCreated();

        presenter.onSubmitButtonClicked();
        testConfiguration.triggerRxSchedulers();

        mockView.showMessage(anyInt());
    }

    @Test
    public void onSubmitButtonClickedShouldShowErrorMessageWhenApiCallThrowsError() {
        when(mockRestApi.adjudicate(anyString(), any())).thenReturn(error(new Throwable()));
        presenter.onViewCreated();

        presenter.onSubmitButtonClicked();
        testConfiguration.triggerRxSchedulers();

        mockView.showMessage(anyInt());
    }

    private List<Entry> makeCompletedListOfEntries() {
        List<Entry> entries = new ArrayList<>();
        for (Entry entry : entries) {
            entry.setRatingAverage(anyInt() + 1);
        }
        return entries;
    }
}
