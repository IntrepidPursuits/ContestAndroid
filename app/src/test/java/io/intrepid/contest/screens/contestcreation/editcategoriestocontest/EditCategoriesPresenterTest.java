package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.testutils.BasePresenterTest;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class EditCategoriesPresenterTest extends BasePresenterTest<EditCategoriesPresenter> {

    @Mock
    EditCategoriesContract.View mockView;

    @Before
    public void setup() {
        prepareEditCategoryPresenter();
    }

    //For testing presenter in Edit Existing Category Mode
    private void prepareEditCategoryPresenter() {
        presenter = new EditCategoriesPresenter(mockView,
                                                testConfiguration,
                                                new Category("a", "d"));
    }

    //For testing Presenter in Add Category Mode
    private void prepareAddCategoryPresenter() {
        presenter = new EditCategoriesPresenter(mockView,
                                                testConfiguration,
                                                null);
    }

    @Test
    public void presenterCanBeCreated() {
        assertTrue(presenter != null);
    }

    @Test
    public void onViewCreatedShouldDisplayPreExistingCategoryDetails() {
        presenter.onViewCreated();
        verify(mockView).showEditableCategory(any(), any());
    }


    @Test
    public void onNextClickedShouldTriggerViewToEditCategory() {
        String categoryName = "NewCategory";
        String categoryDescription = "NewCategory Description";

        presenter.onNextClicked(categoryName, categoryDescription);

        verify(mockView).editCategory(any(), eq(categoryName), eq(categoryDescription));
    }

    @Test
    public void onViewCreatedShouldNeverEditinAddCategoryMode() {
        prepareAddCategoryPresenter();
        presenter.onViewCreated();
        verify(mockView, never()).showEditableCategory(any(), any());
    }

    @Test
    public void onNextClickedShouldTriggerViewToAddCategoryWhenParamsAreNull() {
        prepareAddCategoryPresenter();
        String categoryName = "NewCategory";
        String categoryDescription = "NewCategory Description";

        presenter.onNextClicked(categoryName, categoryDescription);

        verify(mockView).addCategory(any());
    }
}
