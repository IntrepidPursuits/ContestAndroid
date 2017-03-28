package io.intrepid.contest.screens.entrysubmission.cropimage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yalantis.ucrop.BuildConfig;
import com.yalantis.ucrop.UCrop;

public class CustomUCrop {
    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;

    private static final String EXTRA_INPUT_URI = EXTRA_PREFIX + ".InputUri";
    private static final String EXTRA_OUTPUT_URI = EXTRA_PREFIX + ".OutputUri";

    private static final String EXTRA_ASPECT_RATIO_X = EXTRA_PREFIX + ".AspectRatioX";
    private static final String EXTRA_ASPECT_RATIO_Y = EXTRA_PREFIX + ".AspectRatioY";

    private Intent cropIntent;
    private Bundle cropOptionsBundle;

    private CustomUCrop(@NonNull Uri source, @NonNull Uri destination) {
        cropIntent = new Intent();
        cropOptionsBundle = new Bundle();
        cropOptionsBundle.putParcelable(EXTRA_INPUT_URI, source);
        cropOptionsBundle.putParcelable(EXTRA_OUTPUT_URI, destination);
    }

    public static CustomUCrop of(String entryName, @NonNull Uri source, @NonNull Uri destination) {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setToolbarTitle(entryName);
        return new CustomUCrop(source, destination)
                .withAspectRatio(1, 1)
                .useSourceImageAspectRatio()
                .withOptions(options);
    }

    private Intent getIntent(@NonNull Context context) {
        cropIntent.setClass(context, CropImageActivity.class);
        cropIntent.putExtras(cropOptionsBundle);
        return cropIntent;
    }

    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    private CustomUCrop withAspectRatio(float x, float y) {
        cropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, x);
        cropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, y);
        return this;
    }

    private CustomUCrop useSourceImageAspectRatio() {
        cropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, 0);
        cropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 0);
        return this;
    }

    private CustomUCrop withOptions(@NonNull com.yalantis.ucrop.UCrop.Options options) {
        cropOptionsBundle.putAll(options.getOptionBundle());
        return this;
    }
}
