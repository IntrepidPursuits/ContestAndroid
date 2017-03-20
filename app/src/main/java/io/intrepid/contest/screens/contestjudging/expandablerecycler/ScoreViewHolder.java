package io.intrepid.contest.screens.contestjudging.expandablerecycler;

import android.animation.ValueAnimator;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.utils.AnimationEndListener;
import io.intrepid.contest.utils.ScoreMapUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

class ScoreViewHolder extends RecyclerView.ViewHolder implements RatingBar.OnRatingBarChangeListener {
    @BindView(R.id.category_item_title)
    TextView categoryTitleField;
    @BindView(R.id.expandable_relative_layout)
    View expandableLinearLayout;
    @BindView(R.id.editable_rating_bar_category)
    RatingBar ratingBar;
    @BindView(R.id.category_score_status_textview)
    TextView scoreStatusTextView;
    @BindView(R.id.category_item_description)
    TextView categoryDescriptionField;
    private Animation animationUp;
    private Animation animationFadeOut;
    private CategoryScoreListener listener;
    private Score score;
    private boolean isViewExpanded = false;
    private ValueAnimator valueAnimator;
    private int originalHeight;

    ScoreViewHolder(ViewGroup parent, CategoryScoreListener listener) {
        super(inflateView(parent));
        this.listener = listener;
        ButterKnife.bind(this, itemView);
        ratingBar.setOnRatingBarChangeListener(this);
        animationUp = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_up);
        animationFadeOut = new AlphaAnimation(1.00f, 0.00f);
        originalHeight = expandableLinearLayout.getHeight();
        valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + originalHeight);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.scorable_category_row_item, parent, false);
    }

    void bindScore(Score score) {
        isViewExpanded = (score.getScoreValue() == 0);
        bindScore(score, isViewExpanded);
    }

    private void bindScore(Score score, boolean reveal) {
        this.score = score;
        this.isViewExpanded = reveal;
        categoryTitleField.setText(score.getCategoryName());
        categoryDescriptionField.setText(score.getCategoryDescription());
        setRatingPaneVisibility(reveal);
    }

    private void setRatingPaneVisibility(boolean reveal) {
        this.isViewExpanded = reveal;
        expandableLinearLayout.setVisibility(reveal ? VISIBLE : GONE);
    }


    private void showScoreMessage(@StringRes int message) {
        scoreStatusTextView.setVisibility(VISIBLE);
        scoreStatusTextView.setText(message);
    }

    private void doAnimatedToggle() {
        if (!isViewExpanded) {
            expand();
        } else {
            doAnimatedCollapse();
        }
    }

    private void expand() {
        expandableLinearLayout.setVisibility(View.VISIBLE);
        expandableLinearLayout.setEnabled(true);
        isViewExpanded = true;
    }


    private void doAnimatedCollapse() {
        valueAnimator = ValueAnimator.ofInt(originalHeight + originalHeight, originalHeight);
        animationFadeOut.setDuration(200);
        animationUp.setAnimationListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                expandableLinearLayout.startAnimation(animationFadeOut);
                expandableLinearLayout.setVisibility(View.GONE);
            }
        });
        expandableLinearLayout.startAnimation(animationUp);
        doCollapse();
    }

    private void quickCollapse() {
        animationFadeOut.setAnimationListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                expandableLinearLayout.setVisibility(GONE);
            }
        });
        expandableLinearLayout.startAnimation(animationFadeOut);
        doCollapse();
    }

    private void doCollapse() {
        isViewExpanded = false;
        valueAnimator = ValueAnimator.ofInt(originalHeight + originalHeight, originalHeight);
        valueAnimator.setDuration(0);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }

    @OnClick(R.id.always_visible_linear_layout)
    void onClick() {
        if (isViewExpanded) {
            quickCollapse();
        } else {
            expand();
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        int newRating = (int) ratingBar.getRating();
        showScoreMessage(ScoreMapUtil.getRatingTextForScore(newRating));
        isViewExpanded = true;
        doAnimatedToggle();
        listener.onScoreChanged(getAdapterPosition(), newRating);
    }
}