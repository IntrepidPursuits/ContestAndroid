package io.intrepid.contest.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;

public class RemovableImageFrameLayout extends FrameLayout {

    @BindView(R.id.removable_image_image_view)
    ImageView imageView;

    public RemovableImageFrameLayout(final Context context) {
        super(context);
    }

    public RemovableImageFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public RemovableImageFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
