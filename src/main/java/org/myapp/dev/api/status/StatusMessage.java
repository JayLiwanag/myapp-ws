package org.myapp.dev.api.status;

public enum StatusMessage {

    // Message
    //User
    MSG_USER_DELETE_OPERATION("User asccount deletion"),


    // Status
    SUCCESS("Success request."),
    FAILED("Failed request"),
    UNAUTHORIZED("Unauthorized request.");

    private String statusMessage;

    StatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
