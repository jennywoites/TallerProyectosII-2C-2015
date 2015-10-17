package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JFERRIO on 13/10/2015.
 */
public class RequestService {

    private static final int LIMIT_PETS = 4;
    private static final int LIMIT_REQUEST = 10;
    private static RequestService ourInstance = new RequestService();

    public static RequestService getInstance() {
        return ourInstance;
    }

    private RequestService() {

    }

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

    public List<AdoptionPet> getAdoptionPetRequestedByUser(int page) {
        List<AdoptionPet> adoptionPets = new ArrayList<>();
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
            adoptionPets.add(adoptionRequest.getAdoptionPet());
        }

        return adoptionPets;
    }


    public List<AdoptionRequest> getAdoptionRequests(AdoptionPet adoptionPet) {
        List<AdoptionRequest> list = new ArrayList<>();
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.ADOPTION_PET, adoptionPet);
        query.whereNotEqualTo(AdoptionRequest.STATE, RequestState.IGNORED.toString());
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
    }
}
