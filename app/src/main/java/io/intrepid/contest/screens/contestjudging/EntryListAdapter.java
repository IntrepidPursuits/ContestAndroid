package io.intrepid.contest.screens.contestjudging;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Entry;

public class EntryListAdapter extends RecyclerView.Adapter<EntryViewHolder> {
    private final List<Entry> entries = new ArrayList<>();
    private final EntryOnClickListener listener;

    public EntryListAdapter(EntryOnClickListener listener){
        this.listener = listener;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.bindEntry(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setEntries(List<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }
}
