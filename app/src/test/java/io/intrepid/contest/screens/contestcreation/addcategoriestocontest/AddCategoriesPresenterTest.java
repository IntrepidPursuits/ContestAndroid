package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

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
public class AddCategoriesPresenterTest {
    @Mock
    AddCategoriesContract.View mockView;
    private AddCategoriesContract.Presenter addCategoryPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        addCategoryPresenter = new AddCategoriesPresenter(mockView,
                                                          TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void presenterCanBeCreated() {
        assertTrue(addCategoryPresenter != null);
    }

    @Test
    public void onNextClickedShouldTriggerViewToAddCategory(){
        String categoryName = "NewCategory";
        String categoryDescription = "NewCategory Description";
        addCategoryPresenter.onNextClicked(categoryName, categoryDescription);
        verify(mockView).addCategory(eq(new Category(categoryName, categoryDescription)));
    }
}


