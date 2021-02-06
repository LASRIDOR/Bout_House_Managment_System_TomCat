package BMS.managment.utils.exceptions;

public class NeedToLoginException extends Exception {
    public NeedToLoginException() {
        super(String.format("you need to log in"));
    }
}
