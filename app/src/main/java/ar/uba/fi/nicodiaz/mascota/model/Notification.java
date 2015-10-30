package ar.uba.fi.nicodiaz.mascota.model;

import ar.uba.fi.nicodiaz.mascota.HomeActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.adopcion.MascotaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MascotaAdopcionPublicadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MascotaSolicitadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas.MascotaPerdidaPublicadaDetalleActivity;

/**
 * Created by Juan Manuel Romera on 23/10/2015.
 */
public enum Notification {

    ADOPTION_REQUEST_RECEIVED("ADOPTION_REQUEST_RECEIVED", MascotaAdopcionPublicadaDetalleActivity.class, 2), MISSING_REQUEST_RECEIVED("MISSING_REQUEST_RECEIVED", MascotaPerdidaPublicadaDetalleActivity.class, 2),
    COMMENT_ON_ADOPTION_OWNER("COMMENT_ON_ADOPTION_OWNER", MascotaAdopcionPublicadaDetalleActivity.class, 1), COMMENT_ON_MISSING_OWNER("COMMENT_ON_MISSING_OWNER", MascotaPerdidaPublicadaDetalleActivity.class, 1),
    COMMENT_ON_ADOPTION_AUTHOR("COMMENT_ON_ADOPTION_AUTHOR", MascotaDetalleActivity.class, 1), COMMENT_ON_MISSING_AUTHOR("COMMENT_ON_MISSING_AUTHOR", MascotaDetalleActivity.class, 1),
    ADOPTION_ACCEPTED_REQUEST("ADOPTION_ACCEPTED_REQUEST", MascotaSolicitadaDetalleActivity.class, 0), ADOPTION_IGNORED_REQUEST("ADOPTION_IGNORED_REQUEST", MascotaSolicitadaDetalleActivity.class, 0),
    ADOPTION_REJECTED_REQUEST("ADOPTION_REJECTED_REQUEST", HomeActivity.class, 0);

    private String description;
    private int fragmentView;
    private Class launcherActivityClass;

    Notification(String description, Class launcherActivityClass, int fragmentView) {
        this.description = description;
        this.launcherActivityClass = launcherActivityClass;
        this.fragmentView = fragmentView;
    }

    public static String getField() {
        return "notification";
    }

    public static Notification getValue(String description) {
        for (Notification notification : Notification.values()) {
            if (notification.description.equals(description)) {
                return notification;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public int getFragmentView() {
        return fragmentView;
    }

    public Class getLauncherActivityClass() {
        return launcherActivityClass;
    }
}
