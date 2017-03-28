package io.intrepid.contest.screens.entrysubmission.cropimage;


import android.net.Uri;

import com.yalantis.ucrop.callback.BitmapCropCallback;

import io.intrepid.contest.base.BaseContract;

class CropImageContract {
    interface View extends BaseContract.View {
        void dispatchCroppedImage(Uri uri);

        void showCropTitle(String entryName);

        void performCrop();

        void cancelCrop();
    }

    interface Presenter extends BaseContract.Presenter<View>, BitmapCropCallback {
        void onCancelCropClicked();

        void onCropOkButtonClicked();
    }
}
