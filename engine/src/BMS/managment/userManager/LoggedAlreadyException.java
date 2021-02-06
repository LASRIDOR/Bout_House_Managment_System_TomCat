package BMS.managment.userManager;

public class LoggedAlreadyException extends Throwable {
    public LoggedAlreadyException(String value) {
        super(String.format("%s is already connected", value));
    }
}
