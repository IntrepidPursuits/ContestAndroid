package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.screens.contestjudging.EntryListAdapter;
import io.intrepid.contest.screens.contestjudging.EntryOnClickListener;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivityContract;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EntriesListFragment extends BaseFragment<EntriesListContract.Presenter, EntriesListContract.View>
        implements EntriesListContract.View {
    @BindView(R.id.generic_recycler_view)
    RecyclerView entriesRecyclerView;
    @BindView(R.id.contest_judging_submit_button)
    Button submitButton;
    @BindView(R.id.contest_judging_intro_text_view)
    TextView introTextView;

    private EntryListAdapter entryListAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_entries_list;
    }

    @NonNull
    @Override
    public EntriesListContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntriesListPresenter(this, configuration);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        entryListAdapter = new EntryListAdapter((EntryOnClickListener) getActivity());
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entriesRecyclerView.setAdapter(entryListAdapter);
    }

    @Override
    public List<Entry> getEntries() {
        return ((ScoreEntriesActivityContract) getActivity()).getEntriesList();
    }

    @Override
    public List<EntryBallot> getEntryBallots() {
        return ((ScoreEntriesActivityContract) getActivity()).getEntryBallotsList();
    }

    @Override
    public void showEntriesList(boolean reviewMode) {
        setActionBarTitle(reviewMode ? R.string.review_submissions : R.string.submissions);
        introTextView.setText(reviewMode ? R.string.review_your_scores : R.string.select_submission_prompt);
        List<Entry> entries = getEntries();
        entryListAdapter.setEntries(entries);
    }

    @Override
    public void setNextVisible(boolean nextVisible) {
        ((ScoreEntriesActivityContract) getActivity()).setNextEnabled(nextVisible);
    }

    @Override
    public void showSubmitButton(boolean visible) {
        submitButton.setVisibility(visible ? VISIBLE : GONE);
    }

    @OnClick(R.id.contest_judging_submit_button)
    public void onSubmitButtonClicked() {
        getPresenter().onSubmitButtonClicked();
    }

    @Override
    public void showContestStatusScreen() {
        startActivity(ContestStatusActivity.makeIntent(getActivity(), true));
    }
}
