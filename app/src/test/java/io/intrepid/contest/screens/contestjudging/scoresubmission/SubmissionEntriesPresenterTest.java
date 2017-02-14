package io.intrepid.contest.screens.contestjudging.scoresubmission;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import io.intrepid.contest.models.Entry;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.Mockito.verify;

public class SubmissionEntriesPresenterTest extends BasePresenterTest<SubmissionEntriesPresenter> {
    @Mock
    SubmissionEntriesContract.View mockView;
    @Mock
    List<Entry> mockEntries;

    @Before
    public void setup() {
        presenter = new SubmissionEntriesPresenter(mockView, testConfiguration, mockEntries);
    }

    @Test
    public void onViewCreatedShouldTriggerViewToDisplayListOfEntries() {
        presenter.onViewCreated();
        verify(mockView).showSubmissionList(mockEntries);
    }
}
