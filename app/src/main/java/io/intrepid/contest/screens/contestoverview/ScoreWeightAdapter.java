package io.intrepid.contest.screens.contestoverview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import io.intrepid.contest.R;
import io.intrepid.contest.models.ScoreWeight;

public class ScoreWeightAdapter extends RecyclerView.Adapter<ScoreWeightViewHolder>{

    private final static ScoreWeight[] weights = new ScoreWeight[] {
            new ScoreWeight(1, R.string.poor),
            new ScoreWeight(2, R.string.average),
            new ScoreWeight(3, R.string.good),
            new ScoreWeight(4, R.string.great),
            new ScoreWeight(5, R.string.excellent)
    };

    @Override
    public ScoreWeightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreWeightViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ScoreWeightViewHolder holder, int position) {
        ScoreWeight scoreWeight = weights[position];
        holder.bindScore(scoreWeight);
    }

    @Override
    public int getItemCount() {
        return weights.length;
    }
}
