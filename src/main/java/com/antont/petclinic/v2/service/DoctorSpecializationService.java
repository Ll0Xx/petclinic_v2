package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.repository.DoctorSpecializationRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorSpecializationService {

    private final DoctorSpecializationRepository doctorSpecializationRepository;

    public DoctorSpecializationService(DoctorSpecializationRepository doctorSpecializationRepository) {
        this.doctorSpecializationRepository = doctorSpecializationRepository;
    }

}
