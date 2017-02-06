package io.intrepid.contest.screens.conteststatus.waitingsubmissions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.screens.conteststatus.waitingsubmissions.WaitingSubmissionsContract.Presenter;
import io.intrepid.contest.screens.conteststatus.waitingsubmissions.WaitingSubmissionsContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WaitingSubmissionsPresenterTest extends BasePresenterTest<WaitingSubmissionsPresenter> {
    @Mock
    View mockView;

    private Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new WaitingSubmissionsPresenter(mockView, testConfiguration);
    }

    @Test
    public void onNumSubmissionsMissingUpdatedShouldShowNumSubmissionsMissing() {
        presenter.onNumSubmissionsMissingUpdated(1);
        verify(mockView).showNumSubmissionsMissing(any(int.class));
    }
}
