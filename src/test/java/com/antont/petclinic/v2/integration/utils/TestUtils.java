package com.antont.petclinic.v2.integration.utils;

import com.antont.petclinic.v2.db.entity.*;
import com.antont.petclinic.v2.db.repository.*;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TestUtils {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorSpecializationRepository doctorSpecializationRepository;
    private final IssueRepository issueRepository;

    public TestUtils(UserRepository userRepository, RoleRepository roleRepository, PetRepository petRepository,
                     PetTypeRepository petTypeRepository, DoctorRepository doctorRepository, DoctorSpecializationRepository doctorSpecializationRepository, IssueRepository issueRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
        this.doctorRepository = doctorRepository;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.issueRepository = issueRepository;
    }

    @Transactional
    public User initTestUser(String email, boolean isDoctor) {
       return  userRepository.findByEmail(email).orElseGet(() -> {
           User user = new User();
           Role role = roleRepository.findByName(isDoctor ? "ROLE_DOCTOR" : "ROLE_USER").get();
           user.setRole(role);
           user.setEmail(email);
           user.setPassword("Aaa12#aa");
           return userRepository.save(user);
       });

    }

    @Transactional
    public Doctor initTestDoctor(String email) {
        return  doctorRepository.findByUser(initTestUser(email, true)).orElseGet(() -> {
            Doctor doctor = new Doctor();
            doctor.setDoctorSpecialization(initDoctorSpecialization());
            doctor.setUser(initTestUser(email, true));
            return doctorRepository.save(doctor);
        });

    }

    @Transactional
    public DoctorSpecialization initDoctorSpecialization(){
        return doctorSpecializationRepository.findAll().stream().findFirst().orElseGet(() -> {
            DoctorSpecialization specialization = new DoctorSpecialization();
            specialization.setName("test specialization");
            return  doctorSpecializationRepository.save(specialization);
        });
    }

    @Transactional
    public Issue initTestIssue(String doctorEmail, String userEmail){
        Issue issue = new Issue();
        issue.setDoctor(initTestDoctor(doctorEmail));
        issue.setDescription("issue test description");
        issue.setPet(initTestPet(userEmail));
        return issueRepository.save(issue);
    }

    @Transactional
    public Pet initTestPet(String userEmail) {
        return petRepository.findByOwner(initTestUser(userEmail, false)).stream().findFirst().orElseGet(() -> {
            Pet pet = new Pet();
            PetType type = initTestPetType();
            pet.setPetType(type);
            pet.setName("test pet");
            pet.setOwner(initTestUser(userEmail, false));
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
