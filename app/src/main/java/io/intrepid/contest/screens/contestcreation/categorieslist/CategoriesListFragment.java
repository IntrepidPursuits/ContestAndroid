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
import io.intrepid.contest.screens.contestcreation.EditContestContract;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;
import io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity;
import io.intrepid.contest.utils.dragdrop.SimpleItemTouchHelperCallback;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.NOTIFY_EDIT_EXISTING_CATEGORY;


public class CategoriesListFragment extends BaseFragment<CategoriesListContract.Presenter, CategoriesListContract.View>
        implements CategoriesListContract.View, io.intrepid.contest.screens.contestcreation.ContestCreationFragment {

    @BindView(R.id.generic_recycler_view)
    RecyclerView categoriesRecyclerView;

    private CategoryAdapter categoryAdapter;

    @NonNull
    @Override
    public CategoriesListContract.Presenter createPresenter(PresenterConfiguration configuration) {
        Contest.Builder contestBuilder = ((EditContestContract) getActivity()).getContestBuilder();
        return new CategoriesListPresenter(this, configuration, contestBuilder);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.categories_list_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryAdapter = new CategoryAdapter(R.layout.category_card_row_item, getPresenter());
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.scoring_categories);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecyclerView.setAdapter(categoryAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(categoriesRecyclerView);
    }

    @OnClick(R.id.add_new_category_fab)
    public void onAddCategoryClicked() {
        getPresenter().onAddCategoryClicked();
    }

    @Override
    public boolean isNextPageButtonEnabled() {
        return getPresenter().isNextPageButtonEnabled();
    }

    @Override
    public void onNextPageButtonClicked() {
        getPresenter().onNextPageButtonClicked();
    }

    @Override
    public void showNextScreen() {
        ((EditContestContract) getActivity()).showNextScreen();
    }

    @Override
    public void showAddCategoryScreen() {
        ((EditContestContract) getActivity()).showAddCategoryScreen();
    }

    @Override
    public void showEditCategoryPage(Category category, int index) {
        Intent intent = EditCategoryActivity.makeEditCategoryIntent(getContext(), category, index);
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

    @Override
    public void onNextPageEnabledChanged(boolean enabled) {
        ((EditContestContract) getActivity()).onNextPageEnabledChanged();
    }

    private void hideKeyboard() {
        if (isAdded()) {
            ((NewContestActivity) getActivity()).hideKeyboard();
        }
    }

    @Override
    public void onFocus() {
        hideKeyboard();
        // TODO fix
        /* The presenter may not have been created yet, though onFocus was forcefully called.
          If presenter is null, the onViewBound method will still be called when
           the fragment is instantiated
         */
    }
}
