package io.intrepid.contest.screens.contestcreation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.categorieslist.CategoriesListFragment;
import io.intrepid.contest.screens.contestcreation.describecontest.DescribeContestFragment;
import io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity;
import io.intrepid.contest.screens.contestcreation.namecontest.NameContestFragment;
import io.intrepid.contest.screens.contestcreation.reviewcontest.ReviewContestFragment;
import io.intrepid.contest.screens.sendinvitations.SendInvitationsActivity;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.SlidingTabAdapter;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_DESCRIPTION;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_INDEX;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_NAME;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.NOTIFY_EDIT_EXISTING_CATEGORY;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.NOTIFY_NEW_CATEGORY;

public class NewContestActivity extends BaseMvpActivity<NewContestMvpContract.Presenter, NewContestMvpContract.View>
        implements NewContestMvpContract.View, EditContestContract {
    @BindView(R.id.fragment_container)
    ViewPager viewPager;

    private SlidingTabAdapter tabAdapter;
    private boolean nextPageButtonVisible = false;

    public static Intent createIntent(Context context) {
        return new Intent(context, NewContestActivity.class);
    }

    @NonNull
    @Override
    public NewContestMvpContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new NewContestPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_viewpager_container;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setupViewPager();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setActionBarTitle(R.string.new_contest);
        }
        viewPager.addOnPageChangeListener(getPresenter());
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackButtonClicked();
    }

    private void setupViewPager() {
        if (tabAdapter == null) {
            tabAdapter = new SlidingTabAdapter(this);
        } else {
            tabAdapter.clear();
        }
        tabAdapter.addFragment(new NameContestFragment());
        tabAdapter.addFragment(new DescribeContestFragment());
        tabAdapter.addFragment(new CategoriesListFragment());
        tabAdapter.addFragment(new ReviewContestFragment());
        viewPager.setAdapter(tabAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_new_contest, menu);
        MenuItem nextItem = menu.findItem(R.id.action_next);
        nextItem.setVisible(nextPageButtonVisible);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_next:
                getPresenter().onNextPageButtonClicked();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOTIFY_NEW_CATEGORY:
                if (resultCode == RESULT_OK && data != null) {
                    String categoryName = data.getStringExtra(CATEGORY_NAME);
                    String categoryDescription = data.getStringExtra(CATEGORY_DESCRIPTION);
                    getPresenter().onNewCategoryAdded(categoryName, categoryDescription);
                }
                break;
            case NOTIFY_EDIT_EXISTING_CATEGORY:
                if (resultCode == RESULT_OK && data != null) {
                    int index = data.getIntExtra(CATEGORY_INDEX, -1);
                    String newName = data.getStringExtra(CATEGORY_NAME);
                    String newDescription = data.getStringExtra(CATEGORY_DESCRIPTION);
                    getPresenter().onContestEditEntered(index, newName, newDescription);
                }
        }
    }

    @Override
    public void showContestSubmissionPage(int page) {
        viewPager.setCurrentItem(page, true);
    }

    @Override
    public void cancelEdit() {
        startActivity(SplashActivity.makeIntent(this));
        finish();
    }

    @Override
    public ContestCreationFragment getChildEditFragment(int pageIndex) {
        return (ContestCreationFragment) tabAdapter.getItem(pageIndex);
    }

    @Override
    public void setNextPageButtonVisible(boolean visible) {
        nextPageButtonVisible = visible;
        invalidateOptionsMenu();
    }

    @Override
    public int getCurrentIndex() {
        return viewPager.getCurrentItem();
    }

    @Override
    public void showUpdatedCategories() {
        Timber.d("Got back contest data");
        setupViewPager();
        viewPager.setCurrentItem(2);
    }

    @Override
    public void navigateToAddCategoryPage(Contest.Builder contest) {
        startActivityForResult(EditCategoryActivity.makeAddCategoryIntent(this), NOTIFY_NEW_CATEGORY);
    }

    @Override
    public void showAddCategoryScreen() {
        getPresenter().showAddCategoryScreen();
    }

    @Override
    public void navigateToSendInvitationsScreen() {
        startActivity(SendInvitationsActivity.makeIntent(this));
        finish();
    }

    @Override
    public void setPageTitle(@StringRes int pageTitle) {
        setActionBarTitle(pageTitle);
    }

    @Override
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public Contest.Builder getContestBuilder() {
        return getPresenter().getContest();
    }

    @Override
    public void showNextScreen() {
        getPresenter().showNextScreen();
    }

    @Override
    public void onNextPageEnabledChanged() {
        getPresenter().onNextPageEnabledChanged();
    }
}
