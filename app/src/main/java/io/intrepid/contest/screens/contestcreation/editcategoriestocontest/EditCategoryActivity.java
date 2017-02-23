package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragmentActivity;
import io.intrepid.contest.models.Category;

public class EditCategoryActivity extends BaseFragmentActivity implements EditCategoriesFragment.ActivityCallback {
    public static final String CATEGORY_NAME = "NAME";
    public static final String CATEGORY_DESCRIPTION = "DESCRIPTION";
    public static final String CATEGORY_INDEX = "CategoryIndex";
    public static final String CATEGORY_KEY = "CategoryKey";
    public static final int NOTIFY_NEW_CATEGORY = 200;
    public static final int NOTIFY_EDIT_EXISTING_CATEGORY = 400;

    private boolean nextVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarDisplayHomeAsUpEnabled(true);
        setActionBarTitle("");
    }

    @Override
    protected Fragment createFragment(Intent intent) {
        //Default Value assumes a -1 index for a non-existing Category - For adding a new Category
        int index = intent.getIntExtra(CATEGORY_INDEX, -1);

        Category category = intent.getParcelableExtra(CATEGORY_KEY);
        return EditCategoriesFragment.newInstance(category, index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_contest, menu);
        MenuItem nextItem = menu.findItem(R.id.action_next);
        nextItem.setVisible(nextVisible);
        return true;
    }

    public static Intent makeEditCategoryIntent(Context context, Category category, int index) {
        return new Intent(context, EditCategoryActivity.class)
                 .putExtra(CATEGORY_KEY, category)
                .putExtra(CATEGORY_INDEX, index)
                .putExtra(CATEGORY_KEY, category);
    }

    public static Intent makeAddCategoryIntent(Context context) {
        return new Intent(context, EditCategoryActivity.class);
    }

    @Override
    public void addCategory(Category category) {
        Intent intent = new Intent()
                .putExtra(CATEGORY_NAME, category.getName())
                .putExtra(CATEGORY_DESCRIPTION, category.getDescription());
        setResult(RESULT_OK, intent);
        showCategoryList();
    }

    public void publishEditResult(int index, String name, String description) {
        Intent intent = new Intent().putExtra(CATEGORY_INDEX, index)
                .putExtra(CATEGORY_NAME, name)
                .putExtra(CATEGORY_DESCRIPTION, description);
        setResult(RESULT_OK, intent);
        showCategoryList();
    }

    @Override
    public void showCategoryList() {
        onBackPressed();
    }

    @Override
    public void setNextVisible(boolean nextVisible) {
        this.nextVisible = nextVisible;
        invalidateOptionsMenu();
    }
}
