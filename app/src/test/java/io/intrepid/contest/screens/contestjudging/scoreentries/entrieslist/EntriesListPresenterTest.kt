package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist

import io.intrepid.contest.models.Entry
import io.intrepid.contest.rest.AdjudicateRequest
import io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist.EntriesListContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class EntriesListPresenterTest : BasePresenterTest<EntriesListPresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockEntries: List<Entry>
    @Mock
    private lateinit var mockEntry: Entry
    @Mock
    private lateinit var mockEntryIterator: Iterator<Entry>

    @Before
    fun setup() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockView.entries).thenReturn(mockEntries)
        `when`(mockEntryIterator.hasNext()).thenReturn(true, true, false)
        `when`(mockEntryIterator.next()).thenReturn(mockEntry).thenReturn(mockEntry)
        `when`(mockEntries.iterator()).thenReturn(mockEntryIterator)
        presenter = EntriesListPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldTriggerViewToShowEntriesList() {
        presenter.onViewCreated()
        verify<View>(mockView).showEntriesList(false)
    }

    @Test
    fun onViewCreatedShouldShowSubmitButtonWhenEntriesAreCompletelyRated() {
        `when`(mockEntry.isCompletelyScored).thenReturn(true)
        presenter.onViewCreated()
        verify<View>(mockView).showSubmitButton(true)
    }

    @Test
    fun onViewCreatedShouldHideSubmitButtonWhenEntriesAreNotCompletelyRated() {
        `when`(mockEntry.isCompletelyScored).thenReturn(false)
        presenter.onViewCreated()
        verify<View>(mockView).showSubmitButton(false)
    }

    @Test
    fun onSubmitButtonClickedShouldShowContestStatusScreenWhenApiCallIsSuccessful() {
        `when`(mockRestApi.adjudicate(anyString(), any<AdjudicateRequest>()))
                .thenReturn(Observable.just(Response.success<Void>(null)))
        presenter.onViewCreated()

        presenter.onSubmitButtonClicked()
        testConfiguration.triggerRxSchedulers()

        mockView.showContestStatusScreen()
    }

    @Test
    fun onSubmitButtonClickedShouldShowErrorMessageWhenApiCallIsNotSuccessful() {
        `when`(mockRestApi.adjudicate(anyString(), any<AdjudicateRequest>()))
                .thenReturn(Observable.just(Response.error<Void>(HttpURLConnection.HTTP_NOT_FOUND,
                        ResponseBody.create(MediaType.parse("application/json"),
                                "{\"errors\":[\"Error\"]}"))))
        presenter.onViewCreated()

        presenter.onSubmitButtonClicked()
        testConfiguration.triggerRxSchedulers()

        mockView.showMessage(anyInt())
    }

    @Test
    fun onSubmitButtonClickedShouldShowErrorMessageWhenApiCallThrowsError() {
        `when`(mockRestApi.adjudicate(anyString(), any<AdjudicateRequest>()))
                .thenReturn(error<Response<Void>>(Throwable()))
        presenter.onViewCreated()

        presenter.onSubmitButtonClicked()
        testConfiguration.triggerRxSchedulers()

        mockView.showMessage(anyInt())
    }
}
