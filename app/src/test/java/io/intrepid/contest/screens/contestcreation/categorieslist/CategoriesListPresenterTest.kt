package io.intrepid.contest.screens.contestcreation.categorieslist

import io.intrepid.contest.models.Category
import io.intrepid.contest.models.Contest
import io.intrepid.contest.screens.contestcreation.categorieslist.CategoriesListContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.*

class CategoriesListPresenterTest : BasePresenterTest<CategoriesListPresenter>() {
    @Mock
    private lateinit var mockView: View

    private lateinit var contestBuilder: Contest.Builder

    @Before
    fun setup() {
        contestBuilder = Contest.Builder().apply {
            categories = makeCategories()
        }
        presenter = CategoriesListPresenter(mockView, testConfiguration, contestBuilder)
    }

    @Test
    fun presenterShouldAddDefaultCategoryIfCategoriesIsEmptyOrNull() {
        contestBuilder.categories = mutableListOf()
        presenter = CategoriesListPresenter(mockView, testConfiguration, contestBuilder)
        verify<View>(mockView).getDefaultCategory(anyInt(), anyInt())
    }

    @Test
    fun onViewBoundShouldEnableNextWhenCategoriesIsNotEmpty() {
        presenter.onViewBound()
        verify<View>(mockView).setNextEnabled(true)
    }

    @Test
    fun onViewBoundShouldDisableNextWhenCategoriesIsEmpty() {
        contestBuilder.categories = mutableListOf()
        presenter.onViewBound()
        verify<View>(mockView).setNextEnabled(false)
    }

    @Test
    fun onDeleteClickedShouldNeverDisableWhenCategoriesIsNotEmpty() {
        val categories = contestBuilder.categories

        var i = 0
        while (i < (categories.size - 1)) {
            presenter.onDeleteClicked(categories[i++])
        }

        verify<View>(mockView, never()).setNextEnabled(false)
        verify<View>(mockView, times(categories.size - 1)).setNextEnabled(true)
    }

    @Test
    fun onDeleteClickedShouldDisableNextWhenCategoriesIsEmpty() {
        contestBuilder.categories = mutableListOf()
        val singleCategory = Category("Single Category", "TEST")

        presenter.onDeleteClicked(singleCategory)

        verify<View>(mockView).setNextEnabled(false)
        verify<View>(mockView, never()).setNextEnabled(true)
    }

    @Test
    fun onViewShouldCreatedShouldTriggerViewToShowCategories() {
        presenter.onViewCreated()
        verify<View>(mockView).showCategories(any<List<Category>>())
    }

    @Test
    fun onViewBoundShouldTriggerViewToSetNextEnabled() {
        presenter.onViewBound()
        verify<View>(mockView).setNextEnabled(true)
    }

    @Test
    fun onViewBoundShouldTriggerViewToDisableNextWhenCategoriesIsEmpty() {
        contestBuilder.categories = mutableListOf()
        presenter.onViewBound()
        verify<View>(mockView).setNextEnabled(false)
    }

    @Test
    fun onViewBoundShouldTriggerViewToFocus() {
        presenter.onViewBound()
        verify<View>(mockView).onFocus()
    }

    @Test
    fun displayCategoriesShouldShowDefaultCategoryWhenThereAreNoCategories() {
        val categories = listOf(Category("Default name", "Default description"))
        contestBuilder.categories = categories

        presenter.displayCategories()

        verify<View>(mockView).showCategories(argThat<List<Category>> { argument -> argument == categories })
    }

    @Test
    fun displayCategoriesShouldShowCategoriesWhenThereAreCategories() {
        presenter.displayCategories()
        verify<View>(mockView).showCategories(contestBuilder.categories)
    }

    @Test
    fun onAddCategoryClickedShouldCauseViewToShowAdd() {
        presenter.onAddCategoryClicked()
        verify<View>(mockView).showAddCategoryScreen()
    }

    @Test
    fun onNextClickedShouldTriggerViewToShowNextScreen() {
        presenter.onNextClicked()
        verify<View>(mockView).showNextScreen()
    }

    @Test
    fun onCategoryClickedShouldTriggerViewToShowEditPage() {
        val categories = listOf(Category("Test", "Tester"))
        contestBuilder.categories = categories

        presenter.onCategoryClicked(categories[0])

        verify<View>(mockView).showEditCategoryPage(categories[0], 0)
    }

    @Test
    fun onDeleteClickedShouldTriggerViewToLoseCategory() {
        val initialSize = contestBuilder.categories.size
        presenter.onDeleteClicked(contestBuilder.categories[0])
        verify<View>(mockView).showCategories(argThat<List<Category>> { argument -> argument.size == initialSize - 1 })
    }

    @Test
    fun onCategoryMovedShouldUpdateCategoriesListWhenItemMovedUp() {
        presenter.onCategoryMoved(1, 4)

        assertTrue(contestBuilder.categories[4].name == "Category 1")
        assertTrue(contestBuilder.categories[1].name == "Category 2")
    }

    @Test
    fun onCategoryMovedShouldUpdateCategoriesListWhenItemMovedDown() {
        presenter.onCategoryMoved(4, 1)

        assertTrue(contestBuilder.categories[1].name == "Category 4")
        assertTrue(contestBuilder.categories[4].name == "Category 3")
    }

    private fun makeCategories(): List<Category> {
        val categories = ArrayList<Category>()
        for (i in 0..4) {
            categories.add(Category("Category " + i, "Category description " + i))
        }
        return categories
    }
}
