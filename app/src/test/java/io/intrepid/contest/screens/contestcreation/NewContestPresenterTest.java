package io.intrepid.contest.screens.contestcreation;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.BasePresenterTest;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewContestPresenterTest extends BasePresenterTest<NewContestPresenter> {

    @Mock
    NewContestMvpContract.View mockView;
    @Mock
    ContestCreationFragment mockChildFragment;
    @Mock
    Contest.Builder mockContestBuilder;
    private List<Category> categories;

    @Before
    public void setup() {
        categories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            categories.add(new Category("TEST TITLE " + i, "TEST DESCRIPTION " + i));
        }
        when(mockContestBuilder.getCategories()).thenReturn(categories);
        presenter = new NewContestPresenter(mockView, testConfiguration);
        presenter.contest = mockContestBuilder;
        presenter.onViewCreated();
    }

    @Test
    public void presenterCanBeCreatedSuccessfully() {
        assertTrue(presenter != null);
        assertTrue(presenter.getContest() != null);
        verify(mockView).showContestSubmissionPage(0);
    }

    @Test
    public void onBackButtonClickedFromFirstPageShouldCancelEdit() {
        presenter.onBackButtonClicked();
        verify(mockView).cancelEdit();
    }

    @Test
    public void onBackButtonClickedShouldNavigateBackwards() {
        when(mockView.getCurrentIndex()).thenReturn(2);
        presenter.onBackButtonClicked();
        verify(mockView).showContestSubmissionPage(1);
    }

    @Test
    public void onNextButtonClickedShouldNavigateForward() {
        when(mockView.getChildEditFragment(0)).thenReturn(mockChildFragment);
        mockView.showContestSubmissionPage(0);

        presenter.onNextButtonClicked();

        verify(mockChildFragment).onNextClicked();
    }

    @Test
    public void onNextDisabledShouldCauseViewToHideNextButton() {
        presenter.onNextStatusChanged(false);
        verify(mockView).setNextVisible(false);
    }

    @Test
    public void onNextEnabledShouldCauseViewToShowNextButton() {
        presenter.onNextStatusChanged(true);
        verify(mockView).setNextVisible(true);
    }

    @Test
    public void showNextScreenShouldTriggerViewToShowNextScreen() {
        when(mockView.getCurrentIndex()).thenReturn(1);
        presenter.showNextScreen();
        verify(mockView).showContestSubmissionPage(2);
    }

    @Test
    public void onNewCategoryAddedShouldCauseViewToShowUpdatedCategories() {
        presenter.onNewCategoryAdded("Category name", "Category description");
        verify(mockView).showUpdatedCategories();
    }

    @Test
    public void showAddCategoryScreenShouldCauseViewToNavigateToAddCategoryPage() {
        presenter.showAddCategoryScreen();
        verify(mockView).navigateToAddCategoryPage(eq(presenter.getContest()));
    }

    @Test
    public void onEditContestCaegoryShouldTriggerViewToUpdateCategory() {
        presenter.onContestEditEntered(0, "New Name", "New Description");
        verify(mockView).showUpdatedCategories();
    }
}

