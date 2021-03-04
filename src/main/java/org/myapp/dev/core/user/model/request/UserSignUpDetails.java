package org.myapp.dev.core.user.model.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.myapp.dev.shared.validation.PasswordMatches;
import org.myapp.dev.shared.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@PasswordMatches
public class UserSignUpDetails implements Serializable {

    @NotNull
    @Size(min = 6, max = 50, message = "Username could not be empty with minimum size of 6 character")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username should be alphanumeric.")
    private String username;

    @NotNull
    @Size(min = 2, max = 50, message = "First name could not be empty.")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50, message = "Last name could not be empty.")
    private String lastName;

    @NotNull
    @Email
    @Size(min = 3, max = 150)
    private String email;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min=6)
    private String passwordConfirmation;

}
