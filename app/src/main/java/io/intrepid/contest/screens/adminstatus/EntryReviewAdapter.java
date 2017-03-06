package io.intrepid.contest.screens.adminstatus;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Entry;

public class EntryReviewAdapter extends RecyclerView.Adapter<EntryReviewViewHolder> {
    private final List<Entry> entries = new ArrayList<>();

    @Override
    public EntryReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryReviewViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(EntryReviewViewHolder holder, int position) {
        holder.bind(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setData(List<Entry> data) {
        entries.addAll(data);
        notifyDataSetChanged();
    }
}
