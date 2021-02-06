package BMS.managment.timeWindowManager;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;

import java.time.LocalTime;
import java.util.Map;

public class TimeWindowValidator {
    public static boolean isTimeWindowValid(TimeWindow timeWindowToCheck) {
        boolean isTimeWindowValid = false;

        if (isTimeWindowFormValid(timeWindowToCheck)) {
            isTimeWindowValid = true;
        }

        return isTimeWindowValid;
    }

    public static boolean isTimeWindowFormValid(TimeWindow timeWindowToCheck) {
        Map<Informable, InfoField> timeWindowFormFields = timeWindowToCheck.getAllFields();

        if (!areMustFieldsAllContained(timeWindowFormFields)) {
            return false;
        }

        if (!isActivityStartTimePriorToActivityEndTime(timeWindowFormFields)) {
            return false;
        }

        for (InfoField infoField : timeWindowFormFields.values()) {
            if (!isTimeWindowFormFieldValid(infoField)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isActivityStartTimePriorToActivityEndTime(Map<Informable, InfoField> timeWindowFormFields) {
        return (((LocalTime) timeWindowFormFields.get(TimeWindowInfoFieldType.ACTIVITY_START_TIME).getValue()).isBefore((LocalTime) timeWindowFormFields.get(TimeWindowInfoFieldType.ACTIVITY_END_TIME).getValue()));
    }

    public static boolean areMustFieldsAllContained(Map<Informable, InfoField> timeWindowFormFields) {
        return (timeWindowFormFields.containsKey(TimeWindowInfoFieldType.TIME_WINDOW_NAME) &&
                timeWindowFormFields.containsKey(TimeWindowInfoFieldType.ACTIVITY_START_TIME) &&
                timeWindowFormFields.containsKey(TimeWindowInfoFieldType.ACTIVITY_END_TIME));
    }

    public static boolean isTimeWindowFormFieldValid(InfoField infoField) {
        boolean isCompatible = false;

        Informable infoFieldType = infoField.getType();
        Object infoFieldValue = infoField.getValue();

        if (infoFieldType == TimeWindowInfoFieldType.TIME_WINDOW_NAME) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == TimeWindowInfoFieldType.ACTIVITY_START_TIME) {
            if (infoFieldValue instanceof LocalTime) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == TimeWindowInfoFieldType.ACTIVITY_END_TIME) {
            if (infoFieldValue instanceof LocalTime) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == TimeWindowInfoFieldType.BOAT_TYPE) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }

        return isCompatible;
    }
}
