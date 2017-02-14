package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_KEY;

public class EditCategoriesFragment extends BaseFragment<EditCategoriesPresenter> implements EditCategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.category_name_edittext)
    EditText categoryNameField;
    @BindView(R.id.category_description_edittext)
    EditText categoryDescriptionField;
    private ActivityCallback activity;

    public static Fragment newInstance(@Nullable Category category) {
        EditCategoriesFragment fragment = new EditCategoriesFragment();
        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;
    }

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
    public EditCategoriesPresenter createPresenter(PresenterConfiguration configuration) {
        Bundle args = getArguments();
        Category category = (Category) args.get(CATEGORY_KEY);
        return new EditCategoriesPresenter(this, configuration, category);
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

    @Override
    public void showEditableCategory(String previousCategoryName, String previousCategoryDescription) {
        categoryNameField.setText(previousCategoryName);
        categoryDescriptionField.setText(previousCategoryDescription);
    }

    @Override
    public void editCategory(Category category, String name, String description) {
        ((EditCategoryActivity) activity).editCategory(category, name, description);
    }

    public interface ActivityCallback {
        void addCategory(Category category);

        void showCategoryList();
    }
}
