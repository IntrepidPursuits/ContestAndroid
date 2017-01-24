package io.intrepid.contest.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.UUID;

import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.TestConfig;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;
import timber.log.Timber;

import static io.intrepid.contest.screens.splash.SplashContract.Presenter;
import static io.intrepid.contest.screens.splash.SplashContract.View;


public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements View {

    public static Intent getIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new SplashPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    public void showCreateContestScreen() {
        UUID userId = TestConfig.userid;
        Intent intent = NewContestActivity.createIntent(this, userId);
        startActivity(intent);
    }

    @Override
    public void showJoinContestScreen() {
        Timber.d("Create contest clicked");
    }

    @OnClick(R.id.create_contest_button)
    public void onCreateButtonClicked() {
        presenter.onCreateContestClicked();
    }

    @OnClick(R.id.join_contest_button)
    public void onJoinContestClicked() {
        presenter.onJoinContestClicked();
    }
}
