package io.intrepid.contest.screens.contestresults.shareresults;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

public class ShareResultsPresenterTest extends BasePresenterTest<ShareResultsPresenter> {
    @Mock ShareResultsContract.View mockView;
    @Mock
    List<RankedEntryResult> mockResults;

    @Before
    public void setup() {
        presenter = new ShareResultsPresenter(mockView, testConfiguration, mockResults);
    }

    @Test
    public void onViewCreatedShouldShowResultsList() {
        presenter.onViewCreated();
        verify(mockView).showResultsList(anyList());
    }

    @Test
    public void onSaveResultsClickedShouldCauseMockViewToCaptureScreenshot() {
        presenter.onSaveResultsClicked();
        verify(mockView).captureScreenshot();
    }

    @Test
    public void onShareResultsClickedShouldCauseMockViewToShare() {
        presenter.onShareResultsClicked();
        verify(mockView).setShareOptions(any());
    }
}