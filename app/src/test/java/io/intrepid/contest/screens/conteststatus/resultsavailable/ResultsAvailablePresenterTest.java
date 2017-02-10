package io.intrepid.contest.screens.conteststatus.resultsavailable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.screens.conteststatus.resultsavailable.ResultsAvailableContract.Presenter;
import io.intrepid.contest.screens.conteststatus.resultsavailable.ResultsAvailableContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.functions.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResultsAvailablePresenterTest extends BasePresenterTest<ResultsAvailablePresenter> {
    @Mock
    View mockView;

    private Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new ResultsAvailablePresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewCreatedShouldRequestContestDetails() {
        presenter.onViewCreated();
        verify(mockView).requestContestDetails(any(), any());
    }

    @Test
    public void onViewCreatedShouldShowContestNameWhenApiCallDoesNotThrowError() {
        ArgumentCaptor<Consumer<ContestWrapper>> captor = ArgumentCaptor.forClass(Consumer.class);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ContestWrapper response = new ContestWrapper(new Contest());
                response.contest.setTitle("Contest title");
                captor.getValue().accept(response);
                return null;
            }
        }).when(mockView).requestContestDetails(captor.capture(), any());

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestName(any(String.class));
        verify(mockView, never()).showMessage(any(int.class));
    }

    @Test
    public void onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() {
        ArgumentCaptor<Consumer<Throwable>> captor = ArgumentCaptor.forClass(Consumer.class);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                captor.getValue().accept(new Throwable());
                return null;
            }
        }).when(mockView).requestContestDetails(any(), captor.capture());

        presenter.onViewCreated();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void onViewResultsButtonClickedShouldShowResultsPage() {
        presenter.onViewResultsButtonClicked();
        verify(mockView).showResultsPage();
    }
}
