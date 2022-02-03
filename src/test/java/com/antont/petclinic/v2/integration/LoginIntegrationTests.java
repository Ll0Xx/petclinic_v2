package com.antont.petclinic.v2.integration;

import com.antont.petclinic.v2.auth.dto.CreateUserDto;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.UserRepository;
import com.antont.petclinic.v2.integration.base.BaseIntegrationTest;
import com.antont.petclinic.v2.integration.utils.TestUtils;
import com.antont.petclinic.v2.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createNewUserWithInvalidEmail_shouldFail() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("invalid email");
        dto.setPassword("Aaa12#aa"); // valid password
        dto.setConfirmPassword("Aaa12#aa"); // valid password
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/process_register")
                        .contentType(APPLICATION_JSON)
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("email"));

    }

    @Test
    void createNewUserWithoutCsrf_shouldFail() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("email@mail.com");
        dto.setPassword("Aaa12#aa"); // valid password
        dto.setConfirmPassword("Aaa12#aa"); // valid password
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/process_register")
                        .contentType(APPLICATION_JSON)
                        .content(TestUtils.asJsonString(dto))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    void createNewUserWithInvalidPassword_shouldFail() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("email@mail.com");
        dto.setPassword("pass"); // invalid password
        dto.setConfirmPassword("pass"); // invalid password
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/process_register")
                        .contentType(APPLICATION_JSON)
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field",
                        hasItem(oneOf("password", "confirmPassword"))));
    }

    @Test
    @Transactional
    void createNewUserWithValidData() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("email@mail.com");
        dto.setPassword("Aaa12#aa"); // valid password
        dto.setConfirmPassword("Aaa12#aa"); // valid password

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/process_register")
                        .contentType(APPLICATION_JSON)
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));

        Assertions.assertTrue(userRepository.findByEmail(dto.getEmail()).isPresent());
    }

    @Test
    @Transactional
    void createNewUserWithDuplicateEmail() throws Exception {
        User user = testUtils.initTestUser("email@mail.com", false);

        CreateUserDto dto = new CreateUserDto();
        dto.setEmail(user.getEmail());
        dto.setPassword("Aaa12#aa"); // valid password
        dto.setConfirmPassword("Aaa12#aa"); // valid password
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/process_register")
                        .contentType(APPLICATION_JSON)
                        .content(TestUtils.asJsonString(dto))
                        .with(csrf())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", contains("email")));
    }
}
