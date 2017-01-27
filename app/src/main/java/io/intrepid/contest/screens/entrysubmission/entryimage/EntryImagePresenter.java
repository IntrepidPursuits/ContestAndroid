package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import timber.log.Timber;

import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View;

class EntryImagePresenter extends BasePresenter<View> implements Presenter {
    private Bitmap bitmap;

    EntryImagePresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        String entryName = view.getEntryName();
        view.showEntryName(entryName);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (bitmap == null) {
            view.displayChooseImageLayout();
        } else {
            view.displayPreviewImageLayout(bitmap);
        }
    }

    @Override
    public void onBitmapReceived(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onEntrySubmitted() {
        Timber.d("Submit entry");
    }

    @Override
    public void onBitmapRemoved() {
        bitmap = null;
        view.displayChooseImageLayout();
    }

    @Override
    public void onCameraButtonClicked() {
        view.dispatchTakePictureIntent();
    }

    @Override
    public void onGalleryButtonClicked() {
        view.dispatchChoosePictureIntent();
    }
}
