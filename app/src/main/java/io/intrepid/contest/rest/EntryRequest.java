package io.intrepid.contest.rest;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import io.intrepid.contest.utils.ImageUtils;

public class EntryRequest {
    private static final String TITLE_KEY = "title";
    private static final String PICTURE_DATA_KEY = "picture_data";

    @SerializedName("entry")
    private final HashMap<String, Object> entry;

    public EntryRequest(String title, Bitmap photo, String formatPrefix, Bitmap.CompressFormat format, int quality) {
        entry = new HashMap<>();
        entry.put(TITLE_KEY, title);

        if (photo != null) {
            entry.put(PICTURE_DATA_KEY, formatPrefix + ImageUtils.convert(photo, format, quality));
        }
    }
}
