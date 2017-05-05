package io.intrepid.contest.screens.contestcreation.editcategoriestocontest

import io.intrepid.contest.models.Category
import io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoriesContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class EditCategoriesPresenterTest : BasePresenterTest<EditCategoriesPresenter>() {

    @Mock
    private lateinit var mockView: View

    @Before
    fun setup() {
        prepareEditCategoryPresenter()
    }

    //For testing presenter in Edit Existing Category Mode
    private fun prepareEditCategoryPresenter() {
        presenter = EditCategoriesPresenter(mockView, testConfiguration, Category("a", "d"), 0)
    }

    //For testing Presenter in Add Category Mode
    private fun prepareAddCategoryPresenter() {
        presenter = EditCategoriesPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldDisplayPreExistingCategoryDetails() {
        presenter.onViewCreated()
        verify<View>(mockView).showEditableCategory(any<String>(), any<String>())
    }

    @Test
    fun onNextPageButtonClickedShouldTriggerViewToEditCategory() {
        prepareEditCategoryPresenter()
        val categoryName = "NewCategory"
        val categoryDescription = "NewCategory Description"

        presenter.onNextPageButtonClicked(categoryName, categoryDescription)

        verify<View>(mockView).editCategory(anyInt(), eq(categoryName), eq(categoryDescription))
    }

    @Test
    fun onViewCreatedShouldNeverEditInAddCategoryMode() {
        prepareAddCategoryPresenter()
        presenter.onViewCreated()
        verify<View>(mockView, never()).showEditableCategory(any<String>(), any<String>())
    }

    @Test
    fun onNextPageButtonClickedShouldTriggerViewToAddCategoryWhenInAddMode() {
        prepareAddCategoryPresenter()
        val categoryName = "NewCategory"
        val categoryDescription = "NewCategory Description"

        presenter.onNextPageButtonClicked(categoryName, categoryDescription)

        verify<View>(mockView).addCategory(any<Category>())
    }

    @Test
    fun onCategoryNameChangedShouldTriggerViewToSetNextVisibleWhenInEditMode() {
        prepareEditCategoryPresenter()
        presenter.onCategoryNameChanged("TEST_A_NEW_NAME")
        verify<View>(mockView).onNextPageEnabledChanged()
    }

    @Test
    fun onCategoryNameChangedShouldTriggerViewToSetNextInvisibleWhenNameIsEmpty() {
        prepareEditCategoryPresenter()
        val EMPTY_TEXT = ""

        presenter.onCategoryNameChanged(EMPTY_TEXT)

        verify<View>(mockView).onNextPageEnabledChanged()
    }
}
