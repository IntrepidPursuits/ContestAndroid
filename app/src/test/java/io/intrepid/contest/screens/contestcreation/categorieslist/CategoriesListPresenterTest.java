package io.intrepid.contest.screens.contestcreation.categorieslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoriesListPresenterTest extends BasePresenterTest<CategoriesListPresenter> {
    @Mock
    CategoriesListContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;

    @Before
    public void setup() {
        presenter = new CategoriesListPresenter(mockView,
                                                testConfiguration,
                                                mockContestBuilder);
        when(mockContestBuilder.getCategories()).thenReturn(makeCategories());
    }

    private List<Category> makeCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category("Test", "Tester"));
        }
        return categories;
    }

    @Test
    public void onViewShouldCreatedShouldTriggerViewToShowCategories() {
        presenter.onViewCreated();
        verify(mockView).showCategories(any());
    }

    @Test
    public void displayCategoriesShouldShowDefaultCategoryWhenThereAreNoCategories() {
        presenter.displayCategories();
        verify(mockView).showCategories(any());
    }

    @Test
    public void displayCategoriesShouldShowCategories() {
        presenter.displayCategories();
        verify(mockView).showCategories(any());
    }

    @Test
    public void displayCategoriesShouldShowCategoriesContestshasCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category(" ", " "));
        }

        when(mockContestBuilder.getCategories()).thenReturn(categories);
        presenter.displayCategories();

        verify(mockView).showCategories(categories);
    }

    @Test
    public void onAddCategoryClickedShouldCauseViewToShowAdd() {
        presenter.onAddCategoryClicked();
        verify(mockView).showAddCategoryScreen();
    }

    @Test
    public void onNextClickedShouldTriggerViewToShowNextScreen() {
        List<Category> categoryList = new ArrayList<>();
        presenter.onNextClicked(categoryList);
        verify(mockView).showNextScreen();
    }

    @Test
    public void onCategoryClickedShouldTriggerViewToShowEditPage() {
        Category category = mockContestBuilder.getCategories().get(0);
        presenter.onCategoryClicked(category);
        verify(mockView).showEditCategoryPage(category);
    }

    @Test
    public void onDeleteClickedShouldTriggerViewToLoseCategory() {
        int initialSize = mockContestBuilder.getCategories().size();
        presenter.onDeleteClicked(mockContestBuilder.getCategories().get(0));
        verify(mockView).showCategories(argThat(argument -> argument.size() == initialSize - 1));
    }
}
