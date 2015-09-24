package ar.uba.fi.nicodiaz.mascota.model;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.MyApplication;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PetService {

    private static PetService ourInstance = new PetService();

    public static PetService getInstance() {
        return ourInstance;
    }

    private PetService() {
    }

    private static List<AdoptionPet> adoptionPets;

    public void saveAdoptionPet(AdoptionPet adoptionPet) {
        adoptionPet.saveInBackground();
    }

    public List<AdoptionPet> getAdoptionPets() {
        List<AdoptionPet> adoptionPets = new ArrayList<>();
        ParseQuery<AdoptionPet> query = ParseQuery.getQuery(AdoptionPet.class);
        query.addDescendingOrder("createdAt");
        try {
            adoptionPets = query.find();
        } catch (ParseException e) {
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
/*        query.findInBackground(new FindCallback<AdoptionPet>() {
            @Override
            public void done(List<AdoptionPet> list, ParseException e) {
                if (e == null) {
                    PetService.adoptionPets = list;
                } else {
                    Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        return adoptionPets;
    }
}
