package com.antont.petclinic.v2.unit;

import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.UserRepository;
import com.antont.petclinic.v2.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findUserWithValidEmail_shouldBeFound() {
        User user = new User();
        user.setEmail("test@email.com");
        Mockito.doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmail());

        String email = "test@email.com";
        User found = userService.getByEmail(email);

        assert(found.getEmail()).equals(email);
    }

    @Test
    public void findUserWithValidEmail_shouldBeFailed() {
        String email = "test@email.com";
        Mockito.doReturn(Optional.empty()).when(userRepository).findByEmail(email);

        assertThrows(RuntimeException.class, () -> userService.getByEmail(email));
    }
}
