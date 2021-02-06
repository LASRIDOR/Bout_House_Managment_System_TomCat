package BMS.boutHouse.form.field.infoField;

import BMS.boutHouse.form.field.type.Informable;

import java.io.Serializable;
import java.util.Objects;

public class InfoField<T> implements Serializable {
    private Informable type;
    private T value;

    public InfoField(Informable type, T value){
        this.type = type;
        setValue(value);
    }

    public Informable getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        this.value = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoField<?> infoField = (InfoField<?>) o;
        return Objects.equals(type, infoField.type) && Objects.equals(value, infoField.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return type.getNameOfField() + ": " + value + '\'' +System.lineSeparator();
    }
}
