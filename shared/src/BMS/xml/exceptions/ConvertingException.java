package BMS.xml.exceptions;

public class ConvertingException extends Exception {
    public ConvertingException(String from, String to) {
        super(String.format("Cant convert from %s to %s", from, to));
    }
}
