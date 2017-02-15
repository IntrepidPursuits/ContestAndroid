package io.intrepid.contest.screens.contestjudging.scoresubmission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.screens.contestjudging.EntryListAdapter;

public class SubmissionEntriesActivity extends BaseMvpActivity<SubmissionEntriesPresenter>
        implements SubmissionEntriesContract.View {
    @BindView(R.id.generic_recycler_view)
    RecyclerView entriesRecyclerView;
    private EntryListAdapter entryListAdapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, SubmissionEntriesActivity.class);
    }

    @NonNull
    @Override
    public SubmissionEntriesPresenter createPresenter(PresenterConfiguration configuration) {
        return new SubmissionEntriesPresenter(this, configuration);
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
