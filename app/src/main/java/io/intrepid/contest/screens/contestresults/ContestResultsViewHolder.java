package io.intrepid.contest.screens.contestresults;

import android.support.v4.content.ContextCompat;
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
import io.intrepid.contest.models.RankedEntryResult;

class ContestResultsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.contest_result_entry_image_view)
    ImageView entryImage;
    @BindView(R.id.contest_result_entry_score_text_view)
    TextView entryScore;
    @BindView(R.id.contest_result_entry_title_text_view)
    TextView entryTitle;
    @BindView(R.id.contest_result_entry_rank_text_view)
    TextView entryRank;

    ContestResultsViewHolder(ViewGroup parent) {
        super(inflateView(parent));
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.contest_result_row_item, parent, false);
    }

    void bindResultData(RankedEntryResult rankedEntryResult) {
        Picasso.with(itemView.getContext()).load(rankedEntryResult.getPhotoUrl()).placeholder(R.drawable.default_entry_icon).into(entryImage);

        entryScore.setText(String.valueOf(rankedEntryResult.getOverallScore()));
        entryTitle.setText(rankedEntryResult.getTitle());

        int backgroundColorResource;
        int rankTextResource;
        switch (rankedEntryResult.getRank()) {
            case 1:
                backgroundColorResource = R.color.colorFirstPlaceResult;
                rankTextResource = R.string.contest_result_first_place;
                break;
            case 2:
                backgroundColorResource = R.color.colorSecondPlaceResult;
                rankTextResource = R.string.contest_result_second_place;
                break;
            case 3:
                backgroundColorResource = R.color.colorThirdPlaceResult;
                rankTextResource = R.string.contest_result_third_place;
                break;
            default:
                backgroundColorResource = R.color.colorOtherPlacesResult;
                rankTextResource = R.string.contest_result_other_places;
        }
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), backgroundColorResource));
        entryRank.setText(rankTextResource);
    }
}
