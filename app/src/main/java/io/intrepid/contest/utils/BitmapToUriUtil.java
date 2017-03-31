package io.intrepid.contest.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

public class BitmapToUriUtil {

    public static Uri convertToContentUri(Context context, Bitmap bitmap) {
        String directory  = "Judgy";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + directory;

        File outputFile = new File(path,"result.png");


        outputFile.mkdirs();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new FileProvider().getUriForFile(context, "io.intrepid.contest", outputFile);
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

        } catch (Exception e) {
           e.printStackTrace();
        }
        return uri;
    }
}
