package io.intrepid.contest.screens.contestoverview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.ScoreWeight;
import io.intrepid.contest.screens.contestcreation.categorieslist.CategoryViewHolder;

class DualCategoryScoreAdapter extends RecyclerView.Adapter {
    private final int CATEGORY_VIEW_TYPE = 111;
    private final int SCORE_VIEW_TYPE = 222;
    private final List<Category> categories;
    private final List<ScoreWeight> scoreWeights;

    DualCategoryScoreAdapter() {
        this.categories = new ArrayList<>();
        this.scoreWeights = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CATEGORY_VIEW_TYPE:
                return new CategoryViewHolder(parent, null, R.layout.category_overview_row_item);
            case SCORE_VIEW_TYPE:
                return new ScoreWeightViewHolder(parent);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= categories.size()) {
            return SCORE_VIEW_TYPE;
        }
        return CATEGORY_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= categories.size()) {
            int scorePosition = position - categories.size();
            ((ScoreWeightViewHolder) holder).bindScore(scoreWeights.get(scorePosition));
        } else {
            ((CategoryViewHolder) holder).bindCategory(categories.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size() + scoreWeights.size();
    }

    void setData(List<Category> categories, List<ScoreWeight> weights) {
        setCategories(categories);
        setWeights(weights);
    }

    private void setCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    private void setWeights(List<ScoreWeight> weights) {
        scoreWeights.clear();
        scoreWeights.addAll(weights);
        notifyDataSetChanged();
    }
}
