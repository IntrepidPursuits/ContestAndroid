package io.intrepid.contest.screens.contestcreation.describecontest

import io.intrepid.contest.models.Contest
import io.intrepid.contest.screens.contestcreation.describecontest.DescribeContestContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DescribeContestPresenterTest : BasePresenterTest<DescribeContestPresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockContestBuilder: Contest.Builder

    @Before
    fun setup() {
        presenter = DescribeContestPresenter(mockView, testConfiguration, mockContestBuilder)
    }

    @Test
    fun onViewCreatedShouldCauseViewToEnableNext() {
        presenter.onViewCreated()
        verify<View>(mockView).setNextEnabled(true)
    }

    @Test
    fun onNextClickShouldTriggerViewToShowNextScreen() {
        val VALID_TEXT = "Valid Contest Name"
        presenter.onNextClicked(VALID_TEXT)
        verify<View>(mockView).showNextScreen()
    }
}
