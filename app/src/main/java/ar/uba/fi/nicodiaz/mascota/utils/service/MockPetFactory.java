package ar.uba.fi.nicodiaz.mascota.utils.service;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

/**
 * Created by Juan Manuel Romera on 17/10/2015.
 */
public class MockPetFactory {

    public static List<AdoptionPet> createMaleDogPets(int total, String owner, String name, String age, Address address) {
        return createPets(total, "Perro", owner, name, "Macho", age, address);
    }

    public static List<AdoptionPet> createFemaleDogPets(int total, String owner, String name, String age, Address address) {
        return createPets(total, "Perro", owner, name, "Hembra", age, address);
    }

    public static List<AdoptionPet> createMaleCats(int total, String owner, String name, String age, Address address) {
        return createPets(total, "Gato", owner, name, "Macho", age, address);
    }

    public static List<AdoptionPet> createFemaleCats(int total, String owner, String name, String age, Address address) {
        return createPets(total, "Gato", owner, name, "Hembra", age, address);
    }

    public static List<AdoptionPet> createPets(int total, String kind, String owner, String name, String gender, String age, Address address) {
        List<AdoptionPet> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            AdoptionPet adoptionPet = new AdoptionPet();
            User user;
            if (owner.equals("")) {
                user = UserService.getInstance().getUser();
            } else {
                ParseUser parseUser = new ParseUser();
                user = new User(parseUser);
                user.setName(owner);
                user.setGender("male");
                user.setAddress(address);
            }

            adoptionPet.setLocation(user.getAddress());
            adoptionPet.setOwner(user);
            adoptionPet.setGender(gender);
            adoptionPet.setName(name + " " + i);
            adoptionPet.setAgeRange(age);
            adoptionPet.setKind(kind);
            adoptionPet.setSocialNotes("");
            adoptionPet.setDescription("");
            adoptionPet.setBreed("");
            adoptionPet.setChildren("");
            adoptionPet.setMedicine("");
            adoptionPet.setMedicineTime("");
            adoptionPet.setMedicineNotes("");

            list.add(adoptionPet);
        }

        return list;

    }
}
