package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseViewPagerFragment;
import io.intrepid.contest.base.PresenterConfiguration;

public class AddCategoriesFragment extends BaseViewPagerFragment<AddCategoriesPresenter> implements AddCategoriesContract.View {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_category;
    }

    @OnTextChanged(R.id.category_name_edittext)
    public void onCategoryNamed(CharSequence newName, int start, int before, int count) {
        onCategoryNameEntered(String.valueOf(newName));
    }

    @OnTextChanged(R.id.category_description_edittext)
    public void onCategoryDescribed(CharSequence description, int start, int before, int count) {
        onCategoryDescriptonEntered(String.valueOf(description));
    }

    @NonNull
    @Override
    public AddCategoriesPresenter createPresenter(PresenterConfiguration configuration) {
        return new AddCategoriesPresenter(this, configuration);
    }

    @Override
    public void onCategoryNameEntered(String name) {
        presenter.editCategoryName(name);
    }

    @Override
    public void onCategoryDescriptonEntered(String description) {
        presenter.editCategoryDescription(description);
    }

    @Override
    public void onCancelClicked() {
        //todo
    }

    @Override
    public void onEditComplete() {
        submitText();
    }

    @Override
    public boolean areAllFieldValid() {
        return true;
    }

    @Override
    public int errorMessage() {
        return 0;
    }

    @Override
    public void submitText() {
        presenter.submitCategory();
    }
}
