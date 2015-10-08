package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by Juan Manuel Romera on 7/10/2015.
 */
public class ErrorUtils {

    public static AlertDialog createNoConnectionDialog(final String title, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_no_connection);
        builder.setMessage(context.getResources().getString(R.string.ERROR_NO_CONECTIVIDAD_MENSAJE))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        return alert;
    }
}
