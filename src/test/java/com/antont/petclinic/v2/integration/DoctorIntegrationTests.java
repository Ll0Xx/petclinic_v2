package com.antont.petclinic.v2.integration;

import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.dto.IssueDto;
import com.antont.petclinic.v2.integration.base.BaseIntegrationTest;
import com.antont.petclinic.v2.integration.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DoctorIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private PetRepository petRepository;

    @Test
    public void createIssue_shouldSuccess() throws Exception {
        Doctor doctor = testUtils.initTestDoctor("doctor@mail.com");
        Pet pet = testUtils.initTestPet("email@mail.com");

        IssueDto dto = new IssueDto();
        dto.setDoctor(doctor.getId());
        dto.setDescription("issue test description");
        dto.setPet(pet.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/doctor/issue/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(doctor.getUser().getEmail()).roles("DOCTOR"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.doctor.id").value(dto.getDoctor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.pet.id").value(dto.getPet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(dto.getDescription()));
    }

    @Test
    public void createIssueWithoutCsrf_shouldSuccess() throws Exception {
        Doctor doctor = testUtils.initTestDoctor("doctor@mail.com");
        Pet pet = testUtils.initTestPet("email@mail.com");

        IssueDto dto = new IssueDto();
        dto.setDoctor(doctor.getId());
        dto.setDescription("issue test description");
        dto.setPet(pet.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/doctor/issue/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(doctor.getUser().getEmail()).roles("DOCTOR"))
                        .content(TestUtils.asJsonString(dto))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateIssue_shouldSuccess() throws Exception {
        String userEmail = "user@mail.com";
        Issue issue = testUtils.initTestIssue("doctor@mail.com", userEmail );

        Pet pet = new Pet();
        PetType type = testUtils.initTestPetType();
        pet.setPetType(type);
        pet.setName("new test pet");
        pet.setOwner(testUtils.initTestUser(userEmail, false));
        pet = petRepository.save(pet);

        IssueDto dto = new IssueDto();
        dto.setId(issue.getId());
        dto.setDoctor(issue.getDoctor().getId());
        dto.setDescription("new issue description");
        dto.setPet(pet.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/doctor/issue/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(issue.getDoctor().getUser().getEmail()).roles("DOCTOR"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.doctor.id").value(dto.getDoctor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.pet.id").value(dto.getPet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(dto.getDescription()));
    }

    @Test
    public void deleteIssue_shouldSuccess() throws Exception {
        String userEmail = "user@mail.com";
        Issue issue = testUtils.initTestIssue("doctor@mail.com", userEmail );

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/doctor/issue/delete/" + issue.getId())
                        .contentType(APPLICATION_JSON)
                        .with(user(issue.getDoctor().getUser().getEmail()).roles("DOCTOR"))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(issue.getId()));
    }

    @Test
    public void createIssue_shouldFail() throws Exception {
        Doctor doctor = testUtils.initTestDoctor("doctor@mail.com");
        Pet pet = testUtils.initTestPet("email@mail.com");

        IssueDto dto = new IssueDto(); // create issue without doctor
        dto.setDescription("a"); // invalid description
        dto.setPet(pet.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/doctor/issue/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(doctor.getUser().getEmail()).roles("DOCTOR"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", hasItem(oneOf("description", "doctor"))));
    }

    @Test
    public void createIssueWithRoleUser_shouldFail() throws Exception {
        Doctor doctor = testUtils.initTestDoctor("doctor@mail.com");
        Pet pet = testUtils.initTestPet("email@mail.com");

        IssueDto dto = new IssueDto(); // create issue without doctor
        dto.setDescription("a"); // invalid description
        dto.setPet(pet.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/doctor/issue/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(doctor.getUser().getEmail()).roles("USER"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteIssueWithRoleUser_shouldFail() throws Exception {
        String userEmail = "user@mail.com";
        Issue issue = testUtils.initTestIssue("doctor@mail.com", userEmail );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/doctor/issue/delete")
                        .contentType(APPLICATION_JSON)
                        .with(user(issue.getDoctor().getUser().getEmail()).roles("USER"))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
