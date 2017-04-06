package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;
import android.net.Uri;

import io.intrepid.contest.base.BaseContract;

class EntryImageContract {
    interface View extends BaseContract.View {
        String getEntryName();

        void displayChooseImageLayout(String entryName);

        void startCropImage(String entryName, Uri uri);

        void dispatchTakePictureIntent();

        void dispatchChoosePictureIntent();

        void showInvalidEntryErrorMessage();

        void showContestStatusScreen();

        void checkStoragePermissions();

        void requestStoragePermissions();

        void displayPreviewImageLayout(String entryName, Uri croppedUri);

        Bitmap makeBitmap(Uri croppedUri);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onImageReceived(Uri uri);

        void onEntrySubmitted();

        void onBitmapRemoved();

        void onCameraButtonClicked();

        void onGalleryButtonClicked();

        void onStoragePermissionCheck(boolean hasPermissions);

        void onImageCropped(Uri resultUri);
    }
}
