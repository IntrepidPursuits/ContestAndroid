package io.intrepid.contest.screens.contestcreation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.base.TextValidatableView;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesFragment;
import io.intrepid.contest.screens.contestcreation.describecontest.DescribeContestFragment;
import io.intrepid.contest.screens.contestcreation.namecontest.NameContestFragment;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageActivity;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.SlidingTabAdapter;


public class NewContestActivity extends BaseMvpActivity<NewContestPresenter> implements NewContestContract.View {

    private static final String KEY_CREATOR_ID = "User_Id";
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
        setToolbarTitle(R.string.new_contest);
        tabAdapter = new SlidingTabAdapter(this);
        tabAdapter.addFragment(new NameContestFragment());
        tabAdapter.addFragment(new DescribeContestFragment());
        tabAdapter.addFragment(new AddCategoriesFragment());
//        tabAdapter.addFragment(new CategoriesListFragment());
//        tabAdapter.addFragment(new InviteContestantsFragment());
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
                onBackNavigationPressed();
                break;
            case R.id.action_next:
                onForwardNavigationPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackNavigationPressed() {
        presenter.onBackButtonClicked();
    }

    @Override
    public void onForwardNavigationPressed() {
        presenter.onNextButtonClicked();
    }

    public void setToolbarTitle(@StringRes int toolbarTitle) {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setIcon(android.R.drawable.ic_media_rew);
        toolbar.setTitle(toolbarTitle);

    }

    @Override
    public void showContestSubmissionPage(int page) {
        if (viewPager.getAdapter() == null) {
            setupViewPager(viewPager);
        } else if (page < 0) {
            cancelEdit();
        }
        viewPager.setCurrentItem(page);
    }

    @Override
    public void cancelEdit() {
        startActivity(SplashActivity.makeIntent(this));
    }

    @Override
    public void onContestEditComplete() {
        presenter.saveContest();
    }

    @Override
    public TextValidatableView getChildEditFragment(int pageIndex) {
        return (TextValidatableView) tabAdapter.getItem(pageIndex);
    }

    @Override
    public void showNewlyAddedConest(Contest contestSubmission) {
        startActivity(EntryImageActivity.makeIntent(this, "Test"));
    }

}
