package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;

public class CategoriesListFragment extends BaseFragment<CategoriesListPresenter> implements CategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;

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

    }

    @Override
    public void showCategories(List<Category> categories) {

    }

    @Override
    public void OnCategoryClicked(Category category) {

    }
}
