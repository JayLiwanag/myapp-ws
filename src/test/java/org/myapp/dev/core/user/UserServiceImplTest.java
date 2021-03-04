package org.myapp.dev.core.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.myapp.dev.api.exception.UserServiceException;
import org.myapp.dev.core.user.dao.UserRepository;
import org.myapp.dev.core.user.model.dto.UserDto;
import org.myapp.dev.core.user.model.UserEntity;
import org.myapp.dev.shared.Utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    UserEntity userEntity;
    UserDto userDto;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserId("Id123");
        userEntity.setUsername("JNSmith");
        userEntity.setFirstName("Joe");
        userEntity.setLastName("Smith");
        userEntity.setEmail("JoeSmith@myapp.com");
        userEntity.setEmailVerificationStatus(true);
        userEntity.setEncryptedPassword("password123");

        userDto = new UserDto();
        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword(userEntity.getEncryptedPassword());
    }

    //==========================================//
    /*
     * Test For createUser method
     * */
    @Test
    void createUser() {
        when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        when(utils.generateUserId())
                .thenReturn(userEntity.getUserId());

        when(bCryptPasswordEncoder.encode( ArgumentMatchers.anyString() ))
                .thenReturn(userEntity.getEncryptedPassword());

        when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                .thenReturn(userEntity);


        Optional<UserDto> userDetails = userService.createUser(userDto);

        assertFalse(userDetails.isEmpty());
        UserDto user = userDetails.get();

        assertEquals(userEntity.getUsername(), user.getUsername());


    }

    @Test
    void createUser_AlreadyExistException() {
        when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.ofNullable(userEntity));

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userDto);
        });
    }

    //==========================================//
    /*
    * Test For getUserDetails method
    * */
    @Test
    void getUserDetails() {
        when(userRepository.findByUserId( ArgumentMatchers.anyString() ))
                .thenReturn(Optional.ofNullable(userEntity));

        Optional<UserDto> userDetails = userService.getUserDetails("test@test.com");

        assertFalse(userDetails.isEmpty());
        UserDto user = userDetails.get();

        assertEquals(userEntity.getUsername(), user.getUsername());
    }

    @Test
    void getUserDetails_NotFoundException() {
        when(userRepository.findByUserId(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> {
            userService.getUserDetails("test@test.com");
        });
    }
}