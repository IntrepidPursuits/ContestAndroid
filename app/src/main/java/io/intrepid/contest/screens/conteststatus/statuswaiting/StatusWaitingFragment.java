package io.intrepid.contest.screens.conteststatus.statuswaiting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;

public class StatusWaitingFragment extends BaseFragment<StatusWaitingContract.Presenter>
        implements StatusWaitingContract.View {
    private static final long ANIM_DURATION = 800;
    @BindView(R.id.ballot_icon_imageview)
    ImageView ballotIcon;
    @BindView(R.id.ballot_box_icon_imageview)
    ImageView ballotBoxImageView;

    @NonNull
    @Override
    public StatusWaitingPresenter createPresenter(PresenterConfiguration configuration) {
        return new StatusWaitingPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_status_waiting;
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.contest_status_bar_title);
        setActionBarDisplayHomeAsUpEnabled(true);
        animateIndefinitely();
    }

    private void animateIndefinitely() {
        ballotIcon.setVisibility(View.VISIBLE);
        ballotIcon.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (ballotIcon != null) {
                            ballotIcon.setVisibility(View.GONE);
                            ballotIcon.setAlpha(1.0f);
                            animateSlideDown();
                        }
                    }
                });
    }

    private void animateSlideDown() {
        ballotIcon.setVisibility(View.VISIBLE);
        ballotIcon.animate()
                .alpha(0.0f)
                .translationY(ballotBoxImageView.getHeight())
                .setDuration(ANIM_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (ballotIcon != null) {
                            ballotIcon.setAlpha(0.0f);
                            animateIndefinitely();
                        }
                    }
                });
    }
}