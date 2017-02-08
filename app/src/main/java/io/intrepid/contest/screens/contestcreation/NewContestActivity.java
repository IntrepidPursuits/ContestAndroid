package io.intrepid.contest.screens.contestcreation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.categorieslist.CategoriesListFragment;
import io.intrepid.contest.screens.contestcreation.describecontest.DescribeContestFragment;
import io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity;
import io.intrepid.contest.screens.contestcreation.namecontest.NameContestFragment;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.SlidingTabAdapter;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_DESCRIPTION;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.CATEGORY_NAME;
import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoryActivity.NOTIFY_NEW_CATEGORY;

public class NewContestActivity extends BaseMvpActivity<NewContestPresenter> implements NewContestMvpContract.View, EditContestContract {
    @BindView(R.id.fragment_container)
    ViewPager viewPager;
    private SlidingTabAdapter tabAdapter;

    /*Boolean flag to modify the menuItem's visibility
    without restarting the activity*/
    private boolean nextVisible = true;

    public static Intent createIntent(Context context) {
        return new Intent(context, NewContestActivity.class);
    }

    @NonNull
    @Override
    public NewContestPresenter createPresenter(PresenterConfiguration configuration) {
        return new NewContestPresenter(this, configuration);
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
        viewPager.addOnPageChangeListener(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonClicked();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_viewpager_container;
    }

    public NewContestMvpContract.Presenter getPresenter() {
        return presenter;
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
        viewPager.setAdapter(tabAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_new_contest, menu);
        MenuItem nextItem = menu.findItem(R.id.action_next);
        nextItem.setVisible(nextVisible);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_next:
                presenter.onNextButtonClicked();
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
                    presenter.onNewCategoryAdded(categoryName, categoryDescription);
                }
                break;
        }
    }

    @Override
    public void showContestSubmissionPage(int page) {
        viewPager.setCurrentItem(page, true);
    }

    @Override
    public void cancelEdit() {
        startActivity(SplashActivity.makeIntent(this));
    }

    @Override
    public ContestCreationFragment getChildEditFragment(int pageIndex) {
        return (ContestCreationFragment) tabAdapter.getItem(pageIndex);
    }

    @Override
    public void setNextVisible(boolean visible) {
        nextVisible = visible;
        invalidateOptionsMenu();
    }

    @Override
    public int getCurrentIndex() {
        return viewPager.getCurrentItem();
    }

    @Override
    public void showUpdatedCategories(Contest.Builder contest) {
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
        presenter.showAddCategoryScreen();
    }

    @Override
    public Contest.Builder getContestBuilder() {
        return presenter.getContest();
    }

    public void showNextScreen() {
        presenter.showNextScreen();
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        presenter.onNextStatusChanged(enabled);
    }
}
