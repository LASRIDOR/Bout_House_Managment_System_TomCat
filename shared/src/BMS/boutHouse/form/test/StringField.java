package BMS.boutHouse.form.test;

import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.test.exceptions.ConvertingException;

public class StringField extends Field{

    public StringField(Informable type, String stringValue) throws ConvertingException, InvalidInputInfoField {
        super(type, stringValue);
    }

    @Override
    public String getValue() {
        return (String) value;
    }

    @Override
    protected String makeValueAccordingToType(String stringValue) {
        return stringValue;
    }
}
