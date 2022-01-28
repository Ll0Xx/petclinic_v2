package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.DoctorRepository;
import com.antont.petclinic.v2.db.repository.IssueRepository;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.dto.IssueDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    Logger log = LoggerFactory.getLogger(IssueService.class);

    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final IssueRepository issueRepository;
    private final PetRepository petRepository;

    public IssueService(UserService userService, DoctorRepository doctorRepository, IssueRepository issueRepository,
                        PetRepository petRepository) {
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.issueRepository = issueRepository;
        this.petRepository = petRepository;
    }

    public List<Issue> findIssuesForUser(User user){
        return issueRepository.findByPetOwner(user);
    }

    public List<Issue> findIssuesForDoctor(Doctor user){
        return issueRepository.findByDoctor(user);
    }

    public List<Issue> findAllIssues(){
        return issueRepository.findAll();
    }

    public List<Issue> findByKeyword(String keyword){
        return issueRepository.findByPetNameLike("%" + keyword + "%");
    }

    public Issue handleIssueRequest(IssueDto dto) {
        return dto.getId() == null ? create(dto) : update(dto);
    }

    private Issue create(IssueDto dto) {
        Issue issue = new Issue();
        doctorRepository.findById(dto.getDoctor()).ifPresentOrElse(issue::setDoctor, () -> {
            log.error("Failed to create, doctor with id " + dto.getDoctor() + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to create pet issue");
        });
        petRepository.findById(dto.getPet()).ifPresentOrElse(issue::setPet, () -> {
            log.error("Failed to create, pet with id " + dto.getPet() + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to create pet issue");
        });
        issue.setDescription(dto.getDescription());
        issueRepository.save(issue);
        return issue;
    }

    private Issue update(IssueDto dto) {
        Optional<Issue> issue = issueRepository.findById(dto.getId());
        return issue.map(issue1 -> {
            if (!issue1.getPet().getId().equals(dto.getPet())) {
                petRepository.findById(dto.getPet()).ifPresent(issue1::setPet);
            }
            if (!issue1.getDoctor().getId().equals(dto.getDoctor())) {
                doctorRepository.findById(dto.getDoctor()).ifPresent(issue1::setDoctor);
            }
            issue1.setDescription(dto.getDescription());
            return issueRepository.save(issue1);
        }).orElseThrow(() -> {
            log.error("Failed to update, issue with id " + dto.getId() + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to modify pet issue");
        });
    }

    public BigInteger delete(BigInteger id) {
        return issueRepository.findById(id).map(issue -> {
            issueRepository.delete(issue);
            return issue.getId();
        }).orElseThrow(() -> {
            log.error("Failed to delete, issue with id " + id + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to delete pet issue");
        });
    }
}
