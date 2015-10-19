package ar.uba.fi.nicodiaz.mascota.utils.service;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

/**
 * Created by Juan Manuel Romera on 17/10/2015.
 */
public class MockPetFactory {

    public static List<AdoptionPet> createMaleDogAdoption(int total, String owner, String name, String age, Address address) {
        return createPetsAdoption(total, "Perro", owner, name, "Macho", age, address);
    }

    public static List<AdoptionPet> createFemaleDogAdoption(int total, String owner, String name, String age, Address address) {
        return createPetsAdoption(total, "Perro", owner, name, "Hembra", age, address);
    }

    public static List<AdoptionPet> createMaleCatsAdoption(int total, String owner, String name, String age, Address address) {
        return createPetsAdoption(total, "Gato", owner, name, "Macho", age, address);
    }

    public static List<AdoptionPet> createFemaleCatsAdoption(int total, String owner, String name, String age, Address address) {
        return createPetsAdoption(total, "Gato", owner, name, "Hembra", age, address);
    }

    public static List<MissingPet> createMaleDogsMissing(int total, String owner, String name, String age, Address address, Date lastKnowDate) {
        return createMissingPets(total, "Perro", owner, name, "Macho", age, address, lastKnowDate);
    }

    public static List<MissingPet> createFemaleDogsMissing(int total, String owner, String name, String age, Address address, Date lastKnowDate) {
        return createMissingPets(total, "Perro", owner, name, "Hembra", age, address, lastKnowDate);
    }

    public static List<MissingPet> createMaleCatsMissing(int total, String owner, String name, String age, Address address, Date lastKnowDate) {
        return createMissingPets(total, "Gato", owner, name, "Macho", age, address, lastKnowDate);
    }

    public static List<MissingPet> createFemaleCatsMissing(int total, String owner, String name, String age, Address address, Date lastKnowDate) {
        return createMissingPets(total, "Gato", owner, name, "Hembra", age, address, lastKnowDate);
    }

    private static List<MissingPet> createMissingPets(int total, String kind, String owner, String name, String gender, String age, Address address, Date lastKnowDate) {
        List<MissingPet> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            MissingPet missingPet = new MissingPet();
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

            missingPet.setLastKnowAddress(address);
            missingPet.setOwner(user);
            missingPet.setGender(gender);
            missingPet.setName(name + " " + i);
            missingPet.setAgeRange(age);
            missingPet.setKind(kind);
            missingPet.setLastKnowDate(lastKnowDate);

            list.add(missingPet);
        }

        return list;
    }

    public static List<AdoptionPet> createPetsAdoption(int total, String kind, String owner, String name, String gender, String age, Address address) {
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
