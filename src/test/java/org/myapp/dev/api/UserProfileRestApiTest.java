package org.myapp.dev.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.myapp.dev.core.user.UserService;
import org.myapp.dev.core.user.model.dto.UserDto;
import org.myapp.dev.core.user.model.response.UserDetailsRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileRestApiTest {

    UserDto userDto;

    @InjectMocks
    UserProfileRestApi userProfileRestApi;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUserId("Id123");
        userDto.setUsername("JNSmith");
        userDto.setFirstName("Joe");
        userDto.setLastName("Smith");
        userDto.setEmail("JoeSmith@myapp.com");
        userDto.setEmailVerificationStatus(true);
        userDto.setPassword("password123");
        userDto.setEncryptedPassword("encryptedPassword123");
        userDto.setActive(true);
    }

    @Test
    void getUserProfile() {
        when(userService.getUserDetails(ArgumentMatchers.anyString()))
                .thenReturn(Optional.ofNullable(userDto));

        ResponseEntity<UserDetailsRes> user = userProfileRestApi.getUserProfile("userId123");

        verify(userService, times(1)).getUserDetails( ArgumentMatchers.anyString() );
        assertNotNull(user.getBody());
        assertEquals(userDto.getUserId(), user.getBody().getUserId());
        assertEquals(userDto.getFirstName(), user.getBody().getFirstName());
        assertEquals(userDto.getLastName(), user.getBody().getLastName());
        assertEquals(userDto.getEmail(), user.getBody().getEmail());
    }

    @Test
    void getUserProfile_NotFound() {
        when(userService.getUserDetails(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        ResponseEntity<UserDetailsRes> user = userProfileRestApi.getUserProfile("userId123");

        assertEquals(HttpStatus.NOT_FOUND, user.getStatusCode());
        assertNull(user.getBody());
    }
}