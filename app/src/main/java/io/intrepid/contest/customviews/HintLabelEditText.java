package io.intrepid.contest.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;

public class HintLabelEditText extends LinearLayout {

    @BindView(R.id.hint_label_text_view)
    TextView labelTextView;
    @BindView(R.id.hint_label_edit_text)
    EditText editText;
    private String hint;
    private String label;
    private int inputType;

    public HintLabelEditText(final Context context) {
        super(context);
        init(context, null);
    }

    public HintLabelEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HintLabelEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        inflate(context, R.layout.hint_label_layout, this);

        if (attrs != null) {
            TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.HintLabelEditText,
                    0, 0);

            try {
                hint = styledAttributes.getString(R.styleable.HintLabelEditText_hint);
                label = styledAttributes.getString(R.styleable.HintLabelEditText_label);
                inputType = styledAttributes.getInteger(R.styleable.HintLabelEditText_android_inputType,
                                                        EditorInfo.TYPE_NULL);
            } finally {
                styledAttributes.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        labelTextView.setText(label);
        editText.setHint(hint);
        editText.setInputType(inputType);
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public final void onTextChanged(final CharSequence newText) {
        setLabelVisible(newText.length() > 0);
    }

    private void setLabelVisible(boolean visible) {
        if (visible) {
            labelTextView.setVisibility(VISIBLE);
        } else {
            labelTextView.setVisibility(GONE);
        }
    }

    public void setError(String error) {
        editText.setError(error);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
        editText.setSelection(text.length());
    }
}
