package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import io.intrepid.contest.base.BaseFragmentActivity;
import io.intrepid.contest.models.Category;

public class AddCategoryActivity extends BaseFragmentActivity implements AddCategoriesFragment.ActivityCallback{

    public static final String CATEGORY_NAME = "NAME";
    public static final String CATEGORY_DESCRIPTION = "DESCRIPTION";
    public static final int NOTIFY_NEW_CATEGORY = 200;

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddCategoryActivity.class);
    }

    @Override
    protected Fragment createFragment(Intent intent) {
        return new AddCategoriesFragment();
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
