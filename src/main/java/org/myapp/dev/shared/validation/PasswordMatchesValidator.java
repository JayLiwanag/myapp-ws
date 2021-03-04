package org.myapp.dev.shared.validation;

import lombok.SneakyThrows;
import org.myapp.dev.core.user.model.request.UserSignUpDetails;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @SneakyThrows
    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        Class clazz = obj.getClass();

        Field passField = clazz.getDeclaredField("password");
        Field passConfirmField = clazz.getDeclaredField("passwordConfirmation");

        passField.setAccessible(true);
        passConfirmField.setAccessible(true);

        String pass = passField.get(obj).toString();
        String passConfirm = passConfirmField.get(obj).toString();

        return pass.equals(passConfirm);
    }
}
