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
import io.intrepid.contest.screens.contestcreation.EditContestContract;

public class CategoriesListFragment extends BaseFragment<CategoriesListPresenter> implements CategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryAdapter = new CategoryAdapter(getContext());
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecyclerView.setAdapter(categoryAdapter);
        presenter.displayCategories(new Contest()); //todo - modify routing so it is the edited contet
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
        EditContestContract activity = (EditContestContract) getContext();
        activity.setCategories(categories);
    }
}
