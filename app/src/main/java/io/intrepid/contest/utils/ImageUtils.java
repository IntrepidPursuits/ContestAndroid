package io.intrepid.contest.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    public static String convert(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, outputStream);

        String imageEncoded = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return imageEncoded.replace(" ", "").replace("\n", "");
    }
}
