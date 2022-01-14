package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Optional<Doctor> findByUser(User user){
        return doctorRepository.findByUser(user);
    }

    public List<Doctor> getFirst10Doctors() {
        return doctorRepository.findFirst10ByOrderById();
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }
}
