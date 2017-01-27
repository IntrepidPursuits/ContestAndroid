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

    public static final String EXTRAS_DATA_KEY = "data";
    private static final String PICK_IMAGE_TYPE = "image/*";
    private static final String EXTRA_ENTRY_NAME = "_extra_entry_name_";

    @BindView(R.id.entry_choose_image_layout)
    RelativeLayout chooseImageLayout;
    @BindView(R.id.entry_image_camera_button)
    Button chooseCameraButton;
    @BindView(R.id.entry_image_question_text_view)
    TextView questionTextView;
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
        questionTextView.setText(getResources().getString(R.string.entry_image_question, entryName));
    }

    @Override
    public void displayChooseImageLayout() {
        chooseImageLayout.setVisibility(android.view.View.VISIBLE);
        previewImageLayout.setVisibility(android.view.View.GONE);
    }

    @Override
    public void displayPreviewImageLayout(Bitmap bitmap) {
        chooseImageLayout.setVisibility(android.view.View.GONE);
        previewImageLayout.setVisibility(android.view.View.VISIBLE);
        previewImageView.setImageBitmap(bitmap);
    }

    @OnClick(R.id.entry_image_camera_button)
    protected void onCameraButtonClicked() {
        presenter.onCameraButtonClicked();
    }

    @Override
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RequestType.REQUEST_IMAGE_CAPTURE.getValue());
        }
    }

    @OnClick(R.id.entry_image_gallery_button)
    protected void onGalleryButtonClicked() {
        presenter.onGalleryButtonClicked();
    }

    @Override
    public void dispatchChoosePictureIntent() {
        String pickImageMessage = getResources().getString(R.string.pick_image_title);

        Intent androidGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        androidGalleryIntent.setType(PICK_IMAGE_TYPE);

        Intent externalGalleriesIntent = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        externalGalleriesIntent.setType(PICK_IMAGE_TYPE);

        Intent chooserIntent = Intent.createChooser(androidGalleryIntent, pickImageMessage);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { externalGalleriesIntent });

        startActivityForResult(chooserIntent, RequestType.REQUEST_PICK_IMAGE.getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || (requestCode != RequestType.REQUEST_IMAGE_CAPTURE.getValue() &&
                requestCode != RequestType.REQUEST_PICK_IMAGE.getValue())) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Bitmap bitmap = null;

        if (requestCode == RequestType.REQUEST_IMAGE_CAPTURE.getValue()) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get(EXTRAS_DATA_KEY);
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

    private enum RequestType {
        REQUEST_IMAGE_CAPTURE(1), REQUEST_PICK_IMAGE(2);

        private final int value;

        private RequestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
