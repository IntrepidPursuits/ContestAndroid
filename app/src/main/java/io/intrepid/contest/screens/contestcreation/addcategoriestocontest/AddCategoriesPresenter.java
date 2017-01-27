package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.ContestProvider;

import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesContract.View;


class AddCategoriesPresenter extends BasePresenter<AddCategoriesContract.View> implements AddCategoriesContract.Presenter {
    private List<Category> categories;
    private Category editableCategory;

    AddCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
        categories = ContestProvider.getInstance().getLastEditedContest().categories;
        editableCategory = new Category();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public void submitCategory() {
        Contest.Builder contest = ContestProvider.getInstance().getLastEditedContest();
        categories.add(editableCategory);
        contest.categories = categories;
        ContestProvider.getInstance().updateTemporaryContest(contest);
    }

    @Override
    public void editCategoryName(String newName) {
        editableCategory.setName(newName);
    }

    @Override
    public void editCategoryDescription(String newName) {
        editableCategory.setDescription(newName);
    }
}
