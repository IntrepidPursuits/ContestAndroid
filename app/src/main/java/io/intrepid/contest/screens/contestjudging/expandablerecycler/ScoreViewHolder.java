package io.intrepid.contest.screens.contestjudging.expandablerecycler;

import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.utils.ScoreMapUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

class ScoreViewHolder extends RecyclerView.ViewHolder implements ExpandableViewHolder, RatingBar.OnRatingBarChangeListener {
    private static final long COLLAPSE_DELAY = 5;
    private final CategoryScoreListener listener;
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
    private boolean reveal;
    private Score score;

    ScoreViewHolder(ViewGroup parent, CategoryScoreListener listener) {
        super(inflateView(parent));
        this.listener = listener;
        ButterKnife.bind(this, itemView);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.scorable_category_row_item, parent, false);
    }

    void bindScore(Score score) {
        reveal = (score.getScoreValue() == 0);
        bindScore(score, reveal);
    }

    private void bindScore(Score score, boolean reveal) {
        this.score = score;
        this.reveal = reveal;
        categoryTitleField.setText(score.getCategoryName());
        categoryDescriptionField.setText(score.getCategoryDescription());
        setRatingPaneVisibility(reveal);
    }

    private void setRatingPaneVisibility(boolean reveal) {
        this.reveal = reveal;
        expandableLinearLayout.setVisibility(reveal ? VISIBLE : GONE);
    }

    private void expand() {
        bindScore(score, true);
    }

    private void collapse() {
        bindScore(score, false);
    }

    private void collapseWithDelay() {
        //TODO implement animation
        new Handler().postDelayed(this::collapse, COLLAPSE_DELAY);
    }

    @Override
    public boolean isExpanded() {
        return reveal;
    }

    @Override
    public void showTemporaryScoreMessage(@StringRes int message) {
        scoreStatusTextView.setVisibility(VISIBLE);
        scoreStatusTextView.setText(message);
        new Handler().postDelayed(() -> scoreStatusTextView.setVisibility(GONE), COLLAPSE_DELAY);
    }

    @Override
    public void toggle() {
        if (isExpanded()) {
            collapseWithDelay();
        } else {
            expand();
        }
    }

    @OnClick(R.id.always_visible_linear_layout)
    void onClick() {
        toggle();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        int newRating = (int) ratingBar.getRating();
        showTemporaryScoreMessage(ScoreMapUtil.getRatingTextForScore(newRating));
        toggle();

        int position = getAdapterPosition();
        listener.onScoreChanged(position, newRating);
    }
}
