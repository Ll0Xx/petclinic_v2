package com.antont.petclinic.v2.integration;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.integration.base.BaseIntegrationTest;
import com.antont.petclinic.v2.integration.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OwnerIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Test
    @Transactional
    public void createNewPet_shouldSuccess() throws Exception {
        PetDto dto = new PetDto();
        dto.setName("new pet");
        dto.setPetType(testUtils.initTestPetType().getId());

        User user = testUtils.initTestUser("email@mail.com", false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/pet/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(user.getEmail()).roles("USER"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(dto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.owner.id").value(user.getId()));
    }

    @Test
    @Transactional
    public void createNewPetWithoutCsrf_shouldFail() throws Exception {
        PetDto dto = new PetDto();
        dto.setName("new pet");
        dto.setPetType(testUtils.initTestPetType().getId());

        User user = testUtils.initTestUser("email@mail.com", false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/pet/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(user.getEmail()).roles("USER"))
                        .content(TestUtils.asJsonString(dto))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void updatePet_shouldSuccess() throws Exception {
        String userMail = "email@mail.com";
        Pet pet = testUtils.initTestPet(userMail);

        PetType type = new PetType();
        type.setName("new test type");
        PetType petType = petTypeRepository.save(type);

        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName("edited pet name");
        dto.setPetType(petType.getId());

        User user = testUtils.initTestUser(userMail, false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/pet/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(user.getEmail()).roles("USER"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(dto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.petType.id").value(dto.getPetType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.owner.id").value(user.getId()));
    }

    @Test
    @Transactional
    public void deletePet_shouldSuccess() throws Exception {
        String userEmail = "email@mail.com";
        Pet pet = testUtils.initTestPet(userEmail);
        User user = testUtils.initTestUser(userEmail, false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/pet/delete/" + pet.getId())
                        .contentType(APPLICATION_JSON)
                        .with(user(user.getEmail()).roles("USER"))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(pet.getId()));
    }

    @Test
    @Transactional
    public void createNewPet_shouldFail() throws Exception {
        PetDto dto = new PetDto();
        dto.setName("a"); // invalid name
        dto.setPetType(BigInteger.valueOf(-1)); // invalid

        User user = testUtils.initTestUser("email@mail.com", false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/pet/create")
                        .contentType(APPLICATION_JSON)
                        .with(user(user.getEmail()).roles("USER"))
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", hasItem(oneOf("name", "petType"))));
    }

    @Test
    @Transactional
    public void updatePet_shouldFail() throws Exception {
        Pet pet = testUtils.initTestPet("email@mail.com");

        PetType type = new PetType();
        type.setName("new test type");
        PetType petType = petTypeRepository.save(type);

        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName("edited pet name");
        dto.setPetType(petType.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/pet/create")
                        .contentType(APPLICATION_JSON)
                        .with(user("email2@mail.com").roles("USER")) // try editing as a different user
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deletePet_shouldFail() throws Exception {
        Pet pet = testUtils.initTestPet("email@mail.com");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/pet/delete/" + pet.getId())
                        .contentType(APPLICATION_JSON)
                        .with(user("email2@mail.com").roles("USER")) // try editing as a different user
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
