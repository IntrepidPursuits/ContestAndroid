package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_INDEX;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_KEY;

public class EditCategoriesFragment extends BaseFragment<EditCategoriesContract.Presenter, EditCategoriesContract.View>
        implements EditCategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.category_name_edittext)
    EditText categoryNameField;
    @BindView(R.id.category_description_edittext)
    EditText categoryDescriptionField;
    private ActivityCallback activity;

    private Menu menu;

    public static Fragment newInstance(@Nullable Category category, int index) {
        EditCategoriesFragment fragment = new EditCategoriesFragment();
        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_KEY, category);
        args.putInt(CATEGORY_INDEX, index);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                String name = categoryNameField.getText().toString();
                String description = categoryDescriptionField.getText().toString();
                getPresenter().onNextPageButtonClicked(name, description);
                break;
        }
        return true;
    }

    @OnTextChanged(R.id.category_name_edittext)
    public void onCategoryNameChanged(CharSequence newName) {
        getPresenter().onCategoryNameChanged(newName);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_category;
    }

    @NonNull
    @Override
    public EditCategoriesContract.Presenter createPresenter(PresenterConfiguration configuration) {
        Bundle args = getArguments();
        Category categoryToEdit = args.getParcelable(CATEGORY_KEY);
        int index = args.getInt(CATEGORY_INDEX);
        return new EditCategoriesPresenter(this, configuration, categoryToEdit, index);
    }

    @Override
    public boolean isNextPageButtonEnabled() {
        return getPresenter().isNextPageButtonEnabled();
    }

    @Override
    public void onNextPageButtonClicked() {
        getPresenter().onNextPageButtonClicked(categoryNameField.getText().toString(),
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
    public void editCategory(int index, String name, String description) {
        ((EditCategoryActivity) activity).publishEditResult(index, name, description);
    }

    @Override
    public void onNextPageEnabledChanged() {
        activity.onNextPageEnabledChanged(presenter.isNextPageButtonEnabled());
    }

    public interface ActivityCallback {
        void addCategory(Category category);

        void showCategoryList();

        void onNextPageEnabledChanged(boolean isEnabled);
    }
}
