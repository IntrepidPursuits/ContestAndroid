package io.intrepid.contest.screens.contestresults;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.RankedEntryResult;

class ContestResultsAdapter extends RecyclerView.Adapter<ContestResultsViewHolder> {
    private final List<RankedEntryResult> rankedEntryResults = new ArrayList<>();

    @Override
    public ContestResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContestResultsViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ContestResultsViewHolder holder, int position) {
        holder.bindResultData(rankedEntryResults.get(position));
    }

    @Override
    public int getItemCount() {
        return rankedEntryResults.size();
    }

    public void updateResultsList(@NonNull List<RankedEntryResult> rankedEntryResults) {
        this.rankedEntryResults.clear();
        this.rankedEntryResults.addAll(rankedEntryResults);
        notifyDataSetChanged();
    }
}
