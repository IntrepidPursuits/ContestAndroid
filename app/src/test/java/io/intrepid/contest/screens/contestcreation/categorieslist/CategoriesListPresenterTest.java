package io.intrepid.contest.screens.contestcreation.categorieslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoriesListPresenterTest {
    @Mock
    CategoriesContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;
    private CategoriesContract.Presenter categoriesListPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        categoriesListPresenter = new CategoriesListPresenter(mockView,
                                                              TestPresenterConfiguration.createTestConfiguration(),
                                                              mockContestBuilder);
    }

    @Test
    public void displayCategoriesShouldShowDefaultCategoryWhenThereAreNoCategories() {
        when(mockContestBuilder.getCategories()).thenReturn(new ArrayList<>());
        categoriesListPresenter.displayCategories();
        verify(mockView).showDefaultCategory();
    }

    @Test
    public void displayCategoriesShouldShowCategories() {
        categoriesListPresenter.displayCategories();
        verify(mockView).showDefaultCategory();
    }

    @Test
    public void displayCategoriesShouldShowCategoriesContestshasCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category(" ", " "));
        }

        when(mockContestBuilder.getCategories()).thenReturn(categories);
        categoriesListPresenter.displayCategories();

        verify(mockView).showCategories(categories);
    }

    @Test
    public void onAddCategoryClickedShouldCauseViewToShowAdd() {
        categoriesListPresenter.onAddCategoryClicked();
        verify(mockView).showAddCategoryScreen();
    }

    @Test
    public void onNextClickedShouldTriggerViewToShowNextScreen() {
        List<Category> categoryList = new ArrayList<>();
        categoriesListPresenter.onNextClicked(categoryList);
        verify(mockView).showNextScreen();
    }
}
