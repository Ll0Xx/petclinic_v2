package com.antont.petclinic.v2;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.entity.Role;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.db.repository.RoleRepository;
import com.antont.petclinic.v2.db.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TestUtils {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;

    public TestUtils(UserRepository userRepository, RoleRepository roleRepository, PetRepository petRepository,
                     PetTypeRepository petTypeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @Transactional
    public User initTestUser() {
       return  userRepository.findByEmail("email@mail.com").orElseGet(() -> {
           User user = new User();
           Role role = roleRepository.findByName("ROLE_USER").get();
           user.setRole(role);
           user.setEmail("email@mail.com");
           user.setPassword("Aaa12#aa");
           return userRepository.save(user);
       });

    }

    @Transactional
    public Pet initTestPet() {
        return petRepository.findByOwner(initTestUser()).stream().findFirst().orElseGet(() -> {
            Pet pet = new Pet();
            PetType type = initTestPetType();
            pet.setPetType(type);
            pet.setName("test pet");
            pet.setOwner(initTestUser());
            return petRepository.save(pet);
        });

    }

    @Transactional
    public PetType initTestPetType(){
        return petTypeRepository.findAll().stream().findFirst().orElseGet(() -> {
            PetType type = new PetType();
            type.setName("test type");
            return petTypeRepository.save(type);
        });
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
