package io.intrepid.contest.screens.contestcreation.categorieslist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoriesListPresenterTest extends BasePresenterTest<CategoriesListPresenter> {
    @Mock
    CategoriesListContract.ContestCreationFragment mockView;
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
            categories.add(new Category("Category " + i, "Category description " + i));
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
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Default name", "Default description"));
        when(mockContestBuilder.getCategories()).thenReturn(categories);

        presenter.displayCategories();

        verify(mockView).showCategories(argThat(argument -> argument.equals(categories)));
    }

    @Test
    public void displayCategoriesShouldShowCategoriesWhenThereAreCategories() {
        List<Category> categories = makeCategories();
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
        presenter.onNextClicked();
        verify(mockView).showNextScreen();
    }

    @Test
    public void onCategoryClickedShouldTriggerViewToShowEditPage() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Test", "Tester"));
        when(mockContestBuilder.getCategories()).thenReturn(categories);

        presenter.onCategoryClicked(categories.get(0));

        verify(mockView).showEditCategoryPage(categories.get(0), 0);
    }

    @Test
    public void onDeleteClickedShouldTriggerViewToLoseCategory() {
        int initialSize = mockContestBuilder.getCategories().size();
        presenter.onDeleteClicked(mockContestBuilder.getCategories().get(0));
        verify(mockView).showCategories(argThat(argument -> argument.size() == initialSize - 1));
    }

    @Test
    public void onCategoryMovedShouldUpdateCategoriesListWhenItemMovedUp() {
        List<Category> categories = makeCategories();
        when(mockContestBuilder.getCategories()).thenReturn(categories);

        presenter.onCategoryMoved(1, 4);

        assertTrue(categories.get(4).getName().equals("Category 1"));
        assertTrue(categories.get(1).getName().equals("Category 2"));
    }

    @Test
    public void onCategoryMovedShouldUpdateCategoriesListWhenItemMovedDown() {
        List<Category> categories = makeCategories();
        when(mockContestBuilder.getCategories()).thenReturn(categories);

        presenter.onCategoryMoved(4, 1);

        assertTrue(categories.get(1).getName().equals("Category 4"));
        assertTrue(categories.get(4).getName().equals("Category 3"));
    }
}
