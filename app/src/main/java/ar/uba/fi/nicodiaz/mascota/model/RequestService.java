package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JFERRIO on 13/10/2015.
 */
public class RequestService {

    private static final int LIMIT_PETS = 4;
    private static final int LIMIT_REQUEST = 10;
    private static RequestService ourInstance = new RequestService();
    private static PushService pushService;

    public static RequestService getInstance() {
        return ourInstance;
    }

    private RequestService() {
        pushService = PushService.getInstance();
    }

    private AdoptionRequest adoptionRequest;

/*    public AdoptionRequest getSelectedAdoptionRequest() {
        return adoptionRequest;
    }*/

/*    public void setSelectedAdoptionRequest(AdoptionRequest adoptionRequest) {
        this.adoptionRequest = adoptionRequest;
    }*/

    public boolean requestSent(AdoptionPet adoptionPet) {
        User user = UserService.getInstance().getUser();
        if (user == null)
            return false;
        List<AdoptionRequest> adoptionRequests = getAdoptionRequestsToPets(user, 0);
        if (adoptionRequests == null) {
            return false;
        }
        for (AdoptionRequest adoptionRequest : adoptionRequests) {
            if (adoptionRequest == null)
                return false;
            if (adoptionRequest.getAdoptionPet().getID().equals(adoptionPet.getID()))
                return true;
        }
        return false;
    }

    public Map<AdoptionPet, AdoptionRequest> getAdoptionPetRequestedByUser(int page) {
        Map<AdoptionPet, AdoptionRequest> map = new HashMap<>();
        User user = UserService.getInstance().getUser();
        if (user == null)
            return null;
        List<AdoptionRequest> adoptionRequests = getAdoptionRequestsToPets(user, page);
        if (adoptionRequests == null) {
            return null;
        }

        for (AdoptionRequest adoptionRequest : adoptionRequests) {
            if (adoptionRequest == null)
                return null;
            map.put(adoptionRequest.getAdoptionPet(), adoptionRequest);
        }
        return map;
    }


    public List<AdoptionRequest> getAdoptionRequests(AdoptionPet adoptionPet) {
        List<AdoptionRequest> list = new ArrayList<>();
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.ADOPTION_PET, adoptionPet);
        query.whereNotEqualTo(AdoptionRequest.STATE, RequestState.IGNORED.toString());
        query.whereNotEqualTo(AdoptionRequest.STATE, RequestState.REJECTED.toString());
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public List<AdoptionRequest> getAdoptionRequestsByUser(int page) {
        User user = UserService.getInstance().getUser();
        return getAdoptionRequests(user, page, LIMIT_REQUEST);
    }

    private List<AdoptionRequest> getAdoptionRequestsToPets(User user, int page) {
        return getAdoptionRequests(user, page, LIMIT_PETS);
    }

    private List<AdoptionRequest> getAdoptionRequests(User user, int page, int limit) {
        List<AdoptionRequest> list = new ArrayList<>();
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.REQUESTING_USER, user.getParseUser());
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public void save(AdoptionRequest adoptionRequest) {
        adoptionRequest.saveInBackground();
        sendPushNotification(adoptionRequest);
    }

    private void sendPushNotification(AdoptionRequest adoptionRequest) {
        AdoptionPet adoptionPet = adoptionRequest.getAdoptionPet();
        User requestingUser = adoptionRequest.getRequestingUser();
        if (adoptionRequest.isAccepted()) {
            pushService.sendAcceptedRequestAdoptionPet(adoptionPet, requestingUser);
        } else if (adoptionRequest.isPending()) {
            pushService.sendRequestAdoptionPet(adoptionPet);
        }
    }
}
