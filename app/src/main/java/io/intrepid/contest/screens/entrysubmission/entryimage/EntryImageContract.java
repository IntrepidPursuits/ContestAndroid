package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;

import io.intrepid.contest.base.BaseContract;

public class EntryImageContract {
    interface View extends BaseContract.View {
        String getEntryName();

        void showEntryName(String entryName);

        void displayChooseImageLayout();

        void displayPreviewImageLayout(Bitmap bitmap);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onBitmapReceived(Bitmap bitmap);

        void onEntrySubmitted();
    }
}
