package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getFirst10Doctors(){
        return doctorRepository.findFirst10ByOrderById();
    }
}
