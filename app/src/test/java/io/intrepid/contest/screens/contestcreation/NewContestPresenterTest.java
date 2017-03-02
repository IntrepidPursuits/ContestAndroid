package io.intrepid.contest.screens.contestcreation;


import android.support.annotation.StringRes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewContestPresenterTest extends BasePresenterTest<NewContestPresenter> {

    @Mock
    NewContestMvpContract.View mockView;
    @Mock
    ContestCreationFragment mockChildFragment;
    @Mock
    ValidatableContestCreationFragment mockValidatableContestCreationFragment;
    @Mock
    Contest.Builder mockContestBuilder;
    private List<Category> categories;


    @Before
    public void setup() {
        categories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            categories.add(new Category("TEST TITLE " + i, "TEST DESCRIPTION " + i));
        }
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
        when(mockContestBuilder.getCategories()).thenReturn(categories);
        presenter.onContestEditEntered(0, "New Name", "New Description");
        verify(mockView).showUpdatedCategories();
    }

    @Test
    public void onPageSelectedShouldCauseViewToShowCorrectPageTitle() {
        testOnPageScrolledWithExpectedPageTitleStringResource(0, R.string.new_contest);
        testOnPageScrolledWithExpectedPageTitleStringResource(1, R.string.description);
        testOnPageScrolledWithExpectedPageTitleStringResource(2, R.string.scoring_categories);
        testOnPageScrolledWithExpectedPageTitleStringResource(3, R.string.review_contest);
    }

    @Test
    public void onPageScrolledShouldCauseViewToShowCorrectPage() {
        when(mockView.getChildEditFragment(anyInt())).thenReturn(mockChildFragment);
        presenter.onPageScrolled(0, 0, 0);
        verify(mockView).setPageTitle(R.string.new_contest);
    }

    @Test
    public void onPageScrollStateChangedShouldCauseViewToShowCorrectPageTitle() {
        when(mockView.getCurrentIndex()).thenReturn(2);
        presenter.onPageScrollStateChanged(1);
        verify(mockView).setPageTitle(R.string.scoring_categories);
    }

    @Test
    public void onPageChangedToValidatableViewShouldTriggerViewToDoOnFocus() {
        when(mockView.getChildEditFragment(anyInt())).thenReturn(mockValidatableContestCreationFragment);
        presenter.onPageSelected(2);
        verify(mockValidatableContestCreationFragment).onFocus();
    }

    @Test
    public void submitContestSuccessfullyShouldTriggerViewToNavigateToInviteScreen() {
        ContestWrapper contestResponse = new ContestWrapper(new Contest());
        when(mockRestApi.submitContest(any())).thenReturn(Observable.just(contestResponse));
        when(mockView.getCurrentIndex()).thenReturn(NewContestPresenter.LAST_PAGE_INDEX);

        presenter.showNextScreen();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).navigateToSendInvitationsScreen();
    }

    @Test
    public void submitContestFailureShouldTriggerViewToShowErrorMessage() {
        when(mockRestApi.submitContest(any())).thenReturn(Observable.error(new Throwable()));
        when(mockView.getCurrentIndex()).thenReturn(NewContestPresenter.LAST_PAGE_INDEX);

        presenter.showNextScreen();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(R.string.error_api);
    }

    private void testOnPageScrolledWithExpectedPageTitleStringResource(int pageToNavigateTo, @StringRes int pageTitle) {
        when(mockView.getChildEditFragment(anyInt())).thenReturn(mockChildFragment);
        presenter.onPageSelected(pageToNavigateTo);
        verify(mockView).setPageTitle(pageTitle);
    }
}
