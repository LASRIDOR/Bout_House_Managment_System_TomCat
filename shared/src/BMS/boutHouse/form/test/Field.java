package BMS.boutHouse.form.test;


import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.test.exceptions.ConvertingException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class Field {
    Informable type;
    Object value;

    public Field(Informable type, String value) throws ConvertingException, InvalidInputInfoField {
        this.type = type;
        setValue(value);
    }

    public Informable getType() {
        return type;
    }

    public void setValue(String stringValue) throws InvalidInputInfoField, ConvertingException {
        if(isInfoFieldInputValid(type, stringValue)){
            this.value = makeValueAccordingToType(stringValue);
        }
        else{
            throw new InvalidInputInfoField(stringValue);
        }
    }

    abstract public Object getValue();

    public static boolean isInfoFieldInputValid(Informable type, String stringValue) {
        // option doing a configurable regex for manager
        // Compile the ReGex
        Pattern p = Pattern.compile(type.getRegexPattern());

        // If the field is empty
        // return false
        if (stringValue == null) {
            return false;
        }

        if (type.getRegexPattern() == null) {
            return true;
        }

        // Pattern class contains matcher() method
        // to find matching between given field
        // and regular expression.
        Matcher m = p.matcher(stringValue);

        // Return if the field
        // matched the ReGex
        return m.matches();
    }

    abstract protected Object makeValueAccordingToType(String stringValue) throws ConvertingException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(type, field.type) && Objects.equals(value, field.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "Field{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
