package io.intrepid.contest.screens.contestcreation.categorieslist;

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
import io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoryActivity;
import io.intrepid.contest.utils.dragdrop.SimpleItemTouchHelperCallback;

import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoryActivity.CONTEST_KEY;


public class CategoriesListFragment extends BaseFragment<CategoriesListPresenter> implements CategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private EditContestContract activity;

    public static CategoriesListFragment newInstance(Contest.Builder contest) {
        Bundle args = new Bundle();
        args.putParcelable(CONTEST_KEY, contest);
        CategoriesListFragment fragment = new CategoriesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryAdapter = new CategoryAdapter(getContext());
        activity = (EditContestContract) getContext();
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecyclerView.setAdapter(categoryAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(categoriesRecyclerView);
        Contest.Builder contest = getArguments().getParcelable(CONTEST_KEY);
        presenter.displayCategories(contest);
    }

    @OnClick(R.id.add_new_category_fab)
    public void onAddCategoryClicked() {
        presenter.onAddCategoryClicked();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.categories_list_fragment;
    }

    @NonNull
    @Override
    public CategoriesListPresenter createPresenter(PresenterConfiguration configuration) {
        return new CategoriesListPresenter(this, configuration);
    }

    @Override
    public void showAddCategoryScreen() {
        activity.showAddCategoryScreen();
    }

    @Override
    public void showPreviewContestPage() {

    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showDefaultCategory() {
        categoryAdapter.setExampleCategories();
    }

    @Override
    public void onNextClicked() {
        presenter.onNextClicked(categoryAdapter.getCategories());
    }

    @Override
    public void submitCategories(List<Category> categories) {
        activity.setCategories(categories);
    }
}
