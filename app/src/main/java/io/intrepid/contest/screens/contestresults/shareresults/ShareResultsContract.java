package io.intrepid.contest.screens.contestresults.shareresults;


import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.RankedEntryResult;

class ShareResultsContract {
    public interface View extends BaseContract.View {

        void showResultsList(List<RankedEntryResult> topResults);

        Uri captureScreenshot();

        Uri saveInGallery(Bitmap bitmap);

        void setShareOptions(Uri uri);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onSaveResultsClicked();

        void onShareResultsClicked();
    }
}
