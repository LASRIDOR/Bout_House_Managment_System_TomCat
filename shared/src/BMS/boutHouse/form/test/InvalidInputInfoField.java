package BMS.boutHouse.form.test;

public class InvalidInputInfoField extends Exception {
    public InvalidInputInfoField(String stringValue){
        super(String.format("%s doesn't meet the standard of his type regex", stringValue));
    }
}
