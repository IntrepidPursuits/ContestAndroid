package io.intrepid.contest.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.TestConfig;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;
import io.intrepid.contest.screens.entrysubmission.join.JoinActivity;
import timber.log.Timber;

import static io.intrepid.contest.screens.splash.SplashContract.Presenter;
import static io.intrepid.contest.screens.splash.SplashContract.View;


public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements View {
    @BindView(R.id.contest_app_header)
    TextView titleTextView;

    public static Intent makeIntent(Context context) {
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
        startActivity(JoinActivity.makeIntent(this));
    }

    @Override
    public void intializeViews() {
        Shader textShader = new LinearGradient(0, 0, 0, 20,
                                                new int[]{R.color.gradient_start, R.color.gradient_end},
                                                new float[]{0, 1}, Shader.TileMode.CLAMP);
//        titleTextView.getPaint().setShader(textShader);
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
