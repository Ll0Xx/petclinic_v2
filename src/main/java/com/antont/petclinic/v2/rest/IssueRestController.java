package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.dto.IssueDto;
import com.antont.petclinic.v2.service.IssueService;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.service.utils.ServiceUtils;
import com.antont.petclinic.v2.validation.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
public class IssueRestController {

    private final IssueService issueService;
    private final PetService petService;

    public IssueRestController(IssueService issueService, PetService petService) {
        this.issueService = issueService;
        this.petService = petService;
    }

    @GetMapping(path = "/user/issue")
    public Page<Issue> getPagedIssuesForUser(@RequestParam("page") Optional<Integer> page,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam("sort") Optional<String> sort,
                                             @RequestParam("dir") Optional<String> direction) {
        return issueService.getPagedForUser(page, size, sort, direction);
    }

    @GetMapping(path = "/doctor/issue")
    public Page<Issue> getPagedIssuesForDoctor(@RequestParam("page") Optional<Integer> page,
                                               @RequestParam("size") Optional<Integer> size,
                                               @RequestParam("sort") Optional<String> sort,
                                               @RequestParam("dir") Optional<String> direction,
                                               @RequestParam("keyword") Optional<String> keyword) {
        return issueService.getPagedForDoctor(page, size, sort, direction, keyword);
    }

    @GetMapping(path = "/doctor/issue/last")
    public Page<Issue> getLastIssuesPageForDoctor() {
        return issueService.getLastPageForDoctor();
    }

    @PostMapping(path = "/doctor/issue/create")
    public ValidationResult create(@Valid @RequestBody IssueDto dto, BindingResult bindingResult) {
        return ServiceUtils.generateValidationResult(bindingResult, validationResult ->
                validationResult.setResult(issueService.create(dto)));
    }

    @PostMapping(path = "/doctor/issue/update")
    public ValidationResult update(@Valid @RequestBody IssueDto dto, BindingResult bindingResult) {
        return ServiceUtils.generateValidationResult(bindingResult, validationResult ->
                validationResult.setResult(issueService.update(dto)));
    }

    @DeleteMapping(path = "/doctor/issue/delete/{id}")
    public ResponseEntity<BigInteger> delete(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok().body(issueService.delete(id));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/doctor/issue/search")
    public ResponseEntity<List<Pet>> search(@RequestParam String keyword) {
        try {
            return ResponseEntity.ok().body(petService.findByKeyword(keyword));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
