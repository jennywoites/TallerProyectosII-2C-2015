package ar.uba.fi.nicodiaz.mascota.utils.Email;

import android.app.Activity;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.User;

public class EmailHelper {
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // Envía los mails a los usuarios según el tipo de acción
    //      Mi mascota en adopción fue ADOPTADA --> MascotaAdoptada.
    //      Mi mascota perdida fue ENCONTRADA --> MascotaPerdidaEncontrada.
    //      La mascota que encontré fue solicitada --> MascotaEncontradaSolicitada.
    // Arma el Subject y Body según la acción.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // activity: Activity desde donde se lanza el mail en caso de que se quiere mostrar un msj de progreso (necesario para backgroundmaillibrary).
    // tipo: tipo de acción (MascotaAdoptada, MascotaPerdidaEncontrada, MascotaEncontradaSolicitada)
    // usuarioActual: usuario que ejecuta la acción de ACEPTAR la solicitud desde MisMascotas.
    // usuarioSolicitante: usuario que mandó la solicitud y está a la espera.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static void sendEmail(Activity activity, String tipo, User usuarioActual, User usuarioSolicitante) {
        String usuarioActual_mailTo = usuarioActual.getEmail();
        String usuarioActual_mailSubject = "";
        String usuarioActual_mailBody = "";
        String usuarioSolicitante_mailTo = usuarioSolicitante.getEmail();
        String usuarioSolicitante_mailSubject = "";
        String usuarioSolicitante_mailBody = "";

        if ("MascotaAdoptada".equals(tipo)) {
            usuarioActual_mailSubject = "¡Felicidades! " + usuarioSolicitante.getName() + " adoptó a tu mascota.";
            usuarioActual_mailBody = "Te acercamos los datos de " + usuarioSolicitante.getName() + ":" + "\n\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Nombre: " + usuarioSolicitante.getName() + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Email: " + usuarioSolicitante.getEmail() + "\n";

            if (usuarioSolicitante.getAddress().hasSublocality()) {
                usuarioActual_mailBody = usuarioActual_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getSubLocality() + ", " + usuarioSolicitante.getAddress().getLocality() + "\n";
            } else {
                usuarioActual_mailBody = usuarioActual_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getLocality() + "\n";
            }

            usuarioActual_mailBody = usuarioActual_mailBody + "Teléfono: " + usuarioSolicitante.getTelephone() + "\n";

            usuarioSolicitante_mailSubject = "¡Felicidades! Adoptaste la mascota de " + usuarioActual.getName() + ".";
            usuarioSolicitante_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Email: " + usuarioActual.getEmail() + "\n";

            if (usuarioActual.getAddress().hasSublocality()) {
                usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Ubicación: " + usuarioActual.getAddress().getSubLocality() + ", " + usuarioActual.getAddress().getLocality() + "\n";
            } else {
                usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Ubicación: " + usuarioActual.getAddress().getLocality() + "\n";
            }

            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Teléfono: " + usuarioActual.getTelephone() + "\n";
        } else if ("MascotaPerdidaEncontrada".equals(tipo)) {
            usuarioActual_mailSubject = "¡Buenas noticias! " + usuarioSolicitante.getName() + " encontró a tu mascota.";
            usuarioActual_mailBody = "Te acercamos los datos de " + usuarioSolicitante.getName() + ":" + "\n\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Nombre: " + usuarioSolicitante.getName() + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Email: " + usuarioSolicitante.getEmail() + "\n";

            if (usuarioSolicitante.getAddress().hasSublocality()) {
                usuarioActual_mailBody = usuarioActual_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getSubLocality() + ", " + usuarioSolicitante.getAddress().getLocality() + "\n";
            } else {
                usuarioActual_mailBody = usuarioActual_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getLocality() + "\n";
            }


            usuarioActual_mailBody = usuarioActual_mailBody + "Teléfono: " + usuarioSolicitante.getTelephone() + "\n";

            usuarioSolicitante_mailSubject = "¡Gracias! Ayudaste a " + usuarioActual.getName() + " a reencontrarse con su mascota.";
            usuarioSolicitante_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Email: " + usuarioActual.getEmail() + "\n";

            if (usuarioActual.getAddress().hasSublocality()) {
                usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Ubicación: " + usuarioActual.getAddress().getSubLocality() + ", " + usuarioActual.getAddress().getLocality() + "\n";
            } else {
                usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Ubicación: " + usuarioActual.getAddress().getLocality() + "\n";
            }

            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Teléfono: " + usuarioActual.getTelephone() + "\n";
        } else if ("MascotaEncontradaSolicitada".equals(tipo)) {
            usuarioActual_mailSubject = "¡Gracias! Ayudaste a " + usuarioSolicitante.getName() + " a reencontrarse con su mascota.";
            usuarioActual_mailBody = "Te acercamos los datos de " + usuarioSolicitante.getName() + ":" + "\n\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Nombre: " + usuarioSolicitante.getName() + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Email: " + usuarioSolicitante.getEmail() + "\n";

            if (usuarioSolicitante.getAddress().hasSublocality()) {
                usuarioActual_mailBody = usuarioActual_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getSubLocality() + ", " + usuarioSolicitante.getAddress().getLocality() + "\n";
            } else {
                usuarioActual_mailBody = usuarioActual_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getLocality() + "\n";
            }

            usuarioActual_mailBody = usuarioActual_mailBody + "Teléfono: " + usuarioSolicitante.getTelephone() + "\n";

            usuarioSolicitante_mailSubject = "¡Buenas noticias! " + usuarioActual.getName() + " tiene a tu mascota.";
            usuarioSolicitante_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Email: " + usuarioActual.getEmail() + "\n";

            if (usuarioActual.getAddress().hasSublocality()) {
                usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Ubicación: " + usuarioActual.getAddress().getSubLocality() + ", " + usuarioActual.getAddress().getLocality() + "\n";
            } else {
                usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Ubicación: " + usuarioActual.getAddress().getLocality() + "\n";
            }

            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Teléfono: " + usuarioActual.getTelephone() + "\n";
        }

        ArrayList<String> usuarioActualDatos = new ArrayList<String>();
        usuarioActualDatos.add(usuarioActual_mailTo);
        usuarioActualDatos.add(usuarioActual_mailSubject);
        usuarioActualDatos.add(usuarioActual_mailBody);

        ArrayList<String> usuarioSolicitanteDatos = new ArrayList<String>();
        usuarioSolicitanteDatos.add(usuarioSolicitante_mailTo);
        usuarioSolicitanteDatos.add(usuarioSolicitante_mailSubject);
        usuarioSolicitanteDatos.add(usuarioSolicitante_mailBody);

        sendEmailInBackground(activity, usuarioActualDatos, usuarioSolicitanteDatos);
    }

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // Envía al usuario dueño de la mascota los datos del usuario solicitante para hogar en tránsito.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // activity: Activity desde donde se lanza el mail en caso de que se quiere mostrar un msj de progreso (necesario para backgroundmaillibrary).
    // usuarioDuenio: Usuario dueño de la mascota que solicita tránsito.
    // usuarioSolicitante: Usuario que se postula para ofrecer hogar en tránsito.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    public static void sendEmailEnTransito(Activity activity, User usuarioDuenio, User usuarioSolicitante, String nombreMascota) {
        String usuarioDuenio_mailTo = usuarioDuenio.getEmail();
        String usuarioDuenio_mailSubject = "¡Buenas noticias! " + usuarioSolicitante.getName() + " se ofreció para dar hogar transitorio a " + nombreMascota + ".";
        String usuarioDuenio_mailBody = "Te acercamos los datos de " + usuarioSolicitante.getName() + ":" + "\n\n";
        usuarioDuenio_mailBody = usuarioDuenio_mailBody + "Nombre: " + usuarioSolicitante.getName() + "\n";
        usuarioDuenio_mailBody = usuarioDuenio_mailBody + "Email: " + usuarioSolicitante.getEmail() + "\n";

        if (usuarioSolicitante.getAddress().hasSublocality()) {
            usuarioDuenio_mailBody = usuarioDuenio_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getSubLocality() + ", " + usuarioSolicitante.getAddress().getLocality() + "\n";
        } else {
            usuarioDuenio_mailBody = usuarioDuenio_mailBody + "Ubicación: " + usuarioSolicitante.getAddress().getLocality() + "\n";
        }

        usuarioDuenio_mailBody = usuarioDuenio_mailBody + "Teléfono: " + usuarioSolicitante.getTelephone() + "\n";

        ArrayList<String> usuarioDuenioDatos = new ArrayList<String>();
        usuarioDuenioDatos.add(usuarioDuenio_mailTo);
        usuarioDuenioDatos.add(usuarioDuenio_mailSubject);
        usuarioDuenioDatos.add(usuarioDuenio_mailBody);

        sendMail(activity, usuarioDuenioDatos);
    }
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // Envía el mail utiliza backgroundmaillibrary.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // activity: Activity desde donde se lanza el mail en caso de que se quiere mostrar un msj de progreso (necesario para backgroundmaillibrary).
    // usuarioActualDatos: lista de mailTo, Subject y Body para el usuario que recibe la solicitud.
    // usuarioSolicitanteDatos: lista de mailTo, Subject y Body para el usuario que envía la solicitud.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    private static void sendEmailInBackground(Activity activity, ArrayList<String> usuarioActualDatos, ArrayList<String> usuarioSolicitanteDatos) {
        sendMail(activity, usuarioActualDatos);
        sendMail(activity, usuarioSolicitanteDatos);
    }


    private static void sendMail(Activity context, List<String> datos) {
        BackgroundMail bm = new BackgroundMail(context);
        bm.setProcessVisibility(false);
        // Datos mail que envía.
        bm.setGmailUserName("tallerproy2.2c2015@gmail.com");
        bm.setGmailPassword("mascota2015");

        // Manda Mail
        bm.setMailTo(datos.get(0));
        bm.setFormSubject(datos.get(1));
        bm.setFormBody(datos.get(2));
        bm.send();
    }
}
