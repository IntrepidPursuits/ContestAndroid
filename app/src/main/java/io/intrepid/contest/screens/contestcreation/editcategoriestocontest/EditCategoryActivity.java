package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import io.intrepid.contest.base.BaseFragmentActivity;
import io.intrepid.contest.models.Category;

public class EditCategoryActivity extends BaseFragmentActivity implements EditCategoriesFragment.ActivityCallback {

    public static final String CATEGORY_NAME = "NAME";
    public static final String CATEGORY_DESCRIPTION = "DESCRIPTION";
    public static final int NOTIFY_NEW_CATEGORY = 200;

    @Override
    protected Fragment createFragment(Intent intent) {
        return new EditCategoriesFragment();
    }

    public static Intent makeEditCategoryIntent(Context context, Category category) {
        return new Intent(context, EditCategoryActivity.class)
                .putExtra(CATEGORY_NAME, category.getName())
                .putExtra(CATEGORY_DESCRIPTION, category.getDescription());
    }

    public static Intent makeAddCategoryIntent(Context context) {
        return new Intent(context, EditCategoryActivity.class);
    }

    @Override
    public void addCategory(Category category) {
        setResult(RESULT_OK, new Intent()
                .putExtra(CATEGORY_NAME, category.getName())
                .putExtra(CATEGORY_DESCRIPTION, category.getDescription()));
        onBackPressed();
    }

    @Override
    public void showCategoryList() {
        onBackPressed();
    }
}
