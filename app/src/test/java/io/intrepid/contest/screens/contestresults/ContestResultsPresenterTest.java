package io.intrepid.contest.screens.contestresults;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.rest.ContestResultResponse;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContestResultsPresenterTest extends BasePresenterTest<ContestResultsPresenter> {
    @Mock
    ContestResultsContract.View mockView;

    @Before
    public void setup() {
        presenter = new ContestResultsPresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewCreatedShouldHideNoEntriesMessageWhenSuccessfullyRetrievedResultsWithEntries() {
        setupSuccessfulContestResultsCall(getValidContestResultResponse());

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).hideNoEntriesMessage();
    }

    @Test
    public void onViewCreatedShouldShowResultsWhenSuccessfullyRetrievedResultsWithEntries() {
        ContestResultResponse resultResponse = getValidContestResultResponse();
        setupSuccessfulContestResultsCall(resultResponse);

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showResults(resultResponse.contestResults.rankedScoredEntries);
    }

    @Test
    public void onViewCreatedShouldShowNeverHideNoEntriesMessageWhenSuccessfullyRetrievedResultsWithoutEntries() {
        setupSuccessfulContestResultsCall(new ContestResultResponse());

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView, never()).hideNoEntriesMessage();
    }

    @Test
    public void onViewCreatedShouldShowNeverShowResultsWhenSuccessfullyRetrievedResultsWithoutEntries() {
        setupSuccessfulContestResultsCall(new ContestResultResponse());

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView, never()).showResults(anyList());
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenItemIsSendInvitationsAndApiCallThrowsError()
            throws HttpException {
        setupUnsuccessfulContestResultsCall();

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(anyInt());
    }

    private void setupUnsuccessfulContestResultsCall() {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.getContestResults(any())).thenReturn(error(new Throwable()));
    }

    private void setupSuccessfulContestResultsCall(ContestResultResponse resultResponse) {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.getContestResults(any())).thenReturn(Observable.just(resultResponse));
    }

    private ContestResultResponse getValidContestResultResponse() {
        List<RankedEntryResult> rankedScoredEntries = new ArrayList<>();
        rankedScoredEntries.add(new RankedEntryResult());
        rankedScoredEntries.add(new RankedEntryResult());
        rankedScoredEntries.add(new RankedEntryResult());
        return new ContestResultResponse(rankedScoredEntries);
    }
}
