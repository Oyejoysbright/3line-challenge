package org.jpc.tool.enums;

public enum ResponseMessage {
    
    CREATED("Successfully created"),
    ERROR("Internal server error"),
    ALREADY_EXIST("Already exist"),
    ACCESS_DENIED("Access denied"),
    PIN_CREATED("Pin created successfully"),
    WRONG_PIN("Invalid pin"),
    WRONG_PASSWORD("Invalid Password"),
    PIN_ALREADY_CREATED("Pin was previously created"),
    PIN_CHANGED("Pin changed successfully"),
    PASSWORD_RESET("Your Password has been updated"),
    PASSWORD_CHANGED("Password changed successfully"),
    LOGIN_SUCCEED("Login successful"),
    INVALID_CREDENTIALS("Invalid credential"),
    INVALID_TOKEN("Invalid token"),
    EXPIRED_TOKEN("Token expired"),
    USERNAME_NOT_FOUND("Username not found"),
    BAD_DATA("Incompatible data sent. Check your payload, params or path variables"),
    TRANSACTION_COMPLETED_INITIALLY("Transaction completed initially"),
    TRANSACTION_COMPLETED("Transaction completed"),
    INVALID_REFERENCE("Invalid Reference"),
    PAYMENT_NOT_ACCEPTABLE("Integrity of your payment cannot be proved"),
    UNIMPLEMENTED_FEATURE("Feature not implemented"),
    INSUFFICIENT_FUND("Insufficient Fund"),
    DATA_FETCHED("Data Fetched"),
    INVALID_SECRET_KEY("Invalid secret key"),
    OK("Successful");

    public final String text;

    private ResponseMessage(String text) {
        this.text = text;
    }
}
