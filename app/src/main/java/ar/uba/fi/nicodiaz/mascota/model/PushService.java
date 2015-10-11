package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PushService {

    private static PushService ourInstance = new PushService();

    public static PushService getInstance() {
        return ourInstance;
    }

    private PushService() {
    }


    public void sendRequestAdoptionPet(Pet pet) {
        User owner = pet.getOwner();

        User user = UserService.getInstance().getUser();

        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("user", owner.getParseUser());

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage(user.getName() + " quiere adoptar a " + pet.getName());
        push.sendInBackground();
    }
}