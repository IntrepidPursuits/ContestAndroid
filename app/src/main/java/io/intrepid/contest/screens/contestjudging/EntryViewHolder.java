package io.intrepid.contest.screens.contestjudging;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Entry;

class EntryViewHolder extends RecyclerView.ViewHolder {
    private final EntryOnClickListener listener;
    @BindView(R.id.entry_submission_title_field)
    TextView submissionTitleField;
    @BindView(R.id.entry_submission_score)
    TextView scoreField;
    @BindView(R.id.entry_submission_image)
    ImageView entryThumbnail;
    private Entry entry;

    EntryViewHolder(ViewGroup parent, EntryOnClickListener listener) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.entry_submission_row, parent, false);
    }

    @OnClick(R.id.entry_submission_card_view)
    void onEntryClick() {
        listener.onEntryClicked(entry);
    }

    void bindEntry(Entry entry) {
        this.entry = entry;
        submissionTitleField.setText(entry.title);
        scoreField.setText(String.valueOf(entry.getRatingAverage()));
        String thumbNailUrl = entry.photoUrl;
        Picasso.with(itemView.getContext()).load(thumbNailUrl).into(entryThumbnail);
    }
}
