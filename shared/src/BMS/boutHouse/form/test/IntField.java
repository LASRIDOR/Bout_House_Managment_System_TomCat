package BMS.boutHouse.form.test;

import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.test.exceptions.ConvertingException;

public class IntField extends Field{
    public IntField(Informable type, String stringValue) throws ConvertingException, InvalidInputInfoField {
        super(type, stringValue);
    }

    @Override
    public Integer getValue() {
        return (Integer) value;
    }

    @Override
    protected Integer makeValueAccordingToType(String stringValue) throws ConvertingException {
        int convertedNumber;

        try {
            convertedNumber = Integer.parseInt(stringValue);
        }
        catch (NumberFormatException e){
            throw new ConvertingException("String", "int");
        }

        return convertedNumber;
    }
}
