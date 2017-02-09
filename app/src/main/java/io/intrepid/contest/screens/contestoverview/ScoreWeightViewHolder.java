package io.intrepid.contest.screens.contestoverview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.models.ScoreWeight;

class ScoreWeightViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.weight_value_textview)
    TextView weightValueTextView;
    @BindView(R.id.weight_name_textview)
    TextView weightNameTextView;

    ScoreWeightViewHolder(ViewGroup parent) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.score_weight_row, parent, false);
    }

    void bindScore(ScoreWeight scoreWeight) {
        weightValueTextView.setText(String.valueOf(scoreWeight.getWeightValue()));
        weightNameTextView.setText(scoreWeight.getWeightName());
    }
}
