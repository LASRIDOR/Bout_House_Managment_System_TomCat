package BMS.boutHouse.form;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// Todo need to return being Composition
abstract public class Form implements Fieldable, Serializable {
    private final Map<Informable, InfoField> fields = new HashMap<>();

    public void setField(InfoField newInfoField) throws WrongTypeException {
        if(newInfoField.getType() != null && newInfoField.getValue() != null){
            this.fields.put(newInfoField.getType(), newInfoField);
        }
        else{
            throw new WrongTypeException(newInfoField.getType().getNameOfField(), "Form");
        }
    }

    public InfoField getField(Informable fieldType) throws FieldContainException, WrongTypeException {
        if(fieldType != null && this.isTypeExist(fieldType)) {
            return this.fields.get(fieldType);
        }
        else{
            throw new FieldContainException(fieldType);
        }
    }

    public void deleteField(Informable fieldType) throws FieldContainException {
        if(fieldType != null && this.isTypeExist(fieldType)) {
            this.fields.remove(fieldType);
        }
        else{
            throw new FieldContainException(fieldType);
        }
    }

    public boolean isTypeExist(Informable fieldType) {
        return fields.containsKey(fieldType);
    }

    public Map<Informable, InfoField> getAllFields() {
        return Collections.unmodifiableMap(this.fields);
    }

    @Override
    abstract public boolean equals(Object o);

    @Override
    abstract public int hashCode();

    @Override
    abstract public String toString();
}
