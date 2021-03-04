package org.myapp.dev.core.user.model.request;

import lombok.Data;

@Data
public class UserLoginDetails {

    private String username;
    private String password;
}
