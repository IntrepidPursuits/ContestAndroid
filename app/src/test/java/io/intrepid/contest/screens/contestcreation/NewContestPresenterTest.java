package io.intrepid.contest.screens.contestcreation;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewContestPresenterTest {

    @Mock
    NewContestMvpContract.View mockView;
    @Mock
    ContestCreationFragment mockChildFragment;
    private List<Category> categories;
    private NewContestPresenter newContestPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        newContestPresenter = new NewContestPresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
        newContestPresenter.onViewCreated();

        categories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            categories.add(new Category("TEST TITLE " + i, "TEST DESCRIPTION " + i));
        }
    }

    @Test
    public void presenterCanBeCreatedSuccessfully() {
        assertTrue(newContestPresenter != null);
        assertTrue(newContestPresenter.getContest() != null);
        verify(mockView).showContestSubmissionPage(0);
    }

    @Test
    public void onBackButtonClickedFromFirstPageShouldCancelEdit() {
        newContestPresenter.onBackButtonClicked();
        verify(mockView).cancelEdit();
    }

    @Test
    public void onBackButtonClickedShouldNavigateBackwards() {
        when(mockView.getCurrentIndex()).thenReturn(2);
        newContestPresenter.onBackButtonClicked();
        verify(mockView).showContestSubmissionPage(1);
    }

    @Test
    public void onNextButtonClickedShouldNavigateForward() {
        when(mockView.getChildEditFragment(0)).thenReturn(mockChildFragment);
        mockView.showContestSubmissionPage(0);

        newContestPresenter.onNextButtonClicked();

        verify(mockChildFragment).onNextClicked();
    }

    @Test
    public void onNextDisabledShouldCauseViewToHideNextButton() {
        newContestPresenter.onNextStatusChanged(false);
        verify(mockView).setNextVisible(false);
    }

    @Test
    public void onNextEnabledShouldCauseViewToShowNextButton() {
        newContestPresenter.onNextStatusChanged(true);
        verify(mockView).setNextVisible(true);
    }

    @Test
    public void showNextScreenShouldTriggerViewToShowNextScreen() {
        when(mockView.getCurrentIndex()).thenReturn(1);
        newContestPresenter.showNextScreen();
        verify(mockView).showContestSubmissionPage(2);
    }

    @Test
    public void onNewCategoryAddedShouldCauseViewToShowUpdatedCategories() {
        newContestPresenter.onNewCategoryAdded("Category name", "Category description");
        verify(mockView).showUpdatedCategories();
    }

    @Test
    public void showAddCategoryScreenShouldCauseViewToNavigateToAddCategoryPage() {
        newContestPresenter.showAddCategoryScreen();
        verify(mockView).navigateToAddCategoryPage(eq(newContestPresenter.getContest()));
    }
}

