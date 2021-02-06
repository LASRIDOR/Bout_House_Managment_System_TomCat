package BMS.boutHouse.form.test;

import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.test.exceptions.ConvertingException;

public class RoleField extends Field{
    public RoleField(Informable type, String value) throws ConvertingException, InvalidInputInfoField {
        super(type, value);
    }

    @Override
    public Boolean getValue() {
        return (Boolean) this.value;
    }

    @Override
    protected Object makeValueAccordingToType(String stringValue) throws ConvertingException {
        Boolean isManager = false;

        if(stringValue.equals("yes")){
            isManager = true;
        }
        else if (stringValue.equals("no")){
            isManager = false;
        }
        else {
            throw new ConvertingException("String", "Is Manager");
        }

        return isManager;
    }
}