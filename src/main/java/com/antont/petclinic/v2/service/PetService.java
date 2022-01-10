package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.dto.PetDto;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final UserService userService;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;

    public PetService(UserService userService, PetRepository petRepository, PetTypeRepository petTypeRepository) {
        this.userService = userService;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
    }

    public String getPetsNames(){
        return getPets().stream()
                .map(Pet::getName)
                .collect(Collectors.joining(", "));
    }

    public void savePet(PetDto dto) {
        User user = userService.getLoggedInUser();
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setOwner(user);
        pet.setPetType(getPetType(dto.getPetType()));
        petRepository.save(pet);
    }

    public void editPet(BigInteger id, PetDto dto) {
        User user = userService.getLoggedInUser();
        Optional<Pet> pet = petRepository.findById(id);
        pet.ifPresentOrElse(pet1 -> {
            if (pet1.getOwner() != user) {
                pet1.setName(dto.getName());
                pet1.setPetType(getPetType(dto.getPetType()));
            } else {
                throw new RuntimeException("Error while trying to edit pet");
            }
        }, () -> {
            throw new RuntimeException("Pet with id: " + id + " not found");
        });
    }

    public void deletePet(BigInteger id){
        User user = userService.getLoggedInUser();
        Optional<Pet> pet = petRepository.findById(id);
        pet.ifPresentOrElse(pet1 -> {
            if (pet1.getOwner() != user) {
                petRepository.delete(pet1);
            } else {
                throw new RuntimeException("Error while trying to delete pet");
            }
        }, () -> {
            throw new RuntimeException("Pet with id: " + id + " not found");
        });
    }

    public List<PetType> getPetTypes(){
        return petTypeRepository.findAll();
    }

    private PetType getPetType(BigInteger id) {
        return petTypeRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Pet type not found");
        });
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(User owner) {
        return petRepository.findByOwner(owner);
    }
}
