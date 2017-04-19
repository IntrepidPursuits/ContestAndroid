package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivity;
import io.intrepid.contest.screens.entrysubmission.cropimage.CustomUCrop;
import io.intrepid.contest.utils.BitmapToUriUtil;
import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.FEATURE_CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class EntryImageActivity extends BaseMvpActivity<EntryImageContract.Presenter, EntryImageContract.View>
        implements EntryImageContract.View {

    private static final String EXTRAS_DATA_KEY = "data";
    private static final String PICK_IMAGE_TYPE = "image/*";
    private static final String EXTRA_ENTRY_NAME = "_extra_entry_name_";
    private static final int PERMISSIONS_REQUEST_READ_EXT_STORAGE = 100;

    @BindView(R.id.entry_image_submit_button)
    Button submitButton;
    @BindView(R.id.entry_image_container)
    ViewGroup sceneContainer;

    private Scene sceneChooseImage;
    private Scene scenePreviewImage;

    public static Intent makeIntent(Context context, String entryName) {
        return new Intent(context, EntryImageActivity.class).putExtra(EXTRA_ENTRY_NAME, entryName);
    }

    @NonNull
    @Override
    public EntryImageContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryImagePresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_entry_image;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(getResources().getString(R.string.entry_image_bar_title));
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    private Scene getSceneChooseImage() {
        if (sceneChooseImage == null) {
            sceneChooseImage = Scene.getSceneForLayout(sceneContainer, R.layout.entry_image_choose_layout, this);
        }
        return sceneChooseImage;
    }

    private Scene getScenePreviewImage() {
        if (scenePreviewImage == null) {
            scenePreviewImage = Scene.getSceneForLayout(sceneContainer, R.layout.entry_image_preview_layout, this);
        }
        return scenePreviewImage;
    }

    @Override
    public String getEntryName() {
        return getIntent().getStringExtra(EXTRA_ENTRY_NAME);
    }

    @Override
    public void displayChooseImageLayout(String entryName) {
        new ChooseImageView(entryName);
    }

    @Override
    public void startCropImage(String entryName, Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "destination"));
        CustomUCrop uCrop = CustomUCrop.of(entryName, uri, destinationUri);
        uCrop.start(EntryImageActivity.this, RequestType.REQUEST_CROP_IMAGE.getValue());
    }

    @Override
    public void displayPreviewImageLayout(String entryName, Uri croppedUri) {
        new PreviewImageView(entryName, croppedUri);
    }

    @Override
    public Bitmap makeBitmap(Uri croppedUri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RequestType.REQUEST_IMAGE_CAPTURE.getValue());
        }
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
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                               new Intent[] { externalGalleriesIntent });

        startActivityForResult(chooserIntent, RequestType.REQUEST_PICK_IMAGE.getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == RequestType.REQUEST_CROP_IMAGE.getValue()) {
            Uri resultUri = UCrop.getOutput(data);
            getPresenter().onImageCropped(resultUri);
            return;
        }

        Uri uri;
        if (requestCode == RequestType.REQUEST_IMAGE_CAPTURE.getValue()) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get(EXTRAS_DATA_KEY);
            uri = BitmapToUriUtil.convert(this, bitmap);
        } else {
            uri = data.getData();
        }
        getPresenter().onImageReceived(uri);
    }

    @OnClick(R.id.entry_image_submit_button)
    protected void onSubmitButtonClicked() {
        getPresenter().onEntrySubmitted();
    }

    @Override
    public void showInvalidEntryErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.error_creating_entry)
                .setTitle(R.string.error_creating_entry_title)
                .setNeutralButton(R.string.common_ok, (dialog, id) -> {
                });
        builder.create().show();
    }

    @Override
    public void showContestStatusScreen() {
        startActivity(ContestStatusActivity.makeIntent(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkStoragePermissions() {
        boolean hasPermissions = (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                || checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        Timber.d("Permissions needed " + hasPermissions);
        getPresenter().onStoragePermissionCheck(hasPermissions);
    }

    @SuppressLint("NewApi")
    @Override
    public void requestStoragePermissions() {
        requestPermissions(new String[] { READ_CONTACTS },
                           PERMISSIONS_REQUEST_READ_EXT_STORAGE);
    }

    private enum RequestType {
        REQUEST_IMAGE_CAPTURE(1), REQUEST_PICK_IMAGE(2), REQUEST_CROP_IMAGE(3);

        private final int value;

        RequestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    class ChooseImageView {
        @BindView(R.id.entry_image_question_text_view)
        TextView questionTextView;
        @BindView(R.id.entry_image_camera_button)
        Button cameraButton;

        ChooseImageView(String entryName) {
            TransitionManager.go(getSceneChooseImage());
            ButterKnife.bind(this, EntryImageActivity.this);

            questionTextView.setText(getResources()
                                             .getString(R.string.entry_image_question, entryName));
            if (!getPackageManager().hasSystemFeature(FEATURE_CAMERA)) {
                cameraButton.setEnabled(false);
            }
            submitButton.setText(R.string.common_no_thanks);
        }

        @OnClick(R.id.entry_image_camera_button)
        void onCameraButtonClicked() {
            getPresenter().onCameraButtonClicked();
        }

        @OnClick(R.id.entry_image_gallery_button)
        void onGalleryButtonClicked() {
            getPresenter().onGalleryButtonClicked();
        }
    }

    class PreviewImageView {
        @BindView(R.id.entry_preview_label_text_view)
        TextView previewLabelTextView;
        @BindView(R.id.removable_image_image_view)
        ImageView previewImageView;

        PreviewImageView(String entryName, Uri croppedUri) {
            TransitionManager.go(getScenePreviewImage());
            ButterKnife.bind(this, EntryImageActivity.this);

            previewLabelTextView.setText(getResources()
                                                 .getString(R.string.entry_image_preview_caption, entryName));
            previewImageView.setImageURI(croppedUri);
            submitButton.setText(R.string.common_submit);
        }

        @OnClick(R.id.removable_image_button)
        void onRemoveImageButtonClicked() {
            getPresenter().onBitmapRemoved();
        }
    }
}
