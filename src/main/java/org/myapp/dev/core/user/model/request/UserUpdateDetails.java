package org.myapp.dev.core.user.model.request;


import lombok.Data;
import org.myapp.dev.shared.validation.PasswordMatches;
import org.myapp.dev.shared.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@PasswordMatches
public class UserUpdateDetails {

    @NotEmpty
    @Size(min = 6, max = 50, message = "Username could not be empty with minimum size of 6 character")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username should be alphanumeric.")
    private String username;

    @NotEmpty
    @Size(min = 2, max = 50, message = "First name could not be empty.")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 50, message = "Last name could not be empty.")
    private String lastName;

    @NotEmpty
    @Email
    @Size(min = 3, max = 150)
    private String email;

    @ValidPassword
    private String password;

    @NotEmpty
    @Size(min=6)
    private String passwordConfirmation;
}
