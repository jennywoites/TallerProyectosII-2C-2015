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

    public boolean hasAdoptionRequestSent(AdoptionPet adoptionPet) {
        User user = UserService.getInstance().getUser();

        if (user == null) {
            return false;
        }

        int requestCount = 0;
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.REQUESTING_USER, user.getParseUser());
        query.whereEqualTo(AdoptionRequest.ADOPTION_PET, adoptionPet);
        try {
            requestCount = query.count();
        } catch (ParseException e) {
            return false;
        }

        return requestCount > 0;
    }

    public boolean hasMissingRequestSent(MissingPet missingPet) {
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

    public boolean hasFoundRequestSent(FoundPet foundPet) {
        User user = UserService.getInstance().getUser();

        if (user == null) {
            return false;
        }

        int requestCount = 0;
        ParseQuery<FoundRequest> query = ParseQuery.getQuery(FoundRequest.class);
        query.whereEqualTo(FoundRequest.REQUESTING_USER, user.getParseUser());
        query.whereEqualTo(FoundRequest.FOUND_PET, foundPet);
        try {
            requestCount = query.count();
        } catch (ParseException e) {
            return false;
        }

        return requestCount > 0;
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


    public List<AdoptionRequest> getAllAdoptionRequests(AdoptionPet adoptionPet) {
        List<AdoptionRequest> list;
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.ADOPTION_PET, adoptionPet);
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public List<AdoptionRequest> getAdoptionRequests(AdoptionPet adoptionPet) {
        List<AdoptionRequest> list;
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.ADOPTION_PET, adoptionPet);
        String[] states = {RequestState.IGNORED.toString(), RequestState.REJECTED.toString()};
        query.whereNotContainedIn(AdoptionRequest.STATE, Arrays.asList(states));
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
        query.whereNotContainedIn(MissingPet.STATE, Arrays.asList(states));
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public List<FoundRequest> getFoundRequests(FoundPet foundPet) {
        List<FoundRequest> list;
        ParseQuery<FoundRequest> query = ParseQuery.getQuery(FoundRequest.class);
        query.whereEqualTo(FoundRequest.FOUND_PET, foundPet);
        String[] states = {RequestState.IGNORED.toString(), RequestState.REJECTED.toString()};
        query.whereNotContainedIn(FoundRequest.STATE, Arrays.asList(states));
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

    public List<MissingRequest> getMissingRequestsByUser(int page) {
        User user = UserService.getInstance().getUser();
        return getMissingRequests(user, page, LIMIT_REQUEST);
    }

    public List<FoundRequest> getFoundRequestsByUser(int page) {
        User user = UserService.getInstance().getUser();
        return getFoundRequests(user, page, LIMIT_REQUEST);
    }

    private List<AdoptionRequest> getAdoptionRequestsToPets(User user, int page) {
        return getAdoptionRequests(user, page, LIMIT_PETS);
    }

    private List<AdoptionRequest> getAdoptionRequests(User user, int page, int limit) {
        List<AdoptionRequest> list = new ArrayList<>();

        // Obtengo las mascotas banneadas:
        ParseQuery<AdoptionPet> innerQuery = ParseQuery.getQuery(AdoptionPet.class);
        innerQuery.whereEqualTo(AdoptionPet.BANNED, true);

        // Obtengo las request cuya mascota no este banneada:
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereDoesNotMatchQuery(AdoptionRequest.ADOPTION_PET, innerQuery);

        query.whereEqualTo(AdoptionRequest.REQUESTING_USER, user.getParseUser());
        String[] states = {RequestState.IGNORED.toString(), RequestState.REJECTED.toString()};
        query.whereNotContainedIn(AdoptionRequest.STATE, Arrays.asList(states));
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    private List<MissingRequest> getMissingRequests(User user, int page, int limit) {
        List<MissingRequest> list = new ArrayList<>();

        // Obtengo las mascotas banneadas:
        ParseQuery<MissingPet> innerQuery = ParseQuery.getQuery(MissingPet.class);
        innerQuery.whereEqualTo(MissingPet.BANNED, true);

        ParseQuery<MissingRequest> query = ParseQuery.getQuery(MissingRequest.class);
        query.whereDoesNotMatchQuery(MissingRequest.MISSING_PET, innerQuery);

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

    private List<FoundRequest> getFoundRequests(User user, int page, int limit) {
        List<FoundRequest> list = new ArrayList<>();


        // Obtengo las mascotas banneadas:
        ParseQuery<FoundPet> innerQuery = ParseQuery.getQuery(FoundPet.class);
        innerQuery.whereEqualTo(FoundPet.BANNED, true);

        ParseQuery<FoundRequest> query = ParseQuery.getQuery(FoundRequest.class);
        query.whereDoesNotMatchQuery(FoundRequest.FOUND_PET, innerQuery);

        query.whereEqualTo(FoundRequest.REQUESTING_USER, user.getParseUser());
        String[] states = {RequestState.IGNORED.toString(), RequestState.REJECTED.toString()};
        query.whereNotContainedIn(FoundRequest.STATE, Arrays.asList(states));
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
    }

    public void save(AdoptionRequest adoptionRequest) {
        adoptionRequest.saveInBackground();
    }

    public void save(FoundRequest foundRequest) {
        foundRequest.saveInBackground();
    }

    public void save(MissingRequest missingRequest) {
        missingRequest.saveInBackground();
    }

    public void save(AdoptionRequest adoptionRequest, List<AdoptionRequest> adoptionRequestIgnored) {
        adoptionRequest.saveInBackground();
        sendPushNotification(adoptionRequest, adoptionRequestIgnored);
    }

    private void sendPushNotification(AdoptionRequest adoptionRequest, List<AdoptionRequest> adoptionRequestIgnored) {
        AdoptionPet adoptionPet = adoptionRequest.getAdoptionPet();
        User requestingUser = adoptionRequest.getRequestingUser();
        if (adoptionRequest.isAccepted()) {
            pushService.sendAcceptedRequestAdoptionPet(adoptionPet, requestingUser);
            pushService.sendIgnoredRequestAdoptionPet(adoptionRequest, adoptionRequestIgnored);
        }
    }

    public void save(FoundRequest foundRequest, List<FoundRequest> foundRequestIgnored) {
        foundRequest.saveInBackground();
        sendPushNotification(foundRequest, foundRequestIgnored);
    }

    private void sendPushNotification(FoundRequest foundRequest, List<FoundRequest> foundRequestIgnored) {
        FoundPet foundPet = foundRequest.getFoundPet();
        User requestingUser = foundRequest.getRequestingUser();
        if (foundRequest.isAccepted()) {
            pushService.sendAcceptedRequestFoundPet(foundPet, requestingUser);
            pushService.sendIgnoredRequestFoundPet(foundRequest, foundRequestIgnored);
        }
    }

    public void save(MissingRequest missingRequest, List<MissingRequest> missingRequestsIgnored) {
        missingRequest.saveInBackground();
        sendPushNotification(missingRequest, missingRequestsIgnored);
    }

    private void sendPushNotification(MissingRequest missingRequest, List<MissingRequest> missingRequestsIgnored) {
        MissingPet missingPet = missingRequest.getMissingPet();
        User requestingUser = missingRequest.getRequestingUser();
        if (missingRequest.isAccepted()) {
            pushService.sendAcceptedRequestMissingPet(missingPet, requestingUser);
            pushService.sendIgnoredRequestMissingPet(missingRequest, missingRequestsIgnored);
        }
    }

    public List<FoundRequest> getAllFoundRequests(FoundPet foundPet) {
        List<FoundRequest> list;
        ParseQuery<FoundRequest> query = ParseQuery.getQuery(FoundRequest.class);
        query.whereEqualTo(FoundRequest.FOUND_PET, foundPet);
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return list;
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


}
