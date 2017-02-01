package io.intrepid.contest.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    /**
     * Quality ranges from 0-100: 0 meaning compress for small size, 100 meaning compress for max quality.
     * Lossless formats like PGN will ignore this setting.
     */
    private static final int QUALITY = 100;
    private static final Bitmap.CompressFormat FORMAT = Bitmap.CompressFormat.PNG;

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(FORMAT, QUALITY, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}