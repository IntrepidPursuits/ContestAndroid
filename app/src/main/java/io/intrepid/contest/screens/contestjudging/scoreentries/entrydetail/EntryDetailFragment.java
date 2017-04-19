package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivity;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivityContract;
import io.intrepid.contest.utils.CustomSnapHelper;

public class EntryDetailFragment extends BaseFragment<EntryDetailContract.Presenter, EntryDetailContract.View>
        implements EntryDetailContract.View {
    @BindView(R.id.entry_score_review_button)
    Button reviewButton;
    @BindView(R.id.snappable_entries_recycler_view)
    RecyclerView entriesRecyclerView;
    private EntryPageAdapter allEntriesAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_rate_entry_detail;
    }

    @OnClick(R.id.entry_score_review_button)
    public void onEntryScoreReviewClicked() {
        getPresenter().onEntryScoreReviewClicked();
    }

    @NonNull
    @Override
    public EntryDetailContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryDetailPresenter(this, configuration);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        allEntriesAdapter = new EntryPageAdapter(getPresenter(), getCategories());
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                                     LinearLayoutManager.HORIZONTAL,
                                                                     false));
        entriesRecyclerView.setAdapter(allEntriesAdapter);
        SnapHelper snapHelper = new CustomSnapHelper(getPresenter());
        snapHelper.attachToRecyclerView(entriesRecyclerView);

    }

    @Override
    public void scrollToEntry(int index) {
        entriesRecyclerView.scrollToPosition(index);
        getPresenter().onPageScrolled();
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
    public void returnToEntriesListPage(boolean review) {
        ((ScoreEntriesActivity) getActivity()).showEntriesList();
    }

    @Override
    public List<Entry> getAllEntries() {
        return ((ScoreEntriesActivity) getActivity()).getEntriesList();
    }

    @Override
    public void showEntries(List<Entry> entries) {
        allEntriesAdapter.setData(entries);
    }

    @Override
    public void onPageSwipedTo(int pageIndex) {
        ((ScoreEntriesActivity) getActivity()).onEntryDetailFragmentPageChanged(pageIndex);
    }
}
