package ar.uba.fi.nicodiaz.mascota.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Juan Manuel Romera on 24/9/2015.
 */
public class PhotoUtils {

    public static byte[] compressBitMap(Bitmap bitmap) {
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        return image;
    }
}

