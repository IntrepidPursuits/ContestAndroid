package io.intrepid.contest.screens.contestcreation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.describecontest.DescribeContestFragment;
import io.intrepid.contest.screens.contestcreation.namecontest.NameContestFragment;
import io.intrepid.contest.screens.contestcreation.reviewcontest.CategoriesListFragment;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.SlidingTabAdapter;
import timber.log.Timber;


public class NewContestActivity extends BaseMvpActivity<NewContestPresenter> implements NewContestContract.View {

    private static final String KEY_CREATOR_ID = "User_Id";
    private static final int NUMBER_OF_SCREENS = 3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    ViewPager viewPager;
    private SlidingTabAdapter tabAdapter;

    public static Intent createIntent(Context context, UUID creatorId) {
        return new Intent(context, NewContestActivity.class).putExtra(KEY_CREATOR_ID, creatorId);
    }

    @NonNull
    @Override
    public NewContestPresenter createPresenter(PresenterConfiguration configuration) {
        return new NewContestPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_viewpager_container;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupToolbar();
        setupViewPager(viewPager);
        presenter.initializeContestBuilder(getIntent());
    }

    public NewContestContract.Presenter getPresenter() {
        return presenter;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.new_contest);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_media_rew);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupStatusBarColor();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void setupViewPager(ViewPager viewPager) {
        tabAdapter = new SlidingTabAdapter(this);
        tabAdapter.addFragment(new NameContestFragment());
        tabAdapter.addFragment(new DescribeContestFragment());
        tabAdapter.addFragment(new CategoriesListFragment());
        viewPager.setAdapter(tabAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_new_contest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onBackButtonClicked();
                break;
            case R.id.action_next:
                presenter.onNextButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getNumberOfScreens() {
        return NUMBER_OF_SCREENS;
    }

    public void acceptContestName(String contestName) {
        presenter.setContestName(contestName);
    }

    @Override
    public void acceptContestDescription(String contestDescription) {
        presenter.setContestDescription(contestDescription);
    }

    @Override
    public void acceptCategory(Category category) {
        presenter.addCategory(category);
    }

    @Override
    public void onCancelClicked() {
        startActivity(SplashActivity.getIntent(this)); //fixme - NavUtil or onBackPressed - no ned for new intent.
    }

    @Override
    public void returnToPreviousScreen() {
        int currentIndex = tabAdapter.revertToLastPage();
        Timber.d("Moving to " + currentIndex);
        viewPager.setCurrentItem(currentIndex);
    }

    @Override
    public void advanceToNextScreen() {
        int currentIndex = tabAdapter.advance();
        Timber.d("Moving to " + currentIndex);
        viewPager.setCurrentItem(currentIndex);
    }

    @Override
    public void showNewlyCreatedContest(Contest contest) {

    }
}
