package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.ScoreAdapter;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivity;

public class EntryDetailFragment extends BaseFragment<EntryDetailContract.Presenter> implements EntryDetailContract.View {
    @BindView(R.id.top_horizontal_entry_image_card)
    ImageView topImageCard;
    @BindView(R.id.generic_recycler_view)
    RecyclerView categoriesRecyclerView;
    private ScoreAdapter scoreAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_rate_entry_detail;
    }

    @NonNull
    @Override
    public EntryDetailContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryDetailPresenter(this, configuration);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        scoreAdapter = new ScoreAdapter(presenter);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                           DividerItemDecoration.VERTICAL));
        categoriesRecyclerView.setAdapter(scoreAdapter);
    }

    @Override
    public void showEntry(Entry entry) {
        String photoUrl = entry.photoUrl;
        int imageWidth = (int) getResources().getDimension(R.dimen.rating_image_card_width);
        int imageHeight = (int) getResources().getDimension(R.dimen.rating_image_card_height);
        Picasso.with(getContext()).load(photoUrl).resize(imageWidth, imageHeight).into(topImageCard);
    }

    @Override
    public void showListOfEntryScores(List<Score> entryScores) {
        scoreAdapter.setData(entryScores);
    }

    @Override
    public List<Category> getCategories() {
        return ((ScoreEntriesActivity) getActivity()).getCategories();
    }

    @Override
    public void setNextEnabled(boolean nextEnabled) {
        ((ScoreEntriesActivity) getActivity()).setNextEnabled(nextEnabled);
    }

    @Override
    public Entry getEntryToRate() {
        return ((ScoreEntriesActivity) getActivity()).getCurrentEntry();
    }
}
