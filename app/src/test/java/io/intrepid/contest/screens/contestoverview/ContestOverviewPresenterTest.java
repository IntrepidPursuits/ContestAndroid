package io.intrepid.contest.screens.contestoverview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.UUID;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.screens.contestoverview.ContestOverviewContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContestOverviewPresenterTest extends BasePresenterTest<ContestOverviewPresenter> {
    @Mock
    View mockView;
    @Mock
    Contest mockContest;

    @Before
    public void setup() {
        presenter = new ContestOverviewPresenter(mockView, testConfiguration);

        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
    }

    private void setupSuccessfulContestDetailsCall() {
        ContestWrapper response = new ContestWrapper(mockContest);
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(response));
    }

    private void setupFailedContestDetailsCall() {
        when(mockRestApi.getContestDetails(any())).thenReturn(error(new Throwable()));
    }

    @Test
    public void onViewCreatedShouldShowErrorOnContestDetailCallFailure() {
        setupFailedContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(R.string.error_api);
    }

    @Test
    public void onViewCreatedShouldShowRatingGuide() throws Exception {
        //Not a pre-condition for this test but is required to make sure onViewCreated does not fail
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showCategoriesAndWeights(any(), any());
    }

    @Test
    public void showContestDescription() throws Exception {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestDescription(any());
    }

    @Test
    public void onViewCreatedShouldShowCategories() {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
        verify(mockView).showCategoriesAndWeights(anyList(), anyList());
    }

    @Test
    public void onViewCreatedShouldCauseVIewToShowContestTitle() {
        when(mockContest.getEntries()).thenReturn(new ArrayList<>());
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showTitle(anyInt(), any());
    }

    @Test
    public void onViewCreatedShouldCauseViewToShowSubmissionCountMessage() {
        setupSuccessfulContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
        verify(mockView).showSubmissionCountMessage(anyInt(), anyInt());
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenContestDetailsCallThrowsError() throws Exception {
        setupFailedContestDetailsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void onOverviewSubmitButtonClickedShouldOpenJudgingScreen() {
        setupSuccessfulContestDetailsCall();
        presenter.onOverViewSubmitButtonClicked();
        verify(mockView).advanceToJudgingScreen();
    }

    @Test
    public void onBackPressedShouldCauseViewToReturnToSplashScreen() {
        presenter.onBackPressed();
        verify(mockView).returnToSplashScreen();
    }
}
