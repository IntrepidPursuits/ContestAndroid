package io.intrepid.contest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;

public class RemovableImageFrameLayout extends FrameLayout {

    @BindView(R.id.removable_image_root_view)
    ViewGroup rootView;
    @BindView(R.id.removable_image_image_view)
    ImageView imageView;

    public RemovableImageFrameLayout(final Context context) {
        super(context);
        init(context);
    }

    public RemovableImageFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RemovableImageFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.removable_image_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
    }

    @OnClick(R.id.removable_image_button)
    public void onButtonClicked() {
        imageView.setImageBitmap(null);
    }
}
