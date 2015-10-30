package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.MyApplication;
import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PushService {

    private static PushService ourInstance = new PushService();
    private final String USER = "user";

    public static PushService getInstance() {
        return ourInstance;
    }

    private PushService() {
    }

    public void registerUser(User user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(USER, user.getParseUser());
        installation.saveInBackground();
    }

    public void subscribeChannels(List<Channel> channels) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        for (Channel channel : channels) {
            installation.put(channel.toString(), Boolean.TRUE);
        }
        installation.saveInBackground();
    }

    public void unsubscribeChannels(List<Channel> channels) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        for (Channel channel : channels) {
            installation.put(channel.toString(), Boolean.FALSE);
        }
        installation.saveInBackground();
    }

    public void sendRequestAdoptionPet(Pet pet) {
        User user = UserService.getInstance().getUser();
        String pushMessage = user.getName() + " quiere adoptar a " + pet.getName();
        JSONObject data = createData(Notification.ADOPTION_REQUEST_RECEIVED, pet.getID(), pushMessage);
        sendRequestPet(pet, data);
    }

    public void sendAcceptedRequestAdoptionPet(Pet pet, User requestingUser) {
        User user = UserService.getInstance().getUser();
        String pushMessage = "Felicitaciones " + user.getName() + " ha aceptado tu solicitud de adopcion por " + pet.getName();
        JSONObject data = createData(Notification.ADOPTION_ACCEPTED_REQUEST, pet.getID(), pushMessage);
        sendRequestPet(requestingUser, data);
    }

    public void sendRejectRequestAdoptionPet(Pet pet, User requestingUser) {
        User user = UserService.getInstance().getUser();
        String pushMessage = "No has podido adoptar a " + pet.getName() + ", pero no te preocupes hay muchas mascotas esperando un hogar!";
        JSONObject data = createData(Notification.ADOPTION_REJECTED_REQUEST, pet.getID(), pushMessage);
        sendRequestPet(requestingUser, data);
    }

    public void sendUnpublishRequestAdoptionPet(AdoptionRequest request) {
        User user = UserService.getInstance().getUser();
        AdoptionPet pet = request.getAdoptionPet();
        User requestingUser = request.getRequestingUser();
        String pushMessage = user.getName() + " ha despublicado a " + pet.getName() + ", pero no te preocupes hay muchas mascotas esperando un hogar!";
        JSONObject data = createData(Notification.ADOPTION_IGNORED_REQUEST, pet.getID(), pushMessage);
        sendRequestPet(requestingUser, data);
    }

    public void sendRepublishRequestAdoptionPet(AdoptionRequest request) {
        User user = UserService.getInstance().getUser();
        AdoptionPet pet = request.getAdoptionPet();
        User requestingUser = request.getRequestingUser();
        String pushMessage = user.getName() + " ha republicado a " + pet.getName();
        JSONObject data = createData(Notification.ADOPTION_IGNORED_REQUEST, pet.getID(), pushMessage);
        sendRequestPet(requestingUser, data);
    }

    public void sendIgnoredRequestAdoptionPet(AdoptionRequest adoptionRequest, List<AdoptionRequest> adoptionRequestIgnored) {

        List<User> users = new ArrayList<>();
        users.add(adoptionRequest.getRequestingUser());
        String pushMessage = "¡Genial! " + adoptionRequest.getAdoptionPet().getName() + " encontró un hogar. Intenta buscar otra, hay muchas mascotas esperando un hogar!";
        JSONObject data = createData(Notification.ADOPTION_IGNORED_REQUEST, adoptionRequest.getAdoptionPet().getID(), pushMessage);
        for (AdoptionRequest adoptionRequest_ : adoptionRequestIgnored) {
            if (adoptionRequest_.isIgnored()) {
                User requestingUser_ = adoptionRequest_.getRequestingUser();
                if (!users.contains(requestingUser_)) {
                    sendRequestPet(requestingUser_, data);
                    users.add(requestingUser_);
                }
            }
        }
    }

    public void sendRequestMissingPet(Pet pet) {
        User user = UserService.getInstance().getUser();
        String pushMessage = "Parece que " + user.getName() + " encontró a " + pet.getName();
        JSONObject data = createData(Notification.MISSING_REQUEST_RECEIVED, pet.getID(), pushMessage);
        sendRequestPet(pet, data);
    }


    public void sendCommentNotification(CommentDB comment, Pet pet, List<CommentDB> commentsOfPet) {

        //Send Notification to Owner
        User user = UserService.getInstance().getUser();
        ParseUser owner = pet.getOwner().getParseUser();

        if (!owner.getObjectId().equals(user.getParseUser().getObjectId())) {
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo(USER, pet.getOwner().getParseUser());
            pushQuery.whereEqualTo(Channel.COMMENTS.toString(), Boolean.TRUE);

            String pushMessageOwner = comment.getAuthor().getName() + " a comentado algo sobre " + pet.getName();
            JSONObject dataOwner = null;
            if (pet instanceof AdoptionPet) {
                dataOwner = createData(Notification.COMMENT_ON_ADOPTION_OWNER, pet.getID(), pushMessageOwner);
            } else if (pet instanceof MissingPet) {
                dataOwner = createData(Notification.COMMENT_ON_MISSING_OWNER, pet.getID(), pushMessageOwner);
            }

            sendPushNotification(pushQuery, dataOwner);
        }


        if (!commentsOfPet.isEmpty()) {
            //Send Notification to Comment's Author
            ParseQuery pushQueryAuthor = ParseInstallation.getQuery();
            List<ParseUser> userFilter = new ArrayList<>();

            for (CommentDB commentDB : commentsOfPet) {
                ParseUser commentUser = commentDB.getAuthor().getParseUser();
                if (commentUser.getObjectId().equals(user.getParseUser().getObjectId())
                        || commentUser.getObjectId().equals(owner.getObjectId())) {
                    continue;
                }

                if (!userFilter.contains(commentUser)) {
                    pushQueryAuthor.whereEqualTo(USER, commentUser);
                    userFilter.add(commentUser);
                }
            }

            if (!userFilter.isEmpty()) {
                pushQueryAuthor.whereEqualTo(Channel.COMMENTS.toString(), Boolean.TRUE);
                String pushMessageAuthor = MyApplication.getContext().getString(R.string.comment_push_notification_author_message);

                JSONObject dataAuthor = null;
                if (pet instanceof AdoptionPet) {
                    dataAuthor = createData(Notification.COMMENT_ON_ADOPTION_AUTHOR, pet.getID(), pushMessageAuthor);
                } else if (pet instanceof MissingPet) {
                    dataAuthor = createData(Notification.COMMENT_ON_MISSING_AUTHOR, pet.getID(), pushMessageAuthor);
                }

                sendPushNotification(pushQueryAuthor, dataAuthor);
            }
        }
    }

    private void sendRequestPet(Pet pet, JSONObject data) {
        User owner = pet.getOwner();
        sendRequestPet(owner, data);
    }

    private void sendRequestPet(User user, JSONObject data) {
        // Find devices associated with these users and the channels
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo(USER, user.getParseUser());
        pushQuery.whereEqualTo(Channel.REQUEST.toString(), Boolean.TRUE);

        // Send push notification to query
        sendPushNotification(pushQuery, data);
    }

    private void sendPushNotification(ParseQuery pushQuery, JSONObject data) {
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setData(data);
        push.sendInBackground();
    }

    private JSONObject createData(Notification notification, String petID, String message) {
        JSONObject data = null;

        try {

            data = new JSONObject();
            data.put(notification.getField(), notification.getDescription());
            data.put("petID", petID);
            data.put("alert", message);
            data.put("action", "com.parse.starter.UPDATE_STATUS");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}