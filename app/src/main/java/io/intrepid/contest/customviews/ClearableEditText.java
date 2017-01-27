package io.intrepid.contest.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import io.intrepid.contest.R;

/**
 * Custom View that displays a clear button in EditText
 *
 * Adapted from: https://github.com/depop/ClearableEditText/
 * License: http://www.apache.org/licenses/LICENSE-2.0
 */
public class ClearableEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable clearTextIcon;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;

    public ClearableEditText(final Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.cancel_icon);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop

        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        clearTextIcon = wrappedDrawable;
        clearTextIcon.setBounds(0, 0, clearTextIcon.getIntrinsicHeight(), clearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - clearTextIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void setClearIconVisible(final boolean visible) {
        clearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? clearTextIcon : null,
                compoundDrawables[3]);
    }
}
