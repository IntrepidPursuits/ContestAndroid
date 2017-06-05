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

    private lateinit var entries: List<Entry>

    @Before
    fun setup() {
        entries = mutableListOf(Entry(), Entry())
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockView.entries).thenReturn(entries)
        presenter = EntriesListPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldTriggerViewToShowEntriesListWhenEntriesAreNotCompletelyRated() {
        entries[0].setCategoriesSize(1)
        presenter.onViewCreated()
        verify<View>(mockView).showEntriesList(false)
    }

    @Test
    fun onViewCreatedShouldShowSubmitButtonWhenEntriesAreCompletelyRated() {
        presenter.onViewCreated()
        verify<View>(mockView).showSubmitButton(true)
    }

    @Test
    fun onViewCreatedShouldHideSubmitButtonWhenEntriesAreNotCompletelyRated() {
        entries[0].setCategoriesSize(1)
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
