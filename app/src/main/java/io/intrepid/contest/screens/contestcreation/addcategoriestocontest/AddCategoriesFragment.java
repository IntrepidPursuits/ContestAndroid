package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;

public class AddCategoriesFragment extends BaseFragment<AddCategoriesPresenter> implements AddCategoriesContract.View, ContestCreationFragment {
    @BindView(R.id.category_name_edittext)
    EditText categoryNameField;
    @BindView(R.id.category_description_edittext)
    EditText categoryDescriptionField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @OnClick(R.id.add_new_category_fab)
    public void onNewCategoryClicked() {
        categoryNameField.setText("");
        categoryDescriptionField.setText("");
        presenter.onCategoryNameEntered(categoryNameField.getText().toString(),
                                        categoryDescriptionField.getText().toString());
    }

    @Override
    public void onNext() {
        presenter.onNextClicked();
    }

    @Override
    public void saveCategories(List<Category> categories) {
        EditContestContract activity = (EditContestContract) getActivity();
        activity.setCategories(categories);
    }
}

