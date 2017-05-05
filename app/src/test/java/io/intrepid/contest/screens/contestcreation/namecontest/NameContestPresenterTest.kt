package io.intrepid.contest.screens.contestcreation.namecontest

import io.intrepid.contest.models.Contest
import io.intrepid.contest.screens.contestcreation.namecontest.NameContestContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NameContestPresenterTest : BasePresenterTest<NameContestPresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockContestBuilder: Contest.Builder

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = NameContestPresenter(mockView, testConfiguration, mockContestBuilder)
    }

    @Test
    fun onContestTitleUpdatedShouldCauseViewToShowNextScreen() {
        val VALID_TEXT = "ContestName"
        presenter.onContestTitleUpdated(VALID_TEXT)
        verify<View>(mockView).showNextScreen()
    }

    @Test
    fun onViewCreatedShouldTriggerViewToHideNextButton() {
        presenter.onViewCreated()
        verify<View>(mockView).onNextPageEnabledChanged(false)
    }

    @Test
    fun onNextValidatedShouldCauseViewToEnableNext() {
        presenter.onNextValidated()
        verify<View>(mockView).onNextPageEnabledChanged(true)
    }

    @Test
    fun onNextInValidatedShouldCauseViewToDisableNext() {
        presenter.onNextInvalidated()
        verify<View>(mockView).onNextPageEnabledChanged(false)
    }
}

