package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.CategoryScoreListener;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.ScoreAdapter;

class EntryPageViewHolder extends RecyclerView.ViewHolder {
    private final List<Category> categories;
    private final CategoryScoreListener listener;
    @BindView(R.id.top_horizontal_entry_image_card)
    ImageView topImageCard;
    @BindView(R.id.generic_recycler_view)
    RecyclerView categoriesRecyclerView;
    private ScoreAdapter scoreAdapter;

    EntryPageViewHolder(ViewGroup parent, CategoryScoreListener listener, List<Category> categories) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.categories = categories;
        scoreAdapter = new ScoreAdapter(listener);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        categoriesRecyclerView.setAdapter(scoreAdapter);
    }

    private static View inflateView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_page_view_holder, parent, false);
    }

    void bindEntry(Entry entry) {
        setupCategoriesList(entry);
        String photoUrl = entry.photoUrl;
        int imageWidth = (int) itemView.getResources().getDimension(R.dimen.rating_image_card_width);
        int imageHeight = (int) itemView.getResources().getDimension(R.dimen.rating_image_card_height);
        Picasso.with(itemView.getContext()).load(photoUrl).resize(imageWidth, imageHeight).into(topImageCard);
    }

    private void setupCategoriesList(Entry entry) {
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            scores.add(new Score(category, entry.getScoreAt(i)));
        }
        scoreAdapter.setData(scores);
    }
}
