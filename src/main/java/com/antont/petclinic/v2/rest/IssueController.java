package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.dto.IssueDto;
import com.antont.petclinic.v2.service.IssueService;
import com.antont.petclinic.v2.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class IssueController {

    private final IssueService issueService;
    private final PetService petService;

    public IssueController(IssueService issueService, PetService petService) {
        this.issueService = issueService;
        this.petService = petService;
    }

    @PostMapping(path = "/doctor/issue/create")
    public ResponseEntity<Issue> update(@ModelAttribute IssueDto dto) {
        return ResponseEntity.ok().body(issueService.handleIssueRequest(dto));
    }

    @DeleteMapping(path = "/doctor/issue/delete/{id}")
    public ResponseEntity<BigInteger> delete(@PathVariable("id") BigInteger id) {
        return ResponseEntity.ok().body(issueService.delete(id));
    }

    @GetMapping(path = "/doctor/issue/search")
    public ResponseEntity<List<Pet>> search(@RequestParam String keyword) {
        return ResponseEntity.ok().body(petService.findByKeyword(keyword));
    }
}
