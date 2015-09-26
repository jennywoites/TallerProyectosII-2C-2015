package ar.uba.fi.nicodiaz.mascota.model;

import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.MyApplication;
import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PetService {

    private static PetService ourInstance = new PetService();
    private static Integer LIMIT = 4;
    private static String CAT = "Gato";
    private static String DOG = "Perro";


    public static PetService getInstance() {
        return ourInstance;
    }

    private PetService() {
    }

    private static List<AdoptionPet> adoptionPets;

    public void saveAdoptionPet(AdoptionPet adoptionPet) {
        adoptionPet.saveInBackground();
    }

    public List<AdoptionPet> getAdoptionPets(int page) {
        User user = UserService.getInstance().getUser();
        List<AdoptionPet> adoptionPets = new ArrayList<>();
        ParseQuery<AdoptionPet> query = ParseQuery.getQuery(AdoptionPet.class);
        query.addDescendingOrder("createdAt");
        query.whereNotEqualTo(AdoptionPet.OWNER, user.getParseUser());
        query.setLimit(LIMIT);
        query.setSkip(page * LIMIT);

        try {
            adoptionPets = query.find();
        } catch (ParseException e) {
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return adoptionPets;
    }

    public int getIconPet(String petKind) {
        if (petKind.equals(DOG)) {
            return R.drawable.ic_dog;
        } else if (petKind.equals(CAT)) {
            return R.drawable.ic_cat;
        }
        return -1;
    }
}
