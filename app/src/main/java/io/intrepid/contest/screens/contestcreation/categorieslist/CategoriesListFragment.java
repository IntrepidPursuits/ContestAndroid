package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;
import io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity;
import io.intrepid.contest.utils.dragdrop.SimpleItemTouchHelperCallback;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.NOTIFY_EDIT_EXISTING_CATEGORY;


public class CategoriesListFragment extends BaseFragment<CategoriesListPresenter> implements CategoriesListContract.View, ContestCreationFragment {
    @BindView(R.id.generic_recycler_view)
    RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryAdapter = new CategoryAdapter(R.layout.category_card_row_item, presenter);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecyclerView.setAdapter(categoryAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(categoriesRecyclerView);
        presenter.displayCategories();
    }

    @OnClick(R.id.add_new_category_fab)
    public void onAddCategoryClicked() {
        presenter.onAddCategoryClicked();
    }

    @Override
    public void onNextClicked() {
        presenter.onNextClicked(categoryAdapter.getCategories());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.categories_list_fragment;
    }

    @NonNull
    @Override
    public CategoriesListPresenter createPresenter(PresenterConfiguration configuration) {
        Contest.Builder contestBuilder = ((EditContestContract) getActivity()).getContestBuilder();
        return new CategoriesListPresenter(this, configuration, contestBuilder);
    }

    @Override
    public void showAddCategoryScreen() {
        ((EditContestContract) getActivity()).showAddCategoryScreen();
    }

    @Override
    public void showNextScreen() {
        ((EditContestContract) getActivity()).showNextScreen();
    }

    @Override
    public void showEditCategoryPage(Category category) {
        Intent intent = EditCategoryActivity.makeEditCategoryIntent(getContext(), category);
        getActivity().startActivityForResult(intent, NOTIFY_EDIT_EXISTING_CATEGORY);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
        Timber.d("Categories size " + categoryAdapter.getItemCount());
    }

    @Override
    public Category getDefaultCategory(int categoryNameRes, int categoryDescriptionRes) {
        String categoryName = getString(categoryNameRes);
        String categoryDescription = getString(categoryDescriptionRes);
        return new Category(categoryName, categoryDescription);
    }
}
