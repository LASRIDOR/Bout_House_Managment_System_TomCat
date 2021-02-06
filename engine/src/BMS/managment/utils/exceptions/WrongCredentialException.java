package BMS.managment.utils.exceptions;

public class WrongCredentialException extends Exception {
    public WrongCredentialException() {
        super("Wrong Email or Password");
    }
}
