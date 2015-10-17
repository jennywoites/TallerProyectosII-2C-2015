package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nicolas on 16/10/15.
 */
public class WaitForInternet {
    public static void setCallback(final WaitForInternetCallback callback) {
        if (isConnected(callback)) {
            callback.onConnectionSuccess();
        } else {
            callback.onConnectionFailure();
        }
    }

    public static boolean isConnected(final WaitForInternetCallback callback) {
        ConnectivityManager cm = (ConnectivityManager) callback.mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
