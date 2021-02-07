package BMS.boutHouse.form.field.type;

import BMS.boutHouse.form.exceptions.WrongTypeException;

public interface Informable {
    public String getNameOfField();
    public String getRegexPattern();
}
