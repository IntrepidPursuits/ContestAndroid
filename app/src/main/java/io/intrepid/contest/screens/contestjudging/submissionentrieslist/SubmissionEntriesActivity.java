package io.intrepid.contest.screens.contestjudging.submissionentrieslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.screens.contestjudging.EntryListAdapter;

public class SubmissionEntriesActivity extends BaseMvpActivity<SubmissionEntriesPresenter>
        implements SubmissionEntriesContract.View {
    public static final String SUBMISSION_ENTRIES_KEY = "submissionEntries";
    @BindView(R.id.generic_recycler_view)
    RecyclerView entriesRecyclerView;
    private EntryListAdapter entryListAdapter;

    public static Intent makeIntent(Context context, List<Entry> entries) {
        Intent intent = new Intent(context, SubmissionEntriesActivity.class);
        return intent.putParcelableArrayListExtra(SUBMISSION_ENTRIES_KEY, (ArrayList<Entry>) entries);
    }

    @NonNull
    @Override
    public SubmissionEntriesPresenter createPresenter(PresenterConfiguration configuration) {
        List<Entry> entries = getIntent().getParcelableArrayListExtra(SUBMISSION_ENTRIES_KEY);
        return new SubmissionEntriesPresenter(this, configuration, entries);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.submissions_entries_activity;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.submissions);
        setActionBarDisplayHomeAsUpEnabled(true);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        entryListAdapter = new EntryListAdapter();
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entriesRecyclerView.setAdapter(entryListAdapter);
    }

    @Override
    public void showSubmissionList(List<Entry> submissionEntries) {
        entryListAdapter.setEntries(submissionEntries);
    }
}
