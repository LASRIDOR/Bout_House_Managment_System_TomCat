package BMS.boutHouse.form.field.infoField;

public class UserInputForInfoFIeldException extends Exception {
    public UserInputForInfoFIeldException(String userInput) {
        super(String.format("%s doesn't match the standard of the infoField", userInput));
    }
}
