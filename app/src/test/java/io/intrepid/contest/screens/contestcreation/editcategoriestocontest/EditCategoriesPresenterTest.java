package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EditCategoriesPresenterTest {
    @Mock
    EditCategoriesContract.View mockView;
    private EditCategoriesContract.Presenter editCategoryPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        editCategoryPresenter = new EditCategoriesPresenter(mockView,
                                                           TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void presenterCanBeCreated() {
        assertTrue(editCategoryPresenter != null);
    }

    @Test
    public void onNextClickedShouldTriggerViewToAddCategory() {
        String categoryName = "NewCategory";
        String categoryDescription = "NewCategory Description";
        editCategoryPresenter.onNextClicked(categoryName, categoryDescription);
        verify(mockView).addCategory(eq(new Category(categoryName, categoryDescription)));
    }
}


