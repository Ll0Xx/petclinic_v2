package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.dto.IssueDto;
import com.antont.petclinic.v2.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping(path = "/doctor/issue/create")
    public ResponseEntity<Issue> update(@ModelAttribute IssueDto dto) {
        return ResponseEntity.ok().body(issueService.handlePetRequest(dto));
    }

    @DeleteMapping(path = "/doctor/issue/delete/{id}")
    public ResponseEntity<BigInteger> delete(@PathVariable("id") BigInteger id) {
        return ResponseEntity.ok().body(issueService.delete(id));
    }
}
