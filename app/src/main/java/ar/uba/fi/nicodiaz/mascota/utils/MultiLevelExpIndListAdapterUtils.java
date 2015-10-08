package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;

/**
 * Created by nicolas on 07/10/15.
 */
public class MultiLevelExpIndListAdapterUtils {
    /**
     * Converting dp units to pixel units
     * http://developer.android.com/guide/practices/screens_support.html#dips-pels
     */
    public static int getPaddingPixels(Context context, int dpValue) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dpValue * scale + 0.5f);
    }
}