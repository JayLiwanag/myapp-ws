package org.myapp.dev.core.user.model.dto;


import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private String password;
    private String encryptedPassword;
    private Boolean active = true;

}
