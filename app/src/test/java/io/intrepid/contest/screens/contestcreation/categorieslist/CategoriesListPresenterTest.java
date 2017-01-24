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

public class CategoriesListPresenterTest {
    @Mock
    CategoriesContract.View mockView;
    private CategoriesContract.Presenter categoriesListPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        categoriesListPresenter = new CategoriesListPresenter(mockView,
                                                              TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void displayCategoriesShouldShowDefaultCategoryWhenThereAreNoCategories() {
        categoriesListPresenter.displayCategories(new Contest());
        verify(mockView).showDefaultCategory();
    }

    @Test
    public void displayCategoriesShouldShowCategoriesWhenContestIsNull() {
        categoriesListPresenter.displayCategories(null);
        verify(mockView).showDefaultCategory();
    }

    @Test
    public void displayCategoriesShouldShowCategoriesContestshasCategories() {
        Contest.Builder contestBuilder = new Contest.Builder();
        contestBuilder.categories.add(new Category("TEST TITLE", "TEST DESCRIPTION"));

        categoriesListPresenter.displayCategories(contestBuilder.build());

        verify(mockView).showCategories(contestBuilder.categories);
    }

    @Test
    public void onNextClickedShouldTriggerViewToCallSubmitCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoriesListPresenter.onNextClicked(categoryList);
        verify(mockView).submitCategories(categoryList);
    }
}
