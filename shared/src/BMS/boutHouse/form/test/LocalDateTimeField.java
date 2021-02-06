package BMS.boutHouse.form.test;

import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.test.exceptions.ConvertingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeField extends Field{

    public LocalDateTimeField(Informable type, String stringValue) throws ConvertingException, InvalidInputInfoField {
        super(type, stringValue);
    }

    @Override
    public LocalDateTime getValue() {
        return (LocalDateTime) value;
    }

    @Override
    protected LocalDateTime makeValueAccordingToType(String stringValue) {
        String[] localDateTimeParamInput = stringValue.split("T");
        String[] localDateParamInput = localDateTimeParamInput[0].split("-");
        String[] localTimeParamInput = localDateTimeParamInput[1].split(":");

        LocalDate valueLocalDate = LocalDate.of(Integer.parseInt(localDateParamInput[0]), Integer.parseInt(localDateParamInput[1]), Integer.parseInt(localDateParamInput[2]));
        LocalTime valueLocalTime = LocalTime.of(Integer.parseInt(localTimeParamInput[0]), Integer.parseInt(localTimeParamInput[1]), Integer.parseInt(localTimeParamInput[2]));
        return LocalDateTime.of(valueLocalDate, valueLocalTime);
    }
}
