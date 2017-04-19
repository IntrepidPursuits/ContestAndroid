package io.intrepid.contest.screens.conteststatus.resultsavailable

import io.intrepid.contest.models.Contest
import io.intrepid.contest.rest.ContestWrapper
import io.intrepid.contest.screens.conteststatus.resultsavailable.ResultsAvailableContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.functions.Consumer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ResultsAvailablePresenterTest : BasePresenterTest<ResultsAvailablePresenter>() {
    @Mock
    private lateinit var mockView: View

    /**
     * Extension Function to allow generics in argument captor creation
     */
    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)!!

    @Before
    fun setup() {
        presenter = ResultsAvailablePresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldRequestContestDetails() {
        presenter.onViewCreated()
        verify<View>(mockView).requestContestDetails(any<Consumer<ContestWrapper>>(), any<Consumer<Throwable>>())
    }

    @Test
    fun onViewCreatedShouldShowContestNameWhenApiCallDoesNotThrowError() {
        val captor = argumentCaptor<Consumer<ContestWrapper>>()
        doAnswer {
            val response = ContestWrapper(Contest())
            response.contest.title = "Contest title"
            captor.value.accept(response)
        }.`when`<View>(mockView).requestContestDetails(captor.capture(), any<Consumer<Throwable>>())

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showContestName(any(String::class.java))
        verify<View>(mockView, never()).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    @Test
    fun onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() {
        val captor = argumentCaptor<Consumer<Throwable>>()
        doAnswer {
            captor.value.accept(Throwable())
        }.`when`<View>(mockView).requestContestDetails(any<Consumer<ContestWrapper>>(), captor.capture())

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    @Test
    fun onViewResultsButtonClickedShouldShowResultsPage() {
        presenter.onViewResultsButtonClicked()
        verify<View>(mockView).showResultsPage()
    }
}
