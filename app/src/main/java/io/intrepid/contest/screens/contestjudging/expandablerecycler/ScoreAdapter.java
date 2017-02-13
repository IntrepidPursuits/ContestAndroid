package io.intrepid.contest.screens.contestjudging.expandablerecycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Score;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreViewHolder> {
    private final List<Score> entryScores = new ArrayList<>();
    private final CategoryScoreListener listener;

    public ScoreAdapter(CategoryScoreListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScoreViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        holder.bindScore(entryScores.get(position));
    }

    @Override
    public int getItemCount() {
        return entryScores.size();
    }

    public void setData(List<Score> data) {
        entryScores.clear();
        entryScores.addAll(data);
        notifyDataSetChanged();
    }
}
