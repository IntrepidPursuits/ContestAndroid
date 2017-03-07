package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.CategoryScoreListener;

class EntryPageAdapter extends RecyclerView.Adapter<EntryPageViewHolder> {
    private final List<Entry> entries = new ArrayList();
    private final List<Category> categories = new ArrayList<>();
    private final CategoryScoreListener listener;
    private List<Score> scores;

    EntryPageAdapter(CategoryScoreListener listener,
                     List<Category> categories) {
        this.listener = listener;
        setCategories(categories);
    }

    @Override
    public EntryPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryPageViewHolder(parent, listener, categories);
    }

    @Override
    public void onBindViewHolder(EntryPageViewHolder holder, int position) {
        holder.bindEntry(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setData(List<Entry> data) {
        entries.clear();
        entries.addAll(data);
        notifyDataSetChanged();
    }

    public void setCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
    }
}
