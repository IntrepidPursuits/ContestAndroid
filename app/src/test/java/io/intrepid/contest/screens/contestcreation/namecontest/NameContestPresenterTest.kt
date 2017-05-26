package io.intrepid.contest.screens.contestcreation.namecontest

import io.intrepid.contest.models.Contest
import io.intrepid.contest.screens.contestcreation.namecontest.NameContestContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NameContestPresenterTest : BasePresenterTest<NameContestPresenter>() {
    private val VALID_TEXT = "ContestName"
    private val EMPTY_TEXT = ""

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
        presenter.onContestTitleUpdated(VALID_TEXT)
        verify<View>(mockView).showNextScreen()
    }

    @Test
    fun onNextInValidatedShouldCauseViewToDisableNextWhenNotAlreadyDisabled() {
        presenter.onNextValidated() // Arrange, not Act
        presenter.onNextInvalidated()
        verify<View>(mockView).onNextPageEnabledChanged(false)
    }

    @Test
    fun onNextValidatedShouldCauseViewToEnableNextWhenNotAlreadyEnabled() {
        presenter.onNextValidated()
        verify<View>(mockView).onNextPageEnabledChanged(true)
    }

    @Test
    fun onTextChangedShouldCauseViewToDisableNextWhenNotAlreadyDisabledAndTextIsEmpty() {
        presenter.onNextValidated() // Arrange, not Act
        presenter.onTextChanged(EMPTY_TEXT)
        verify<View>(mockView).onNextPageEnabledChanged(false)
    }

    @Test
    fun onTextChangedShouldCauseViewToEnableNextWhenNotAlreadyEnabledAndTextIsNotEmpty() {
        presenter.onTextChanged(VALID_TEXT)
        verify<View>(mockView).onNextPageEnabledChanged(true)
    }

    @Test
    fun isNextPageButtonEnabledShouldReturnFalseWhenDisabled() {
        assertFalse(presenter.isNextPageButtonEnabled)
    }

    @Test
    fun isNextPageButtonEnabledShouldReturnTrueWhenEnabled() {
        presenter.onNextValidated()
        assertTrue(presenter.isNextPageButtonEnabled)
    }
}

