package io.intrepid.contest.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;
import io.intrepid.contest.screens.join.JoinActivity;

import static android.view.View.VISIBLE;

public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements SplashContract.View {
    @BindView(R.id.splash_screen_actions_layout)
    RelativeLayout splashScreenActionsLayout;

    public static Intent makeIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @NonNull
    @Override
    public SplashContract.Presenter createPresenter(PresenterConfiguration configuration) {
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
        Intent intent = NewContestActivity.createIntent(this);
        startActivity(intent);
    }

    @Override
    public void showJoinContestScreen() {
        startActivity(JoinActivity.makeIntent(this));
    }

    @Override
    public void showUserButtons() {
        splashScreenActionsLayout.setVisibility(VISIBLE);
    }
}
