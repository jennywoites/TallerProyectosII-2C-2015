package ar.uba.fi.nicodiaz.mascota.model;

import java.util.logging.Logger;

import ar.uba.fi.nicodiaz.mascota.DispatchActivity;
import ar.uba.fi.nicodiaz.mascota.HomeActivity;
import ar.uba.fi.nicodiaz.mascota.LoginActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.adopcion.MascotaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas.MascotaEncontradaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.perdidas.MascotaPerdidaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MascotaAdopcionPublicadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MascotaSolicitadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.encontradas.MascotaEncontradaPublicadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.encontradas.MascotaEncontradaSolicitadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas.MascotaPerdidaPublicadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas.MascotaPerdidaSolicitadaDetalleActivity;

/**
 * Created by Juan Manuel Romera on 23/10/2015.
 */
public enum Notification {

    ADOPTION_REQUEST_RECEIVED("ADOPTION_REQUEST_RECEIVED", MascotaAdopcionPublicadaDetalleActivity.class, 2), MISSING_REQUEST_RECEIVED("MISSING_REQUEST_RECEIVED", MascotaPerdidaPublicadaDetalleActivity.class, 2),
    FOUND_REQUEST_RECEIVED("FOUND_REQUEST_RECEIVED", MascotaEncontradaPublicadaDetalleActivity.class, 2),
    COMMENT_ON_ADOPTION_OWNER("COMMENT_ON_ADOPTION_OWNER", MascotaAdopcionPublicadaDetalleActivity.class, 1), COMMENT_ON_MISSING_OWNER("COMMENT_ON_MISSING_OWNER", MascotaPerdidaPublicadaDetalleActivity.class, 1),
    COMMENT_ON_FOUND_OWNER("COMMENT_ON_FOUND_OWNER", MascotaEncontradaPublicadaDetalleActivity.class, 1),
    COMMENT_ON_ADOPTION_AUTHOR("COMMENT_ON_ADOPTION_AUTHOR", MascotaDetalleActivity.class, 1), COMMENT_ON_MISSING_AUTHOR("COMMENT_ON_MISSING_AUTHOR", MascotaPerdidaDetalleActivity.class, 1),
    COMMENT_ON_FOUND_AUTHOR("COMMENT_ON_FOUND_AUTHOR", MascotaEncontradaDetalleActivity.class, 1),
    ADOPTION_ACCEPTED_REQUEST("ADOPTION_ACCEPTED_REQUEST", MascotaSolicitadaDetalleActivity.class, 0), ADOPTION_IGNORED_REQUEST("ADOPTION_IGNORED_REQUEST", MascotaSolicitadaDetalleActivity.class, 0),
    ADOPTION_REJECTED_REQUEST("ADOPTION_REJECTED_REQUEST", HomeActivity.class, 0), MISSING_ACCEPTED_REQUEST("MISSING_ACCEPTED_REQUEST", MascotaPerdidaSolicitadaDetalleActivity.class, 0),
    MISSING_IGNORED_REQUEST("MISSING_IGNORED_REQUEST", MascotaPerdidaSolicitadaDetalleActivity.class, 0), MISSING_REJECTED_REQUEST("MISSING_REJECTED_REQUEST", HomeActivity.class, 0),
    FOUND_REJECTED_REQUEST("FOUND_REJECTED_REQUEST", HomeActivity.class, 0), FOUND_ACCEPTED_REQUEST("FOUND_ACCEPTED_REQUEST", MascotaEncontradaSolicitadaDetalleActivity.class, 0),
    FOUND_IGNORED_REQUEST("FOUND_IGNORED_REQUEST", MascotaEncontradaSolicitadaDetalleActivity.class, 0),
    PUBLICACION_BLOQUEADA("PUBLICACION_BLOQUEADA", HomeActivity.class, 0), COMENTARIO_BLOQUEADO("COMENTARIO_BLOQUEADO", HomeActivity.class, 0), USUARIO_BLOQUEADO("USUARIO_BLOQUEADO", DispatchActivity.class, 0);

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
