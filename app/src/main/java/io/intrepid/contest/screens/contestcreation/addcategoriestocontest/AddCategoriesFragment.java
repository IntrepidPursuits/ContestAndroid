package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;

public class AddCategoriesFragment extends BaseFragment<AddCategoriesPresenter> implements AddCategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.category_name_edittext)
    EditText categoryNameField;
    @BindView(R.id.category_description_edittext)
    EditText categoryDescriptionField;
    private ActivityCallback activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = (ActivityCallback) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_new_contest, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                String name = categoryNameField.getText().toString();
                String description = categoryDescriptionField.getText().toString();
                presenter.onNextClicked(name, description);
                break;
        }
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_category;
    }

    @NonNull
    @Override
    public AddCategoriesPresenter createPresenter(PresenterConfiguration configuration) {
        return new AddCategoriesPresenter(this, configuration);
    }

    @Override
    public void onNextClicked() {
        presenter.onNextClicked(categoryNameField.getText().toString(),
                                categoryDescriptionField.getText().toString());
    }

    @Override
    public void addCategory(Category category) {
        activity.addCategory(category);
    }

    @Override
    public void showCategoriesList() {
        activity.showCategoryList();
    }

    public interface ActivityCallback {
        void addCategory(Category category);

        void showCategoryList();
    }
}

