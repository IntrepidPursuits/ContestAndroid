package io.intrepid.contest.screens.splash;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Contest;

import static io.intrepid.contest.screens.splash.ContestRowViewHolder.*;

class ContestAdapter extends RecyclerView.Adapter<ContestRowViewHolder> {
    private final List<Contest> contests = new ArrayList<>();
    private final OnContestClickedListener listener;

    ContestAdapter(OnContestClickedListener listener){
        this.listener = listener;
    }

    @Override
    public ContestRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContestRowViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(ContestRowViewHolder holder, int position) {
        holder.bind(contests.get(position));
    }

    @Override
    public int getItemCount() {
        return contests.size();
    }

    public void setData(List<Contest> contestDataList) {
        contests.clear();
        contests.addAll(contestDataList);
        notifyDataSetChanged();
    }
}
