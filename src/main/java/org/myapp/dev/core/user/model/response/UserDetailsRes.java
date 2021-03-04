package org.myapp.dev.core.user.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetailsRes implements Serializable {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

}
