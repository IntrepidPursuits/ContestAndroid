package io.intrepid.contest.screens.entrysubmission.cropimage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.BaseMvpActivity;
import timber.log.Timber;

/* Modified from UCrop Library - UCropActivity */

@SuppressWarnings("ALL")
public abstract class BaseUCropActivity<T extends BaseContract.Presenter<V>, V extends BaseContract.View> extends BaseMvpActivity<T, V> {
    private static final int DEFAULT_COMPRESS_QUALITY = 90;
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int NONE = 0;
    private static final int SCALE = 1;
    private static final int ROTATE = 2;
    private static final int ALL = 3;
    private static final int TABS_COUNT = 3;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    private final List<ViewGroup> cropAspectRatioViews = new ArrayList<>();
    protected boolean showLoader = true;
    protected GestureCropImageView gestureCropImageView;
    protected View blockingView;
    protected Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
    protected int compressQuality = DEFAULT_COMPRESS_QUALITY;
    @BindView(R.id.ucrop)
    UCropView mUCropView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // Enables dynamic coloring
    private int toolbarColor;
    private int statusBarColor;
    private int activeWidgetColor;
    private int toolbarWidgetColor;
    private boolean showBottomControls;
    private OverlayView overlayView;
    private ViewGroup wrapperStateAspectRatio, wrapperStateRotate, wrapperStateScale;
    private ViewGroup layoutAspectRatio, layoutRotate, layoutScale;
    private TextView textViewRotateAngle, textViewScalePercent;
    private final TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {
            setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
            setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            mUCropView.animate()
                    .alpha(1)
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator());
            blockingView.setClickable(false);
            showLoader = false;
            supportInvalidateOptionsMenu();
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            setResultError(e);
            finish();
        }

    };
    private int[] allowedGestures = new int[] { SCALE, ROTATE, ALL };
    private final View.OnClickListener mStateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!v.isSelected()) {
                setWidgetState(v.getId());
            }
        }
    };

    @Override
    protected int getLayoutResourceId() {
        return com.yalantis.ucrop.R.layout.ucrop_activity_photobox;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        final Intent intent = getIntent();
        setupViews(intent);
        setImageData(intent);
        setInitialState();
        addBlockingView();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(com.yalantis.ucrop.R.menu.ucrop_menu_activity, menu);

        // Change crop & loader menu icons color to match the rest of the UI colors

        MenuItem menuItemLoader = menu.findItem(com.yalantis.ucrop.R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(toolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Timber.i(String.format("%s - %s", e.getMessage(),
                                       getString(com.yalantis.ucrop.R.string.ucrop_mutate_exception_hint)));
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }

        MenuItem menuItemCrop = menu.findItem(com.yalantis.ucrop.R.id.menu_crop);
        Drawable menuItemCropIcon = menuItemCrop.getIcon();
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(toolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(com.yalantis.ucrop.R.id.menu_crop).setVisible(!showLoader);
        menu.findItem(com.yalantis.ucrop.R.id.menu_loader).setVisible(showLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (gestureCropImageView != null) {
            gestureCropImageView.cancelAllAnimations();
        }
    }

    /**
     * This method extracts all data from the incoming intent and setups views properly.
     */
    private void setImageData(@NonNull Intent intent) {
        Uri inputUri = intent.getParcelableExtra(UCrop.EXTRA_INPUT_URI);
        Uri outputUri = intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
        processOptions(intent);

        if (inputUri != null && outputUri != null) {
            try {
                gestureCropImageView.setImageUri(inputUri, outputUri);
            } catch (Exception e) {
                setResultError(e);
                finish();
            }
        } else {
            setResultError(new NullPointerException(getString(com.yalantis.ucrop.R.string.ucrop_error_input_data_is_absent)));
            finish();
        }
    }

    /**
     * This method extracts {@link com.yalantis.ucrop.UCrop.Options #optionsBundle} from incoming intent
     * and setups Activity, {@link OverlayView} and {@link CropImageView} properly.
     */
    @SuppressWarnings("deprecation")
    private void processOptions(@NonNull Intent intent) {
        // Bitmap compression options
        String compressionFormatName = intent.getStringExtra(UCrop.Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        this.compressFormat = (compressFormat == null) ? DEFAULT_COMPRESS_FORMAT : compressFormat;

        compressQuality = intent.getIntExtra(UCrop.Options.EXTRA_COMPRESSION_QUALITY,
                                             UCropActivity.DEFAULT_COMPRESS_QUALITY);

        // Gestures options
        int[] allowedGestures = intent.getIntArrayExtra(UCrop.Options.EXTRA_ALLOWED_GESTURES);
        if (allowedGestures != null && allowedGestures.length == TABS_COUNT) {
            this.allowedGestures = allowedGestures;
        }

        // Crop image view options
        gestureCropImageView.setMaxBitmapSize(intent.getIntExtra(UCrop.Options.EXTRA_MAX_BITMAP_SIZE,
                                                                 CropImageView.DEFAULT_MAX_BITMAP_SIZE));
        gestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER,
                                                                        CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER));
        gestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION,
                                                                                     CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));


        // Overlay view options
        overlayView.setFreestyleCropEnabled(intent.getBooleanExtra(UCrop.Options.EXTRA_FREE_STYLE_CROP,
                                                                   OverlayView.DEFAULT_FREESTYLE_CROP_ENABLED));

        overlayView.setDimmedColor(intent.getIntExtra(UCrop.Options.EXTRA_DIMMED_LAYER_COLOR,
                                                      getResources().getColor(
                                                              com.yalantis.ucrop.R.color.ucrop_color_default_dimmed)));
        overlayView.setCircleDimmedLayer(intent.getBooleanExtra(UCrop.Options.EXTRA_CIRCLE_DIMMED_LAYER,
                                                                OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER));

        overlayView.setShowCropFrame(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_FRAME,
                                                            OverlayView.DEFAULT_SHOW_CROP_FRAME));
        overlayView.setCropFrameColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_COLOR,
                                                         getResources().getColor(
                                                                 com.yalantis.ucrop.R.color.ucrop_color_default_crop_frame)));
        overlayView.setCropFrameStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_STROKE_WIDTH,
                                                               getResources().getDimensionPixelSize(
                                                                       com.yalantis.ucrop.R.dimen.ucrop_default_crop_frame_stoke_width)));

        overlayView.setShowCropGrid(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_GRID,
                                                           OverlayView.DEFAULT_SHOW_CROP_GRID));
        overlayView.setCropGridRowCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_ROW_COUNT,
                                                           OverlayView.DEFAULT_CROP_GRID_ROW_COUNT));
        overlayView.setCropGridColumnCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLUMN_COUNT,
                                                              OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT));
        overlayView.setCropGridColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLOR,
                                                        getResources().getColor(
                                                                com.yalantis.ucrop.R.color.ucrop_color_default_crop_grid)));
        overlayView.setCropGridStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_STROKE_WIDTH,
                                                              getResources().getDimensionPixelSize(
                                                                      com.yalantis.ucrop.R.dimen.ucrop_default_crop_grid_stoke_width)));

        // Aspect ratio options
        float aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, 0);
        float aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, 0);

        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT,
                                                               0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioX > 0 && aspectRatioY > 0) {
            if (wrapperStateAspectRatio != null) {
                wrapperStateAspectRatio.setVisibility(View.GONE);
            }
            gestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
        } else if (aspectRatioList != null &&
                aspectRationSelectedByDefault < aspectRatioList.size()) {
            gestureCropImageView.setTargetAspectRatio(
                    aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioX() /
                            aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioY());
        } else {
            gestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
        }

        // Result bitmap max size options
        int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
        int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);

        if (maxSizeX > 0 && maxSizeY > 0) {
            gestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            gestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void setupViews(@NonNull Intent intent) {
        statusBarColor = intent.getIntExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR,
                                            ContextCompat.getColor(this, R.color.colorAccent));
        toolbarColor = intent.getIntExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR,
                                          ContextCompat.getColor(this, R.color.colorPrimary));
        activeWidgetColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE,
                                               ContextCompat.getColor(this,
                                                                      com.yalantis.ucrop.R.color.ucrop_color_widget_active));
        toolbarWidgetColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR,
                                                ContextCompat.getColor(this,
                                                                       com.yalantis.ucrop.R.color.ucrop_color_toolbar_widget));
        showBottomControls = !intent.getBooleanExtra(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS,
                                                     true);

        setupAppBar();
        initiateRootViews();

        if (showBottomControls) {
            ViewGroup photoBox = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.ucrop_photobox);
            View.inflate(this, com.yalantis.ucrop.R.layout.ucrop_controls, photoBox);

            wrapperStateAspectRatio = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.state_aspect_ratio);
            wrapperStateAspectRatio.setOnClickListener(mStateClickListener);
            wrapperStateRotate = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.state_rotate);
            wrapperStateRotate.setOnClickListener(mStateClickListener);
            wrapperStateScale = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.state_scale);
            wrapperStateScale.setOnClickListener(mStateClickListener);

            layoutAspectRatio = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.layout_aspect_ratio);
            layoutRotate = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.layout_rotate_wheel);
            layoutScale = (ViewGroup) findViewById(com.yalantis.ucrop.R.id.layout_scale_wheel);

            setupAspectRatioWidget(intent);
            setupRotateWidget();
            setupScaleWidget();
            setupStatesWrapper();
        }
    }

    /**
     * Configures and styles both status bar and toolbar.
     */
    private void setupAppBar() {
        setStatusBarColor(statusBarColor);
        final TextView toolbarTitle = (TextView) toolbar.findViewById(com.yalantis.ucrop.R.id.toolbar_title);

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(toolbarColor);
        toolbar.setTitleTextColor(toolbarWidgetColor);

        toolbarTitle.setVisibility(View.GONE);
        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_cross)
                .mutate();
        stateButtonDrawable.setColorFilter(toolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(stateButtonDrawable);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void initiateRootViews() {
        mUCropView = (UCropView) findViewById(com.yalantis.ucrop.R.id.ucrop);
        gestureCropImageView = mUCropView.getCropImageView();
        overlayView = mUCropView.getOverlayView();

        gestureCropImageView.setTransformImageListener(mImageListener);
    }

    /**
     * Use {@link #activeWidgetColor} for color filter
     */
    private void setupStatesWrapper() {
        ImageView stateScaleImageView = (ImageView) findViewById(com.yalantis.ucrop.R.id.image_view_state_scale);
        ImageView stateRotateImageView = (ImageView) findViewById(com.yalantis.ucrop.R.id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = (ImageView) findViewById(com.yalantis.ucrop.R.id.image_view_state_aspect_ratio);

        stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView.getDrawable(),
                                                                           activeWidgetColor));
        stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView.getDrawable(),
                                                                            activeWidgetColor));
        stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(
                stateAspectRatioImageView.getDrawable(),
                activeWidgetColor));
    }


    /**
     * Sets status-bar color for L devices.
     *
     * @param color - status-bar color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    private void setupAspectRatioWidget(@NonNull Intent intent) {

        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT,
                                                               0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioList == null || aspectRatioList.isEmpty()) {
            aspectRationSelectedByDefault = 2;

            aspectRatioList = new ArrayList<>();
            aspectRatioList.add(new AspectRatio(null, 1, 1));
            aspectRatioList.add(new AspectRatio(null, 3, 4));
            aspectRatioList.add(new AspectRatio(getString(com.yalantis.ucrop.R.string.ucrop_label_original)
                                                        .toUpperCase(),
                                                CropImageView.SOURCE_IMAGE_ASPECT_RATIO,
                                                CropImageView.SOURCE_IMAGE_ASPECT_RATIO));
            aspectRatioList.add(new AspectRatio(null, 3, 2));
            aspectRatioList.add(new AspectRatio(null, 16, 9));
        }

        LinearLayout wrapperAspectRatioList = (LinearLayout) findViewById(com.yalantis.ucrop.R.id.layout_aspect_ratio);

        FrameLayout wrapperAspectRatio;
        AspectRatioTextView aspectRatioTextView;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                                                                     ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        for (AspectRatio aspectRatio : aspectRatioList) {
            wrapperAspectRatio = (FrameLayout) getLayoutInflater().inflate(com.yalantis.ucrop.R.layout.ucrop_aspect_ratio,
                                                                           null);
            wrapperAspectRatio.setLayoutParams(lp);
            aspectRatioTextView = ((AspectRatioTextView) wrapperAspectRatio.getChildAt(0));
            aspectRatioTextView.setActiveColor(activeWidgetColor);
            aspectRatioTextView.setAspectRatio(aspectRatio);

            wrapperAspectRatioList.addView(wrapperAspectRatio);
            cropAspectRatioViews.add(wrapperAspectRatio);
        }

        cropAspectRatioViews.get(aspectRationSelectedByDefault).setSelected(true);

        for (ViewGroup cropAspectRatioView : cropAspectRatioViews) {
            cropAspectRatioView.setOnClickListener(v -> {
                gestureCropImageView.setTargetAspectRatio(
                        ((AspectRatioTextView) ((ViewGroup) v).getChildAt(0)).getAspectRatio(v.isSelected()));
                gestureCropImageView.setImageToWrapCropBounds();
                if (!v.isSelected()) {
                    for (ViewGroup cropAspectRatioView1 : cropAspectRatioViews) {
                        cropAspectRatioView1.setSelected(cropAspectRatioView1 == v);
                    }
                }
            });
        }
    }

    private void setupRotateWidget() {
        textViewRotateAngle = ((TextView) findViewById(com.yalantis.ucrop.R.id.text_view_rotate));
        ((HorizontalProgressWheelView) findViewById(com.yalantis.ucrop.R.id.rotate_scroll_wheel))
                .setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
                    @Override
                    public void onScroll(float delta, float totalDistance) {
                        gestureCropImageView.postRotate(
                                delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT);
                    }

                    @Override
                    public void onScrollEnd() {
                        gestureCropImageView.setImageToWrapCropBounds();
                    }

                    @Override
                    public void onScrollStart() {
                        gestureCropImageView.cancelAllAnimations();
                    }
                });

        ((HorizontalProgressWheelView) findViewById(com.yalantis.ucrop.R.id.rotate_scroll_wheel)).setMiddleLineColor(
                activeWidgetColor);


        findViewById(com.yalantis.ucrop.R.id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRotation();
            }
        });
        findViewById(com.yalantis.ucrop.R.id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateByAngle(90);
            }
        });
    }

    private void setupScaleWidget() {
        textViewScalePercent = ((TextView) findViewById(com.yalantis.ucrop.R.id.text_view_scale));
        ((HorizontalProgressWheelView) findViewById(com.yalantis.ucrop.R.id.scale_scroll_wheel))
                .setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
                    @Override
                    public void onScroll(float delta, float totalDistance) {
                        if (delta > 0) {
                            gestureCropImageView.zoomInImage(
                                    gestureCropImageView.getCurrentScale()
                                            + delta * ((gestureCropImageView.getMaxScale() -
                                            gestureCropImageView.getMinScale()) /
                                            SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                        } else {
                            gestureCropImageView.zoomOutImage(
                                    gestureCropImageView.getCurrentScale()
                                            + delta * ((gestureCropImageView.getMaxScale() -
                                            gestureCropImageView.getMinScale()) /
                                            SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                        }
                    }

                    @Override
                    public void onScrollEnd() {
                        gestureCropImageView.setImageToWrapCropBounds();
                    }

                    @Override
                    public void onScrollStart() {
                        gestureCropImageView.cancelAllAnimations();
                    }
                });
        ((HorizontalProgressWheelView) findViewById(com.yalantis.ucrop.R.id.scale_scroll_wheel)).setMiddleLineColor(
                activeWidgetColor);
    }

    private void setAngleText(float angle) {
        if (textViewRotateAngle != null) {
            textViewRotateAngle.setText(String.format(Locale.getDefault(), "%.1f°", angle));
        }
    }

    private void setScaleText(float scale) {
        if (textViewScalePercent != null) {
            textViewScalePercent.setText(String.format(Locale.getDefault(),
                                                       "%d%%",
                                                       (int) (scale * 100)));
        }
    }

    private void resetRotation() {
        gestureCropImageView.postRotate(-gestureCropImageView.getCurrentAngle());
        gestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        gestureCropImageView.postRotate(angle);
        gestureCropImageView.setImageToWrapCropBounds();
    }

    private void setInitialState() {
        if (showBottomControls) {
            if (wrapperStateAspectRatio.getVisibility() == View.VISIBLE) {
                setWidgetState(com.yalantis.ucrop.R.id.state_aspect_ratio);
            } else {
                setWidgetState(com.yalantis.ucrop.R.id.state_scale);
            }
        } else {
            setAllowedGestures(0);
        }
    }

    private void setWidgetState(@IdRes int stateViewId) {
        if (!showBottomControls) {
            return;
        }

        wrapperStateAspectRatio.setSelected(
                stateViewId == com.yalantis.ucrop.R.id.state_aspect_ratio);
        wrapperStateRotate.setSelected(stateViewId == com.yalantis.ucrop.R.id.state_rotate);
        wrapperStateScale.setSelected(stateViewId == com.yalantis.ucrop.R.id.state_scale);

        layoutAspectRatio.setVisibility(stateViewId ==
                                                com.yalantis.ucrop.R.id.state_aspect_ratio ? View.VISIBLE : View.GONE);
        layoutRotate.setVisibility(
                stateViewId == com.yalantis.ucrop.R.id.state_rotate ? View.VISIBLE : View.GONE);
        layoutScale.setVisibility(
                stateViewId == com.yalantis.ucrop.R.id.state_scale ? View.VISIBLE : View.GONE);

        if (stateViewId == com.yalantis.ucrop.R.id.state_scale) {
            setAllowedGestures(0);
        } else if (stateViewId == com.yalantis.ucrop.R.id.state_rotate) {
            setAllowedGestures(1);
        } else {
            setAllowedGestures(2);
        }
    }

    private void setAllowedGestures(int tab) {
        gestureCropImageView.setScaleEnabled(
                allowedGestures[tab] == ALL || allowedGestures[tab] == SCALE);
        gestureCropImageView.setRotateEnabled(
                allowedGestures[tab] == ALL || allowedGestures[tab] == ROTATE);
    }

    /**
     * Adds view that covers everything below the Toolbar.
     * When it's clickable - user won't be able to click/touch anything below the Toolbar.
     * Need to block user input while loading and cropping an image.
     */
    private void addBlockingView() {
        if (blockingView == null) {
            blockingView = new View(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                             ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, com.yalantis.ucrop.R.id.toolbar);
            blockingView.setLayoutParams(lp);
            blockingView.setClickable(true);
        }

        ((RelativeLayout) findViewById(com.yalantis.ucrop.R.id.ucrop_photobox)).addView(
                blockingView);
    }

    protected void setResultUri(Uri uri) {
        setResult(RESULT_OK, new Intent()
                .putExtra(UCrop.EXTRA_OUTPUT_URI, uri));
    }

    private void setResultError(Throwable throwable) {
        setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }

    @IntDef({ NONE, SCALE, ROTATE, ALL })
    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {

    }
}
