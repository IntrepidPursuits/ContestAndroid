package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.ScoreAdapter;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivity;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivityContract;

public class EntryDetailFragment extends BaseFragment<EntryDetailContract.Presenter>
        implements EntryDetailContract.View {
    @BindView(R.id.top_horizontal_entry_image_card)
    ImageView topImageCard;
    @BindView(R.id.generic_recycler_view)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.entry_score_review_button)
    Button reviewButton;

    private ScoreAdapter scoreAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_rate_entry_detail;
    }

    @OnClick(R.id.entry_score_review_button)
    public void onEntryScoreReviewClicked() {
        presenter.onEntryScoreReviewClicked();
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
    public void setReviewRatingsButtonVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        reviewButton.setVisibility(visibility);
    }

    @Override
    public List<Category> getCategories() {
        return ((ScoreEntriesActivityContract) getActivity()).getCategories();
    }

    @Override
    public void setNextEnabled(boolean nextEnabled) {
        ((ScoreEntriesActivityContract) getActivity()).setNextEnabled(nextEnabled);
    }

    @Override
    public Entry getEntryToRate() {
        return ((ScoreEntriesActivityContract) getActivity()).getCurrentEntry();
    }

    @Override
    public EntryBallot getEntryBallot() {
        return ((ScoreEntriesActivityContract) getActivity()).getCurrentEntryBallot();
    }

    @Override
    public List<EntryBallot> getAllBallots() {
        return ((ScoreEntriesActivityContract) getActivity()).getEntryBallotsList();
    }

    @Override
    public void returnToEntriesListPage() {
        ((ScoreEntriesActivity) getActivity()).showEntriesList();
    }
}
