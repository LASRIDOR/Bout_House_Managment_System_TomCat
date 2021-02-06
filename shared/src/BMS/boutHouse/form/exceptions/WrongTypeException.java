package BMS.boutHouse.form.exceptions;

public class WrongTypeException extends Exception {
    public WrongTypeException(String fieldType, String objectName) {
        super(String.format("Field Type (%s) is not Compatible with class (%s)", fieldType, objectName));
    }

    public WrongTypeException(String theMessage) {
        super(theMessage);
    }
}
