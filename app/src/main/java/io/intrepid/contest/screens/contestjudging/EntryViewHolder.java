package io.intrepid.contest.screens.contestjudging;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Entry;

class EntryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.entry_submission_title_field)
    TextView submissionTitleField;

    EntryViewHolder(ViewGroup parent) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.entry_submission_row, parent, false);
    }

    void bindEntry(Entry entry) {
        submissionTitleField.setText(entry.title);
    }
}
