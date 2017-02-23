package io.intrepid.contest.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class SpannableUtil {
    private static final SpannableUtil INSTANCE = new SpannableUtil();

    public static SpannableUtil getInstance() {
        return INSTANCE;
    }

    public void setColor(@NonNull Context context,
                         @NonNull SpannableString spannableString,
                         @NonNull String targetText,
                         @ColorRes int colorId) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan((ContextCompat.getColor(context, colorId)));
        setObject(spannableString, targetText, colorSpan);
    }

    public void setBold(@NonNull SpannableString spannableString, @NonNull String targetText) {
        setObject(spannableString, targetText, new StyleSpan(Typeface.BOLD));
    }

    private void setObject(@NonNull SpannableString spannableString,
                           @NonNull String targetText,
                           @NonNull Object object) {
        int start = spannableString.toString().indexOf(targetText);
        int end = start + targetText.length();
        if (start != -1) {
            spannableString.setSpan(object, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }
}
