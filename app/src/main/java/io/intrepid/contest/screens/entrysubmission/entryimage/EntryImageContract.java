package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;
import android.net.Uri;

import io.intrepid.contest.base.BaseContract;

class EntryImageContract {
    interface View extends BaseContract.View {
        String getEntryName();

        void showEntryName(String entryName);

        void displayChooseImageLayout();

        void startCropImage(String entryName, Uri uri);

        void dispatchTakePictureIntent();

        void dispatchChoosePictureIntent();

        void showInvalidEntryErrorMessage();

        void showContestStatusScreen();

        boolean checkStoragePermissions();

        void requestStoragePermissions();

        void displayPreviewImageLayout(Uri croppedUri);

        Bitmap makeBitmap(Uri croppedUri);

        void cancelEntryEdit();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onImageReceived(Uri uri);

        void onEntrySubmitted();

        void onRemoveBitmapClicked();

        void onCameraButtonClicked();

        void onGalleryButtonClicked();

        void onImageCropped(Uri resultUri);
    }
}
