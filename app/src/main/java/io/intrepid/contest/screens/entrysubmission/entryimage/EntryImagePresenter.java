package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.EntryRequest;
import io.intrepid.contest.rest.EntryResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EntryImagePresenter extends BasePresenter<EntryImageContract.View> implements EntryImageContract.Presenter {
    /**
     * Quality ranges from 0-100: 0 meaning compress for small size, 100 meaning compress for max quality.
     * Lossless formats like PGN will ignore this setting.
     */
    private static final int QUALITY = 100;
    private static final Bitmap.CompressFormat FORMAT = Bitmap.CompressFormat.PNG;
    private static final String FORMAT_PREFIX = "data:image/png;base64,";

    private Uri imageUri;
    private String entryName;
    private Uri croppedUri;

    EntryImagePresenter(@NonNull EntryImageContract.View view,
                        @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        entryName = getView().getEntryName();
        getView().displayChooseImageLayout(entryName);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();
        if (croppedUri != null) {
            getView().displayPreviewImageLayout(entryName, croppedUri);
        } else if (imageUri != null) {
            getView().checkStoragePermissions();
            getView().startCropImage(entryName, imageUri);
            Timber.d("Starting crop");
        } else {
            getView().displayChooseImageLayout(entryName);
        }
    }

    @Override
    public void onImageReceived(Uri uri) {
        this.imageUri = uri;
    }

    @Override
    public void onEntrySubmitted() {
        Timber.d("Entry creation API call.");
        String contestId = getPersistentSettings().getCurrentContestId().toString();

        Bitmap bitmap = null;
        if (croppedUri != null) {
            bitmap = getView().makeBitmap(croppedUri);
        }

        EntryRequest entryRequest = new EntryRequest(entryName,
                                                     bitmap,
                                                     FORMAT_PREFIX,
                                                     FORMAT,
                                                     QUALITY);

        Disposable disposable = getRestApi().createEntry(contestId, entryRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(this::showResult, throwable -> {
                    Timber.d("API error creating an entry: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(disposable);
    }

    private void showResult(EntryResponse response) {
        if ((response.getEntry() == null) || (response.getEntry().getId() == null)) {
            Timber.d("Entry was not created.");
            getView().showInvalidEntryErrorMessage();
            return;
        }

        getView().showContestStatusScreen();
    }

    @Override
    public void onBitmapRemoved() {
        imageUri = null;
        croppedUri = null;
        getView().displayChooseImageLayout(entryName);
    }

    @Override
    public void onCameraButtonClicked() {
        getView().dispatchTakePictureIntent();
    }

    @Override
    public void onGalleryButtonClicked() {
        getView().dispatchChoosePictureIntent();
    }

    @Override
    public void onStoragePermissionCheck(boolean hasPermissions) {
        if (!hasPermissions) {
            getView().requestStoragePermissions();
        }
    }

    @Override
    public void onImageCropped(Uri resultUri) {
        if (resultUri == null) {
            getView().showMessage(R.string.crop_error_message);
            return;
        }
        Timber.d("image cropped ");
        this.croppedUri = resultUri;
    }
}
