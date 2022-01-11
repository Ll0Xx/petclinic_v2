package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.dto.PetDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Pet handlePetRequest(PetDto dto) {
        if (dto.getId() == null) {
            return savePet(dto);
        } else {
            return editPet(dto.getId(), dto);
        }
    }
    
    private Pet savePet(PetDto dto) {
        User user = userService.getLoggedInUser();
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setOwner(user);
        pet.setPetType(getPetType(dto.getPetType()));
        petRepository.save(pet);
        return pet;
    }

    private Pet editPet(BigInteger id, PetDto dto) {
        User user = userService.getLoggedInUser();
        Optional<Pet> pet = petRepository.findById(id);
        return pet.map(pet1 -> {
            if (pet1.getOwner() == user) {
                pet1.setName(dto.getName());
                pet1.setPetType(getPetType(dto.getPetType()));
                petRepository.save(pet1);
                return pet1;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error while trying to edit pet");
            }
        }).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Pet with id: " + id + " not found");
        });
    }

    public BigInteger deletePet(BigInteger id) {
        User user = userService.getLoggedInUser();
        Optional<Pet> pet = petRepository.findById(id);
        return pet.map(pet1 -> {
            if (pet1.getOwner() == user) {
                petRepository.delete(pet1);
                return id;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error while trying to delete pet");
            }
        }).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet with id: " + id + " not found");
        });
    }

    public List<PetType> getPetTypes(){
        return petTypeRepository.findAll();
    }

    private PetType getPetType(BigInteger id) {
        return petTypeRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet type with id: " + id + " not found");
        });
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(User owner) {
        return petRepository.findByOwner(owner);
    }
}
