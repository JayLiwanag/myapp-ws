package org.myapp.dev.api.exception;

public enum ErrorMessage {

    MISSING_REQUIRED_FIELD("Missing required field, Please check documentation for required fields."),
    USERNAME_NOT_EXIST("Username not exist."),
    INVALID_PASSWORD("Invalid Password."),
    INVALID_INPUT("Invalid input."),
    RECORD_ALREADY_EXIST("Record already exist."),
    EMAIL_ALREADY_EXIST("Email address already exist."),
    EMAIL_NOT_EXIST("Email address not exist."),
    INTERNAL_SERVER_ERROR("Internal server error."),
    NO_RECORD_FOUND("No record found."),
    AUTHENTICATION_FAILED("Authentication failed."),
    COULD_NOT_UPDATE_RECORD("Could not update record."),
    COULD_NOT_DELETE_RECORD("Could not delete record."),
    COULD_NOT_CREATE_RECORD("Could not create record."),
    INVALID_TOKEN("Invalid token"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address not yet verified"),
    PASSWORD_NOT_MATCH("Password did not matched.");

    private String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
