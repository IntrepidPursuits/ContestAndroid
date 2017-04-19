package io.intrepid.contest.screens.entrysubmission.cropimage;


import android.net.Uri;
import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import timber.log.Timber;

class CropImagePresenter extends BasePresenter<CropImageContract.View> implements CropImageContract.Presenter {

    private final String entryName;

    CropImagePresenter(@NonNull CropImageContract.View view,
                       @NonNull PresenterConfiguration configuration,
                       String entryName) {
        super(view, configuration);
        this.entryName = entryName;
        Timber.d("Entry name was " + entryName);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();
        getView().showCropTitle(entryName);
    }

    @Override
    public void onCancelCropClicked() {
        getView().cancelCrop();
    }

    @Override
    public void onCropOkButtonClicked() {
        Timber.d("Crop ok clicked");
        getView().performCrop();
    }

    @Override
    public void onBitmapCropped(@NonNull Uri resultUri, int imageWidth, int imageHeight) {
        getView().dispatchCroppedImage(resultUri);
    }

    @Override
    public void onCropFailure(@NonNull Throwable t) {
        getView().showMessage(R.string.crop_error_message);
    }
}
