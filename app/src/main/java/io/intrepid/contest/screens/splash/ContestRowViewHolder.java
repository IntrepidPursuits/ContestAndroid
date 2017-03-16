package io.intrepid.contest.screens.splash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Contest;

class ContestRowViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.contest_row_title_textview)
    TextView titleTextView;
    private Contest contest;
    private OnContestClickedListener listener;

    ContestRowViewHolder(ViewGroup parent, OnContestClickedListener listener) {
        super(inflateView(parent));
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.contest_row_item, parent, false);
    }

    @OnClick({R.id.contest_row_icon_imageview, R.id.contest_row_title_textview})
    void onClick() {
        listener.onContestClicked(contest);
    }

    void bind(Contest contest) {
        this.contest = contest;
        titleTextView.setText(contest.getTitle());
    }

    interface OnContestClickedListener {
        void onContestClicked(Contest contest);
    }
}
