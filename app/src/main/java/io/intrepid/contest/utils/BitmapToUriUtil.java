package io.intrepid.contest.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import timber.log.Timber;

public class BitmapToUriUtil {

    public static Uri convertToContentUri(Context context, Bitmap bitmap) {
        String directory  = "Judgy";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + directory;

        File outputFile = new File(path,"result.png");
        Uri uri = null;

        outputFile.mkdirs();
        try {
            FileOutputStream out = context.openFileOutput(outputFile.getName(),
                                                          Context.MODE_PRIVATE);
            File file = new File(context.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);
            Timber.d("Uri from conversion was " + uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    public static Uri convertToFileUri(Context context, Bitmap bitmap) {
        Uri uri = null;
        try {
            File file = new File(context.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);
            Timber.d("Uri from conversion was " + uri);

        } catch (Exception e) {
           e.printStackTrace();
        }
        return uri;
    }
}
