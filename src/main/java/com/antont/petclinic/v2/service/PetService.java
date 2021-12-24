package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public String getPetsNames(){
        return getPets().stream()
                .map(Pet::getName)
                .collect(Collectors.joining(", "));
    }

    public List<Pet> getPets(){
         return petRepository.findAll();
    }
}
