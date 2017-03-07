package io.intrepid.contest.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;

public class ProgressIndicatorTextView extends FrameLayout {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.animatable_progress_text_view)
    TextView textView;
    private String text;

    public ProgressIndicatorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        ButterKnife.bind(this);
        textView.setText(text);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.animatable_progress_text_view, this);
        if (attrs != null) {
            TypedArray styledAttributes = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.ProgressIndicatorTextView, 0, 0);
            try {
                text = styledAttributes.getString(R.styleable.ProgressIndicatorTextView_text);
            } finally {
                styledAttributes.recycle();
            }
        }
    }

    public void enableProgressAnimation() {
        progressBar.setVisibility(VISIBLE);
        textView.setCompoundDrawables(null, null, null, null);
        textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
    }

    public void showCompleteIndicator() {
        progressBar.setVisibility(GONE);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.check_with_circle, 0, 0);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorVeryLightGray));
    }
}
