package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.utils.PageableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class PetService {

    Logger log = LoggerFactory.getLogger(PetService.class);

    private final UserService userService;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;

    public PetService(UserService userService, PetRepository petRepository, PetTypeRepository petTypeRepository) {
        this.userService = userService;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
    }

    public String getPetsNames() {
        return getPets().stream()
                .map(Pet::getName)
                .collect(Collectors.joining(", "));
    }

    public List<Pet> findByKeyword(String keyword) {
        return petRepository.findByNameLike("%" + keyword + "%");
    }

    public Pet handlePetRequest(PetDto dto) {
        return dto.getId() == null ? create(dto) : update(dto);
    }

    public Pet create(PetDto dto) {
        User user = userService.getLoggedInUser();
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setOwner(user);
        pet.setPetType(getPetType(dto.getPetType()));
        petRepository.save(pet);
        return pet;
    }

    private Pet findPetByIdForCurrentUser(BigInteger id, Consumer<Pet> petConsumer) {
        User user = userService.getLoggedInUser();
        Optional<Pet> pet = petRepository.findByIdAndOwner(id, user);
        return pet.map(pet1 -> {
            petConsumer.accept(pet1);
            return pet1;
        }).orElseThrow(() -> {
            log.error("Failed to update, pet with id " + id + " for user " + user.getEmail() + " not found");
            throw new RuntimeException("Error while trying to modify/delete pet");
        });
    }


    
    public Pet update(PetDto dto) {
        return findPetByIdForCurrentUser(dto.getId(), pet -> {
            pet.setName(dto.getName());
            pet.setPetType(getPetType(dto.getPetType()));
            petRepository.save(pet);
        });
    }

    public BigInteger deletePet(BigInteger id) {
        return findPetByIdForCurrentUser(id, pet -> {
            try {
                petRepository.delete(pet);
            } catch (Exception e) {
                throw new RuntimeException("Unable to delete pet, maybe it's already added to the some issue");
            }
        }).getId();
    }

    public List<PetType> getPetTypes() {
        return petTypeRepository.findAll();
    }

    private PetType getPetType(BigInteger id) {
        return petTypeRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to update, pet type with id " + id + " not found");
            throw new RuntimeException("Pet type with id: " + id + " not found");
        });
    }

    public Page<Pet> getPetsPaged(Optional<Integer> page, Optional<Integer> size, Optional<String> sort, Optional<String> direction) {
        return petRepository.findAllByOwner(userService.getLoggedInUser(), PageableUtils.getPageable(page, size, sort, direction));
    }

    public Page<Pet> getPetsLastPage() {
        Page<Pet> petPage = petRepository.findAllByOwner(userService.getLoggedInUser(), Pageable.ofSize(PageableUtils.DEFAULT_PAGE_SIZE));
        int pageCount = petPage.getTotalPages() - 1;
        return petRepository.findAllByOwner(userService.getLoggedInUser(), PageRequest.of(pageCount, PageableUtils.DEFAULT_PAGE_SIZE));
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(User owner) {
        return petRepository.findByOwner(owner);
    }
}
