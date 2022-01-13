package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.auth.dto.CreateUserDto;
import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.entity.Role;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.DoctorRepository;
import com.antont.petclinic.v2.db.repository.DoctorSpecializationRepository;
import com.antont.petclinic.v2.db.repository.RoleRepository;
import com.antont.petclinic.v2.db.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorSpecializationRepository doctorSpecializationRepository;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, DoctorRepository doctorRepository,
                       DoctorSpecializationRepository doctorSpecializationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
    }

    public void create(CreateUserDto dto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);

        getRoleByName(dto.getDoctor() ? "ROLE_DOCTOR" : "ROLE_USER").ifPresentOrElse(user::setRole, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while creating user");
        });

        user = userRepository.save(user);

        if (Objects.equals(user.getRole().getName(), "ROLE_DOCTOR")) {
            Doctor doc = new Doctor();
            doc.setUser(user);
            doctorSpecializationRepository.findById(dto.getDoctorSpecialization()).ifPresentOrElse(doctorSpecialization -> {
                doc.setDoctorSpecialization(doctorSpecialization);
                doctorRepository.save(doc);
            }, () -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while creating doctor");
            });
        }
    }

    private Optional<Role> getRoleByName(String name){
        return roleRepository.findByName(name);
    }

}
