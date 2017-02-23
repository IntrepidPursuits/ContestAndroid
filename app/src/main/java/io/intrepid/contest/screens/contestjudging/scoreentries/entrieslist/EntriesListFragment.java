package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.screens.contestjudging.EntryListAdapter;
import io.intrepid.contest.screens.contestjudging.EntryOnClickListener;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivity;

public class EntriesListFragment extends BaseFragment<EntriesListContract.Presenter> implements EntriesListContract.View {
    @BindView(R.id.generic_recycler_view)
    RecyclerView entriesRecyclerView;
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
        return ((ScoreEntriesActivity) getActivity()).getEntriesList();
    }

    @Override
    public void showEntriesList() {
        List<Entry> entries = getEntries();
        entryListAdapter.setEntries(entries);
    }

    @Override
    public void setNextVisible(boolean nextVisible) {
        ((ScoreEntriesActivity) getActivity()).setNextEnabled(nextVisible);
    }
}
