package BMS.managment.utils.exceptions;

public class ExistingException extends Exception {
    public ExistingException(String instanceID, boolean isExist) {
        super(String.format("The object %s you've typed exists: %s", instanceID, isExist));
    }
}
