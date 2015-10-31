package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import ar.uba.fi.nicodiaz.mascota.HomeActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MascotaAdopcionPublicadaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.model.Notification;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by Juan Manuel Romera on 23/10/2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {

    private static final String PARSE_DATA_KEY = "com.parse.Data";

    @Override
    public void onPushOpen(Context context, Intent intent) {

        //To track "App Opens"
        ParseAnalytics.trackAppOpenedInBackground(intent);

        JSONObject data = getDataFromIntent(intent);
        String notDescription = null;
        String petId = null;
        try {
            notDescription = data.getString(Notification.getField());
            petId = data.getString("petID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Notification notification = Notification.getValue(notDescription);
        switch (notification) {

            case ADOPTION_REQUEST_RECEIVED:
            case COMMENT_ON_ADOPTION_OWNER:
            case COMMENT_ON_ADOPTION_AUTHOR:
            case ADOPTION_ACCEPTED_REQUEST:
            case ADOPTION_IGNORED_REQUEST: {
                Pet adoptionPet = PetServiceFactory.getInstance().getAdoptionPet(petId);
                PetServiceFactory.getInstance().setSelectedPet(adoptionPet);
                startActivity(context, intent, notification);
                break;
            }
            case COMMENT_ON_MISSING_OWNER:
            case COMMENT_ON_MISSING_AUTHOR: {
                Pet adoptionPet = PetServiceFactory.getInstance().getMissingPet(petId);
                PetServiceFactory.getInstance().setSelectedPet(adoptionPet);
                startActivity(context, intent, notification);
                break;
            }

            case COMMENT_ON_FOUND_AUTHOR: {
                Pet foundPet = PetServiceFactory.getInstance().getFoundPet(petId);
                PetServiceFactory.getInstance().setSelectedPet(foundPet);
                startActivity(context, intent, notification);
                break;
            }

            default:
                Intent i = new Intent(context, HomeActivity.class);
                i.putExtras(intent.getExtras());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }
    }

    private void startActivity(Context context, Intent intent, Notification notification) {
        Intent i = new Intent(context, notification.getLauncherActivityClass());
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("fragementView", notification.getFragmentView());
        context.startActivity(i);
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            // Json was not readable...
        }
        return data;
    }
}
