package io.intrepid.contest.screens.adminstatus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Entry;

class EntryReviewViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.entry_review_row_imageview)
    ImageView entryThumbnail;
    @BindView(R.id.entry_review_row_title)
    TextView entryTitleTextView;

    EntryReviewViewHolder(ViewGroup parent) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.entry_review_row_item, parent, false);
    }

    public void bind(Entry entry) {
        Picasso.with(itemView.getContext())
                .load(entry.photoUrl).resizeDimen(R.dimen.entry_review_row_size, R.dimen.entry_review_row_size)
                .into(entryThumbnail);
        entryTitleTextView.setText(entry.title);
    }
}
