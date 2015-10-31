package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JFERRIO on 13/10/2015.
 */
public class RequestMissingService {

    private static final int LIMIT_PETS = 4;
    private static final int LIMIT_REQUEST = 10;
    private static RequestMissingService ourInstance = new RequestMissingService();
    private static PushService pushService;

    public static RequestMissingService getInstance() {
        return ourInstance;
    }

    private RequestMissingService() {
        pushService = PushService.getInstance();
    }

    private MissingRequest missingRequest;

/*    public MissingRequest getSelectedMissingRequest() {
        return missingRequest;
    }*/

/*    public void setSelectedMissingRequest(MissingRequest missingRequest) {
        this.missingRequest = missingRequest;
    }*/

    public boolean requestSent(MissingPet missingPet) {
        User user = UserService.getInstance().getUser();

        if (user == null) {
            return false;
        }

        int requestCount = 0;
        ParseQuery<MissingRequest> query = ParseQuery.getQuery(MissingRequest.class);
        query.whereEqualTo(MissingRequest.REQUESTING_USER, user.getParseUser());
        query.whereEqualTo(MissingRequest.MISSING_PET, missingPet);
        try {
            requestCount = query.count();
        } catch (ParseException e) {
            return false;
        }

        return requestCount > 0;
    }

    public Map<MissingPet, MissingRequest> getMissingPetRequestedByUser(int page) {
        Map<MissingPet, MissingRequest> map = new HashMap<>();
        User user = UserService.getInstance().getUser();
        if (user == null)
            return null;
        List<MissingRequest> missingRequests = getMissingRequestsToPets(user, page);
        if (missingRequests == null) {
            return null;
        }

        for (MissingRequest missingRequest : missingRequests) {
            if (missingRequest == null)
                return null;
            map.put(missingRequest.getMissingPet(), missingRequest);
        }
        return map;
    }


    public List<MissingRequest> getAllMissingRequests(MissingPet missingPet) {
        List<MissingRequest> list;
        ParseQuery<MissingRequest> query = ParseQuery.getQuery(MissingRequest.class);
        query.whereEqualTo(MissingRequest.MISSING_PET, missingPet);
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public List<MissingRequest> getMissingRequests(MissingPet missingPet) {
        List<MissingRequest> list;
        ParseQuery<MissingRequest> query = ParseQuery.getQuery(MissingRequest.class);
        query.whereEqualTo(MissingRequest.MISSING_PET, missingPet);
        String[] states = {RequestState.IGNORED.toString(), RequestState.REJECTED.toString()};
        query.whereNotContainedIn(MissingRequest.STATE, Arrays.asList(states));
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public List<MissingRequest> getMissingRequestsByUser(int page) {
        User user = UserService.getInstance().getUser();
        return getMissingRequests(user, page, LIMIT_REQUEST);
    }

    private List<MissingRequest> getMissingRequestsToPets(User user, int page) {
        return getMissingRequests(user, page, LIMIT_PETS);
    }

    private List<MissingRequest> getMissingRequests(User user, int page, int limit) {
        List<MissingRequest> list = new ArrayList<>();
        ParseQuery<MissingRequest> query = ParseQuery.getQuery(MissingRequest.class);
        query.whereEqualTo(MissingRequest.REQUESTING_USER, user.getParseUser());
        String[] states = {RequestState.IGNORED.toString(), RequestState.REJECTED.toString()};
        query.whereNotContainedIn(MissingRequest.STATE, Arrays.asList(states));
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public void save(MissingRequest missingRequest) {
        missingRequest.saveInBackground();
    }

    public void save(MissingRequest missingRequest, List<MissingRequest> missingRequestIgnored) {
        missingRequest.saveInBackground();
        sendPushNotification(missingRequest, missingRequestIgnored);
    }

    private void sendPushNotification(MissingRequest missingRequest, List<MissingRequest> missingRequestIgnored) {
        MissingPet missingPet = missingRequest.getMissingPet();
        User requestingUser = missingRequest.getRequestingUser();
        if (missingRequest.isAccepted()) {
            pushService.sendAcceptedRequestMissingPet(missingPet, requestingUser);
            pushService.sendIgnoredRequestMissingPet(missingRequest, missingRequestIgnored);
        }
    }
}
