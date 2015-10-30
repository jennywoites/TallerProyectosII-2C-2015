package ar.uba.fi.nicodiaz.mascota.utils.Email;

import android.app.Activity;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import java.util.ArrayList;

import ar.uba.fi.nicodiaz.mascota.model.User;

public class EmailHelper
{
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
    public static void sendEmail(Activity activity,String tipo, User usuarioActual, User usuarioSolicitante)
    {
        String usuarioActual_mailTo = usuarioActual.getEmail();
        String usuarioActual_mailSubject = "";
        String usuarioActual_mailBody = "";
        String usuarioSolicitante_mailTo = usuarioSolicitante.getEmail();
        String usuarioSolicitante_mailSubject = "";
        String usuarioSolicitante_mailBody = "";

        if ("MascotaAdoptada".equals(tipo))
        {
            usuarioActual_mailSubject = "¡Felicidades! " + usuarioSolicitante.getName() + " adoptó a tu mascota.";
            usuarioActual_mailBody = "Te acercamos los datos de " + usuarioSolicitante.getName() + ":" + "\n\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Nombre: " + usuarioSolicitante.getName() + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Dirección: " + usuarioSolicitante.getAddress().getCalle().split(",")[0] + ", " + usuarioSolicitante.getAddress().getPiso() + usuarioSolicitante.getAddress().getDepartamento() + "(" + usuarioSolicitante.getAddress().getSubLocality() + " - " + usuarioSolicitante.getAddress().getLocality() + ")" + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Teléfono: " + usuarioSolicitante.getTelephone() + "\n";

            usuarioSolicitante_mailSubject = "¡Felicidades! Adoptaste la mascota de " + usuarioActual.getName() + ".";
            usuarioSolicitante_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Dirección: " + usuarioActual.getAddress().getCalle().split(",")[0] + ", " + usuarioActual.getAddress().getPiso() + usuarioActual.getAddress().getDepartamento() + "(" + usuarioActual.getAddress().getSubLocality() + " - " + usuarioActual.getAddress().getLocality() + ")" + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Teléfono: " + usuarioActual.getTelephone() + "\n";
        }
        else if ("MascotaPerdidaEncontrada".equals(tipo))
        {
            usuarioActual_mailSubject = "¡Buenas noticias! " + usuarioSolicitante.getName() + " encontró a tu mascota.";
            usuarioActual_mailBody = "Te acercamos los datos de " + usuarioSolicitante.getName() + ":" + "\n\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Nombre: " + usuarioSolicitante.getName() + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Dirección: " + usuarioSolicitante.getAddress().getCalle().split(",")[0] + ", " + usuarioSolicitante.getAddress().getPiso() + usuarioSolicitante.getAddress().getDepartamento() + "(" + usuarioSolicitante.getAddress().getSubLocality() + " - " + usuarioSolicitante.getAddress().getLocality() + ")" + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Teléfono: " + usuarioSolicitante.getTelephone() + "\n";

            usuarioSolicitante_mailSubject = "¡Gracias! Ayudaste a " + usuarioActual.getName() + " a reencontrarse con su mascota.";
            usuarioSolicitante_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Dirección: " + usuarioActual.getAddress().getCalle().split(",")[0] + ", " + usuarioActual.getAddress().getPiso() + usuarioActual.getAddress().getDepartamento() + "(" + usuarioActual.getAddress().getSubLocality() + " - " + usuarioActual.getAddress().getLocality() + ")" + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Teléfono: " + usuarioActual.getTelephone() + "\n";
        }
        else if ("MascotaEncontradaSolicitada".equals(tipo))
        {
            usuarioActual_mailSubject = "¡Gracias! Ayudaste a " + usuarioActual.getName() + " a reencontrarse con su mascota.";
            usuarioActual_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Dirección: " + usuarioActual.getAddress().getCalle().split(",")[0] + ", " + usuarioActual.getAddress().getPiso() + usuarioActual.getAddress().getDepartamento() + "(" + usuarioActual.getAddress().getSubLocality() + " - " + usuarioActual.getAddress().getLocality() + ")" + "\n";
            usuarioActual_mailBody = usuarioActual_mailBody + "Teléfono: " + usuarioActual.getTelephone() + "\n";

            usuarioSolicitante_mailSubject = "¡Buenas noticias! " + usuarioSolicitante.getName() + " encontró a tu mascota.";
            usuarioSolicitante_mailBody = "Te acercamos los datos de " + usuarioActual.getName() + ":" + "\n\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Nombre: " + usuarioActual.getName() + "\n";
            usuarioSolicitante_mailBody = usuarioSolicitante_mailBody + "Dirección: " + usuarioActual.getAddress().getCalle().split(",")[0] + ", " + usuarioActual.getAddress().getPiso() + usuarioActual.getAddress().getDepartamento() + "(" + usuarioActual.getAddress().getSubLocality() + " - " + usuarioActual.getAddress().getLocality() + ")" + "\n";
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
    // Envía el mail utiliza backgroundmaillibrary.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // activity: Activity desde donde se lanza el mail en caso de que se quiere mostrar un msj de progreso (necesario para backgroundmaillibrary).
    // usuarioActualDatos: lista de mailTo, Subject y Body para el usuario que recibe la solicitud.
    // usuarioSolicitanteDatos: lista de mailTo, Subject y Body para el usuario que envía la solicitud.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    private static void sendEmailInBackground(Activity activity, ArrayList<String> usuarioActualDatos, ArrayList<String> usuarioSolicitanteDatos)
    {
        BackgroundMail bm = new BackgroundMail(activity);

        // Corre en background sin que el usuario vea por pantalla el envío de mail.
        bm.setProcessVisibility(false);

        // Datos mail que envía.
        bm.setGmailUserName("tallerproy2.2c2015@gmail.com");
        bm.setGmailPassword("mascota2015");

        // Manda mail al usuario que recibe la solicitud.
        bm.setMailTo(usuarioActualDatos.get(0));
        bm.setFormSubject(usuarioActualDatos.get(1));
        bm.setFormBody(usuarioActualDatos.get(2));
        bm.send();

        // Manda mail al usuario que envía la solicitud.
        bm.setMailTo(usuarioSolicitanteDatos.get(0));
        bm.setFormSubject(usuarioSolicitanteDatos.get(1));
        bm.setFormBody(usuarioSolicitanteDatos.get(2));
        bm.send();
    }
}
