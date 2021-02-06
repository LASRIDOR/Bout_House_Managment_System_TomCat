package BMS.boutHouse.form.exceptions;

import BMS.boutHouse.form.field.type.Informable;

public class FieldContainException extends Exception {
    public FieldContainException(Informable fieldType) {
        super(String.format("Form Doesnt contains %s", fieldType.toString()));
    }
}
