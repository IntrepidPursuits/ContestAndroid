package io.intrepid.contest.screens.adminstatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestStatus;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.END_CONTEST;
import static io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.START_CONTEST;
import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminStatusPresenterTest extends BasePresenterTest<AdminStatusPresenter> {

    @Mock
    AdminStatusContract.View mockView;
    @Mock
    ContestStatus mockStatus;

    @Before
    public void setup() {
        setupSuccessfulApiResponse();
        presenter = new AdminStatusPresenter(mockView, testConfiguration);
    }

    private void setupSuccessfulApiResponse() {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.getContestDetails(any())).thenReturn(Observable.just(new ContestWrapper(new Contest())));
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = mockStatus;
        when(mockRestApi.getContestStatus(any())).thenReturn(Observable.just(response))
        ;
    }

    @Test
    public void onViewCreatedShouldCauseViewToShowAwaitSubmissionsIndicator() {
        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
        verify(mockView).showAwaitSubmissionsIndicator(anyBoolean());
    }

    @Test
    public void onBottonNavigationButtonClickedShouldCauseToAdvanceToJudging() {
        presenter.onBottomNavigationButtonClicked();
        testConfiguration.triggerRxSchedulers();
        verify(mockView).advanceToJudgingIndicator();
    }

    @Test
    public void onViewCreatedShouldCauseViewToShowSubmittedEntries() {
        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();
        verify(mockView).showSubmittedEntries(any());
    }

    @Test
    public void onViewCreatedShouldCauseViewToShowErrorMessageWhenGetContestDetailsReturnsThrowable() {
        when(mockRestApi.getContestDetails(any())).thenReturn(error(new Throwable()));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(R.string.error_api);
    }

    @Test
    public void onViewCreatedShouldCauseViewToShowErrorMessageWhenGetContestStatusReturnsThrowable() {
        when(mockRestApi.getContestStatus(any())).thenReturn(error(new Throwable()));

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(R.string.error_api);
    }

    @Test
    public void onBottomNavigationButtonClickedShouldCauseViewToShowConfirmStartContestDialog() {
        when(mockStatus.getNumSubmissionsMissing()).thenReturn(6);
        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        presenter.onBottomNavigationButtonClicked();

        verify(mockView).showConfirmStartContestDialog();
        verify(mockView, never()).advanceToJudgingIndicator();
    }

    @Test
    public void onBottomNavigationButtonClickedShouldCauseViewToAdvanceJudgingIndicatorIfNoMissingEntries() {
        presenter.onBottomNavigationButtonClicked();
        verify(mockView).advanceToJudgingIndicator();
    }

    @Test
    public void onBottomNavClickedTwiceShouldCauseViewToShowConfirmEndContestDialogIfMissingJudges() {
        when(mockStatus.getNumScoresMissing()).thenReturn(5);
        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        //Click to start contest
        presenter.onBottomNavigationButtonClicked();
        //Then click to end contest
        presenter.onBottomNavigationButtonClicked();

        verify(mockView).showConfirmEndContestDialog();
    }

    @Test
    public void onBottomNavClickedTwiceShouldCauseViewToAdvanceToEndOfContestIndicatorWhenNoMissingJudges() {
        presenter.onBottomNavigationButtonClicked();
        presenter.onBottomNavigationButtonClicked();

        verify(mockView).showJudgingStatusIndicator(false);
        verify(mockView).showEndOfContestIndicator(true);
    }

    @Test
    public void onPositiveButtonClickedOnStartContestDialogShouldCauseViewToAdvanceToJudgingIndicator() {
        presenter.onPositiveButtonClicked(START_CONTEST);
        verify(mockView).advanceToJudgingIndicator();
    }

    @Test
    public void onPositiveButtonClickedOnEndContestDialogShouldCauseViewToAdvanceToEndOfContestIndicator() {
        presenter.onPositiveButtonClicked(END_CONTEST);

        verify(mockView).showJudgingStatusIndicator(false);
        verify(mockView).showEndOfContestIndicator(true);
    }

    @Test
    public void onNegativeButtonClickedShouldDismissDialogAndDoNothing() {
        presenter.onNegativeButtonClicked(START_CONTEST);
        presenter.onNegativeButtonClicked(END_CONTEST);

        verify(mockView, never()).showJudgingStatusIndicator(anyBoolean());
        verify(mockView, never()).showEndOfContestIndicator(anyBoolean());
    }

    @Test
    public void onBackPressedShouldCauseViewToExitAdminStatusScreen() {
        presenter.onBackPressed();
        verify(mockView).exitStatusScreen();
    }
}
