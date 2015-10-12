package ar.uba.fi.nicodiaz.mascota.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Juan Manuel Romera on 24/9/2015.
 */
public class PhotoUtils {

    public static int PHOTO_WIDHT_MAX = 800;
    public static int PHOTO_HEIGHT_MAX = 600;

    public static Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        float factorH = height / (float) bitmap.getHeight();
        float factorW = width / (float) bitmap.getWidth();

        if (factorH > 1 || factorW > 1) {
            return bitmap;
        }

        float factorToUse = (factorH > factorW) ? factorW : factorH;
        Bitmap bm = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * factorToUse),
                (int) (bitmap.getHeight() * factorToUse), false);
        return bm;
    }


    public static Bitmap resizeImage(File file, int width, int height) throws FileNotFoundException {
        int reqHeight = height;
        int reqWidth = width;
        BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(file), null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}

