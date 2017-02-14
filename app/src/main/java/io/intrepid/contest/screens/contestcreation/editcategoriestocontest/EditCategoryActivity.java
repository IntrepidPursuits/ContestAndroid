package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.intrepid.contest.base.BaseFragmentActivity;
import io.intrepid.contest.models.Category;

public class EditCategoryActivity extends BaseFragmentActivity implements EditCategoriesFragment.ActivityCallback {
    public static final String CATEGORY_NAME = "NAME";
    public static final String CATEGORY_DESCRIPTION = "DESCRIPTION";
    public static final String CATEGORY_KEY = "CategoryKey";
    public static final int NOTIFY_NEW_CATEGORY = 200;
    public static final int NOTIFY_EDIT_EXISTING_CATEGORY = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected Fragment createFragment(Intent intent) {
        Category category = intent.getParcelableExtra(CATEGORY_KEY);
        return EditCategoriesFragment.newInstance(category);
    }

    public static Intent makeEditCategoryIntent(Context context, @Nullable Category category) {
        return new Intent(context, EditCategoryActivity.class)
                .putExtra(CATEGORY_KEY, category);
    }

    public static Intent makeAddCategoryIntent(Context context) {
        return new Intent(context, EditCategoryActivity.class);
    }

    @Override
    public void addCategory(Category category) {
        Intent intent = new Intent().putExtra(CATEGORY_NAME, category.getName())
                .putExtra(CATEGORY_DESCRIPTION, category.getDescription());
        setResult(RESULT_OK, intent);
        showCategoryList();
    }

    public void editCategory(Category category, String name, String description) {
        Intent intent = new Intent().putExtra(CATEGORY_KEY, category)
                .putExtra(CATEGORY_NAME, name)
                .putExtra(CATEGORY_DESCRIPTION, description);
        setResult(RESULT_OK, intent);
        showCategoryList();
    }

    @Override
    public void showCategoryList() {
        onBackPressed();
    }
}
