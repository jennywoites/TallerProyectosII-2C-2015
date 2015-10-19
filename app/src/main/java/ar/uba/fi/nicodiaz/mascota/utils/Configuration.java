package ar.uba.fi.nicodiaz.mascota.utils;

/**
 * Created by Juan Manuel Romera on 19/10/2015.
 */
public class Configuration {

    public static Boolean developmentMode = Boolean.FALSE;

    public static void developmentModeEnable() {
        developmentMode = Boolean.TRUE;
    }

    public static void developmentModeDisable() {
        developmentMode = Boolean.FALSE;
    }

    public static boolean isDevelopmentMode() {
        return developmentMode;
    }
}
