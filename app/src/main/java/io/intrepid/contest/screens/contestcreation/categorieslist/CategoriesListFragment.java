package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;

public class CategoriesListFragment extends BaseFragment<CategoriesListPresenter> implements CategoriesContract.View, ContestCreationFragment {
    private static final String CONTEST_KEY = "Contest Key";
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private Contest contest;

    public static CategoriesListFragment newInstance(Contest.Builder contest) {
        Bundle args = new Bundle();
        CategoriesListFragment fragment = new CategoriesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryAdapter = new CategoryAdapter(getActivity());
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecyclerView.setAdapter(categoryAdapter);
        presenter.displayCategories(contest);
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
    public void onNext() {
        presenter.onNext();
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void onCategoryClicked(Category category) {
        presenter.onCategoryClicked(category);
    }

    @Override
    public void showCategoryForReview(Category category) {
        categoryAdapter.onCategoryClicked(category);
    }

    @Override
    public void showDefaultCategory() {
        categoryAdapter.setExampleCategories();
    }
}
