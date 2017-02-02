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
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
        assertTrue(newContestPresenter.contest != null);
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
        newContestPresenter.setContestName(" d ");

        newContestPresenter.onBackButtonClicked();

        verify(mockView).showContestSubmissionPage(1);
    }

    @Test
    public void onBackTwo() {
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
    public void setContestNameShouldModifyContestName() {
        String newContestName = "New Contest";

        newContestPresenter.setContestName(newContestName);

        verify(mockView).showContestSubmissionPage(1);
        assertEquals(newContestName, newContestPresenter.contest.title);
    }

    @Test
    public void setContestDescriptionShouldModifyContestDescription() {
        mockView.showContestSubmissionPage(1);
        String newDescription = "testing";
        newContestPresenter.setContestDescription(newDescription);
        assertEquals(newDescription, newContestPresenter.contest.description);
    }

    @Test
    public void setCategoriesShouldModifyContestCategories() {
        newContestPresenter.setCategories(categories);
        assertTrue(newContestPresenter.contest.categories == categories);
    }

    @Test
    public void addCategoryShouldIncrementCategoriesSize() {
        int initialCategorySize = newContestPresenter.contest.categories.size();

        newContestPresenter.addCategory(new Category("", ""));
        int expectedSize = newContestPresenter.contest.categories.size();

        assertEquals(expectedSize, initialCategorySize + 1);
    }

    @Test
    public void setCategoriesShouldEndTheForm() {
        mockView.showContestSubmissionPage(3);
        newContestPresenter.setCategories(new ArrayList<>());
        verify(mockView).completeEditForm(any(Contest.class));
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
}

