package io.intrepid.contest.screens.entrysubmission.cropimage;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageActivity;

import static com.yalantis.ucrop.UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR;

public class CropImageActivity extends BaseUCropActivity<CropImageContract.Presenter, CropImageContract.View>
        implements CropImageContract.View {
    private String entryName;

    @NonNull
    @Override
    public CropImageContract.Presenter createPresenter(PresenterConfiguration configuration) {
        entryName = getIntent().getStringExtra(EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        return new CropImagePresenter(this, configuration, entryName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_crop:
                getPresenter().onCropOkButtonClicked();
                break;
            case android.R.id.home:
                getPresenter().onCancelCropClicked();
        }
        return true;
    }

    @Override
    public void dispatchCroppedImage(Uri uri) {
        setResultUri(uri);
        finish();
    }

    @Override
    public void showCropTitle(String entryName) {
        String cropTitle = getString(R.string.crop_message, entryName);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(cropTitle);
        }
    }

    @Override
    public void performCrop() {
        blockingView.setClickable(true);
        showLoader = true;
        supportInvalidateOptionsMenu();
        gestureCropImageView.cropAndSaveImage(compressFormat, compressQuality, getPresenter());
    }

    @Override
    public void cancelCrop() {
        startActivity(EntryImageActivity.makeIntent(this, entryName));
    }
}
