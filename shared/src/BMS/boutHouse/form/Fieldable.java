package BMS.boutHouse.form;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;

public interface Fieldable {
    void setField(InfoField<Object> infoField) throws WrongTypeException;
    InfoField<Object> getField(Informable fieldType) throws WrongTypeException, FieldContainException;
}
