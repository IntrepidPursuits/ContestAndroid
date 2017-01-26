package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import timber.log.Timber;

import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View;

public class EntryImageActivity extends BaseMvpActivity<Presenter> implements View {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final String PICK_IMAGE_TYPE = "image/*";
    private static final String EXTRA_ENTRY_NAME = "_extra_entry_name_";

    @BindView(R.id.entry_choose_image_layout)
    RelativeLayout chooseImageLayout;
    @BindView(R.id.entry_image_camera_button)
    Button chooseCameraButton;
    @BindView(R.id.entry_preview_image_layout)
    RelativeLayout previewImageLayout;
    @BindView(R.id.removable_image_image_view)
    ImageView previewImageView;
    @BindView(R.id.entry_preview_label_text_view)
    TextView previewLabelTextView;

    public static Intent makeIntent(Context context, String entryName) {
        return new Intent(context, EntryImageActivity.class).putExtra(EXTRA_ENTRY_NAME, entryName);
    }

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryImagePresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_entry_image;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            chooseCameraButton.setEnabled(false);
        }
    }

    @Override
    public String getEntryName() {
        return getIntent().getStringExtra(EXTRA_ENTRY_NAME);
    }

    @Override
    public void showEntryName(String entryName) {
        previewLabelTextView.setText(getResources().getString(R.string.entry_image_preview_caption, entryName));
    }

    @Override
    public void displayChooseImageLayout() {
        chooseImageLayout.setVisibility(android.view.View.VISIBLE);
        chooseImageLayout.bringToFront();
        previewImageLayout.setVisibility(android.view.View.INVISIBLE);
    }

    @Override
    public void displayPreviewImageLayout(Bitmap bitmap) {
        chooseImageLayout.setVisibility(android.view.View.INVISIBLE);
        previewImageLayout.setVisibility(android.view.View.VISIBLE);
        previewImageLayout.bringToFront();
        previewImageView.setImageBitmap(bitmap);
    }

    @OnClick(R.id.entry_image_camera_button)
    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @OnClick(R.id.entry_image_gallery_button)
    protected void dispatchChoosePictureIntent() {
        String pickImageMessage = getResources().getString(R.string.pick_image_title);

        Intent androidGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        androidGalleryIntent.setType(PICK_IMAGE_TYPE);

        Intent externalGalleriesIntent = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        externalGalleriesIntent.setType(PICK_IMAGE_TYPE);

        Intent chooserIntent = Intent.createChooser(androidGalleryIntent, pickImageMessage);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { externalGalleriesIntent });

        startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || (requestCode != REQUEST_IMAGE_CAPTURE && requestCode != REQUEST_PICK_IMAGE)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Bitmap bitmap = null;

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
        } else {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Timber.e(e.getMessage());
            }
        }

        presenter.onBitmapReceived(bitmap);
    }

    @OnClick(R.id.removable_image_button)
    protected void onRemoveImageButtonClicked() {
        presenter.onBitmapRemoved();
    }

    @OnClick(R.id.entry_image_submit_button)
    protected void onSubmitButtonClicked() {
        presenter.onEntrySubmitted();
    }
}
