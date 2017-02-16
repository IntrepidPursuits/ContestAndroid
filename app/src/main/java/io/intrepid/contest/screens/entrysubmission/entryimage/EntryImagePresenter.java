package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.EntryRequest;
import io.intrepid.contest.rest.EntryResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class EntryImagePresenter extends BasePresenter<EntryImageContract.View> implements EntryImageContract.Presenter {
    /**
     * Quality ranges from 0-100: 0 meaning compress for small size, 100 meaning compress for max quality.
     * Lossless formats like PGN will ignore this setting.
     */
    private static final int QUALITY = 100;
    private static final Bitmap.CompressFormat FORMAT = Bitmap.CompressFormat.PNG;
    private static final String FORMAT_PREFIX = "data:image/png;base64,";

    private Bitmap bitmap;
    private String entryName;

    EntryImagePresenter(@NonNull EntryImageContract.View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        entryName = view.getEntryName();
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
        Timber.d("Entry creation API call.");

        String contestId = persistentSettings.getCurrentContestId().toString();
        EntryRequest entryRequest = new EntryRequest(entryName, bitmap, FORMAT_PREFIX, FORMAT, QUALITY);

        Disposable disposable = restApi.createEntry(contestId, entryRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> showResult(response), throwable -> {
                    Timber.d("API error creating an entry: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(disposable);
    }

    private void showResult(EntryResponse response) {
        if ((response.getEntry() == null) || (response.getEntry().id == null)) {
            Timber.d("Entry was not created.");
            view.showInvalidEntryErrorMessage();
            return;
        }

        view.showContestStatusScreen();
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
