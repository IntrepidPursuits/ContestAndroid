package io.intrepid.contest.screens.splash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Contest;

class ContestRowViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.contest_row_title_textview)
    TextView titleTextView;

    private Contest contest;

    ContestRowViewHolder(ViewGroup parent) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.contest_row_item, parent, false);
    }

    public void bind(Contest contest) {
        this.contest = contest;
        titleTextView.setText(contest.getTitle());
    }
}
