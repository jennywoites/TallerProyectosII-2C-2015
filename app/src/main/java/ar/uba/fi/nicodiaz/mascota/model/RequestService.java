package ar.uba.fi.nicodiaz.mascota.model;

import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.MyApplication;

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

    public List<AdoptionPet> getAdoptionPetRequestedByUser(int page) {
        List<AdoptionPet> adoptionPets = new ArrayList<>();
        User user = UserService.getInstance().getUser();
        List<AdoptionRequest> adoptionRequests = getAdoptionRequestsToPets(user, page);
        for (AdoptionRequest adoptionRequest : adoptionRequests) {
            adoptionPets.add(adoptionRequest.getAdoptionPet());
        }

        return adoptionPets;
    }


    public List<AdoptionRequest> getAdoptionRequests(AdoptionPet adoptionPet) {
        List<AdoptionRequest> list = new ArrayList<>();
        ParseQuery<AdoptionRequest> query = ParseQuery.getQuery(AdoptionRequest.class);
        query.whereEqualTo(AdoptionRequest.ADOPTION_PET, adoptionPet);
        try {
            list = query.find();
        } catch (ParseException e) {
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return list;
    }

    public void save(AdoptionRequest adoptionRequest) {
        adoptionRequest.saveInBackground();
    }
}