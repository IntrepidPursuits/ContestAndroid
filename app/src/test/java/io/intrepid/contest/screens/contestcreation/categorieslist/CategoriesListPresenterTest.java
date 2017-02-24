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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoriesListPresenterTest extends BasePresenterTest<CategoriesListPresenter> {
    @Mock
    CategoriesListContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;

    @Before
    public void setup() {
        when(mockContestBuilder.getCategories()).thenReturn(makeCategories());
        presenter = new CategoriesListPresenter(mockView,
                                                testConfiguration,
                                                mockContestBuilder);
    }

    private List<Category> makeCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new Category("Test", "Tester"));
        }
        return categories;
    }

    @Test
    public void presenterShouldAddDefaultCategoryIfCategoriesIsEmptyOrNull() {
        when(mockContestBuilder.getCategories()).thenReturn(new ArrayList<>());
        presenter = new CategoriesListPresenter(mockView, testConfiguration, mockContestBuilder);
        verify(mockView).getDefaultCategory(anyInt(), anyInt());
    }

    @Test
    public void onViewBoundShouldEnableNextWhenCategoriesIsNotEmpty() {
        presenter.onViewBound();
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onViewBoundShouldDisableNextWhenCategoriesIsEmpty() {
        when(mockContestBuilder.getCategories()).thenReturn(new ArrayList<>());
        presenter.onViewBound();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onDeleteClickedShouldNeverDisableWhenCategoriesIsNotEmpty() {
        List<Category> categories = mockContestBuilder.getCategories();
        for (int i = 0; i < categories.size() - 1; i++) {
            presenter.onDeleteClicked(categories.get(i));
        }

        verify(mockView, never()).setNextEnabled(false);
        verify(mockView, times(categories.size() - 1)).setNextEnabled(true);
    }

    @Test
    public void onDeleteClickedShouldDisableNextWhenCategoriesIsEmpty() {
        List<Category> singleCategoryList = new ArrayList<>();
        Category singleCategory = new Category("Single Category", "TEST");
        singleCategoryList.add(singleCategory);
        when(mockContestBuilder.getCategories()).thenReturn(new ArrayList<>());

        presenter.onDeleteClicked(singleCategory);

        verify(mockView).setNextEnabled(false);
        verify(mockView, never()).setNextEnabled(true);
    }

    @Test
    public void onViewShouldCreatedShouldTriggerViewToShowCategories() {
        presenter.onViewCreated();
        verify(mockView).showCategories(any());
    }

    @Test
    public void onViewBoundShouldTriggerViewToSetNextEnabled() {
        presenter.onViewBound();
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onViewBoundShouldTriggerViewToDisableNextWhenCategoriesIsEmpty() {
        when(mockContestBuilder.getCategories()).thenReturn(new ArrayList<>());
        presenter.onViewBound();
        verify(mockView).setNextEnabled(false);
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
        Category category = new Category("Test", "Tester");
        presenter.onCategoryClicked(category);
        verify(mockView).showEditCategoryPage(category, 0);
    }

    @Test
    public void onDeleteClickedShouldTriggerViewToLoseCategory() {
        int initialSize = mockContestBuilder.getCategories().size();
        presenter.onDeleteClicked(mockContestBuilder.getCategories().get(0));
        verify(mockView).showCategories(argThat(argument -> argument.size() == initialSize - 1));
    }
}
