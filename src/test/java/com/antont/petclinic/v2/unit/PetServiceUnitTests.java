package com.antont.petclinic.v2.unit;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.PetType;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.PetService;
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

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class PetServiceUnitTests {

    @Mock
    PetRepository petRepository;

    @Mock
    PetTypeRepository petTypeRepository;

    @InjectMocks
    PetService petService;

    @Mock
    UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updatePetWithValidData_shouldSuccess(){
        User user = new User();
        user.setEmail("test@email.com");
        Mockito.doReturn(user).when(userService).getLoggedInUser();

        PetType type = new PetType();
        type.setId(BigInteger.valueOf(1));
        Mockito.doReturn(Optional.of(type)).when(petTypeRepository).findById(type.getId());

        Pet pet = new Pet();
        pet.setId(BigInteger.valueOf(1));
        pet.setName("name");
        pet.setPetType(type);
        Mockito.doReturn(Optional.of(pet)).when(petRepository).findByIdAndOwner(pet.getId(), user);

        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName("new name");
        dto.setPetType(type.getId());
        Pet pet1 = petService.update(dto);

        assertEquals(pet1.getName(), dto.getName());
    }

    @Test
    public void updatePet_shouldSuccess(){
        User user = new User();
        user.setEmail("test@email.com");
        Mockito.doReturn(user).when(userService).getLoggedInUser();

        Mockito.doReturn(Optional.empty()).when(petRepository).findByIdAndOwner(BigInteger.valueOf(1), user);

        assertThrows(RuntimeException.class, () -> petService.update(new PetDto()));
    }
}
