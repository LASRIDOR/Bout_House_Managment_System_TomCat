package BMS.boutHouse.activities;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.Form;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TimeWindow extends Form implements BoutHouseInstance, Serializable {
    public TimeWindow() {}

    @Override
    public String toString() {
        // 12:00-13:30, rowing with sharks, BOAT_TYPE (string)
        String theString = null;

        String activityName = null;
        try {
            activityName = (String) super.getField(TimeWindowInfoFieldType.TIME_WINDOW_NAME).getValue();
            LocalTime activityStartTime = (LocalTime) super.getField(TimeWindowInfoFieldType.ACTIVITY_START_TIME).getValue();
            LocalTime activityEndTime = (LocalTime) super.getField(TimeWindowInfoFieldType.ACTIVITY_END_TIME).getValue();


        theString = formatLocalTime(activityStartTime) +
                    "-" + formatLocalTime(activityEndTime) + " - " + activityName;
        } catch (FieldContainException | WrongTypeException ignored) {
        }

        try {
            String boatType = (String) super.getField(TimeWindowInfoFieldType.BOAT_TYPE).getValue();
            if (boatType.trim().length() > 0) {
                theString += " - " + boatType;
            }
        } catch (FieldContainException | WrongTypeException ignored) {
        }


        return theString;
    }

    // Formats the time into a "HH:mm" and returns a string of that
    private String formatLocalTime(LocalTime theTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return timeFormatter.format(theTime);
    }

    @Override
    public void setField(InfoField newInfoField) throws WrongTypeException {
        if (newInfoField.getType() instanceof TimeWindowInfoFieldType) {
            super.setField(newInfoField);
        }
        else {
            throw new WrongTypeException(newInfoField.getType().getNameOfField(), "Time Window Form");
        }
    }

    @Override
    public InfoField getField(Informable fieldType) throws WrongTypeException, FieldContainException {
        if (fieldType instanceof TimeWindowInfoFieldType) {
                return super.getField(fieldType);
            }
        else {
            throw new WrongTypeException(fieldType.getNameOfField(), "Time Window Form");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeWindow that = (TimeWindow) o;
        return Objects.equals(this.getAllFields(), that.getAllFields());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getAllFields().get(TimeWindowInfoFieldType.TIME_WINDOW_NAME));
    }
}
