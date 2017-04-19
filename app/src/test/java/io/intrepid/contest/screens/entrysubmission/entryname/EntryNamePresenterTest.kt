package io.intrepid.contest.screens.entrysubmission.entryname

import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EntryNamePresenterTest : BasePresenterTest<EntryNamePresenter>() {
    @Mock
    private lateinit var mockView: View

    @Before
    fun setup() {
        presenter = EntryNamePresenter(mockView, testConfiguration)
    }

    @Test
    fun onEntryNameTextViewChangedShouldHideNextButtonWhenNameIsNotEmpty() {
        val newText = ""

        presenter.onEntryNameTextChanged(newText)

        verify<View>(mockView).hideEntryNameButton()
        verify<View>(mockView, never()).showEntryNameButton()
    }

    @Test
    fun onEntryNameTextViewChangedShouldShowIconWhenNameIsNotEmpty() {
        val newText = ""

        presenter.onEntryNameTextChanged(newText)

        verify<View>(mockView).showEntryNameIcon()
        verify<View>(mockView, never()).hideEntryNameIcon()
    }

    @Test
    fun onEntryNameTextViewChangedShouldShowNextButtonWhenNameIsNotEmpty() {
        val newText = "1"

        presenter.onEntryNameTextChanged(newText)

        verify<View>(mockView).showEntryNameButton()
        verify<View>(mockView, never()).hideEntryNameButton()
    }

    @Test
    fun onEntryNameTextViewChangedShouldHideIconWhenNameIsNotEmpty() {
        val newText = "1"

        presenter.onEntryNameTextChanged(newText)

        verify<View>(mockView).hideEntryNameIcon()
        verify<View>(mockView, never()).showEntryNameIcon()
    }

    @Test
    fun onEntryNameSubmittedShouldShowEntryImageScreen() {
        val entryName = "Name"
        presenter.onEntryNameSubmitted(entryName)
        verify<View>(mockView).showEntryImageScreen(any<String>())
    }
}
