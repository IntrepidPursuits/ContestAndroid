package io.intrepid.contest.splash;

import android.support.annotation.NonNull;

import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import timber.log.Timber;

import static io.intrepid.contest.splash.SplashContract.Presenter;
import static io.intrepid.contest.splash.SplashContract.View;


public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements View {

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new SplashPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @OnClick(R.id.create_contest_button)
    public void onCreateButtonClicked() {
        presenter.onCreateContestClicked();
    }

    @OnClick(R.id.join_contest_button)
    public void onJoinContestClicked() {
        presenter.onJoinContestClicked();
    }

    @Override
    public void showCreateContestScreen() {
        Timber.d("Join contest clicked");
    }

    @Override
    public void showJoinContestScreen() {
        Timber.d("Create contest clicked");
    }
}
