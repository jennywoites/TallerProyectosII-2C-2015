package ar.uba.fi.nicodiaz.mascota.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Juan Manuel Romera on 30/10/2015.
 */
public class DateUtils {

    public static String formatDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

}
