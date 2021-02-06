package BMS.managment.boatManager;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.BoatInfoFieldType;
import BMS.boutHouse.form.field.type.BoatType;
import BMS.boutHouse.form.field.type.Informable;
import BMS.bouthouse.storage.vessel.Boat;

import java.util.Map;

public class BoatValidator {
    public static boolean isBoatValid(Boat boatToCheck) {
        boolean isBoatValid = false;

        if (isBoatFormValid(boatToCheck)) {
            isBoatValid = true;
        }

        return isBoatValid;
    }

    public static boolean isBoatFormValid(Boat boatToCheck) {
        Map<Informable, InfoField> boatFormFields = boatToCheck.getAllFields();

        if (!areMustFieldsAllContained(boatFormFields)) {
            return false;
        }

        for (InfoField infoField : boatFormFields.values()) {
            if (!isBoatFormFieldValid(infoField)) {
                return false;
            }
        }

        return true;
    }

    public static boolean areMustFieldsAllContained(Map<Informable, InfoField> boatFormFields) {
        return (boatFormFields.containsKey(BoatInfoFieldType.SERIAL_NUMBER) &&
                boatFormFields.containsKey(BoatInfoFieldType.BOAT_NAME) &&
                boatFormFields.containsKey(BoatInfoFieldType.BOAT_TYPE) &&
                boatFormFields.containsKey(BoatInfoFieldType.BOAT_DISABLED) &&
                boatFormFields.containsKey(BoatInfoFieldType.BOAT_PRIVATE));
    }

    public static boolean isBoatFormFieldValid(InfoField infoField) {
        boolean isCompatible = false;

        Informable infoFieldType = infoField.getType();
        Object infoFieldValue = infoField.getValue();

        if (infoFieldType == BoatInfoFieldType.SERIAL_NUMBER) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }
       else if (infoFieldType == BoatInfoFieldType.BOAT_NAME) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == BoatInfoFieldType.BOAT_TYPE) {
            if (infoFieldValue instanceof BoatType) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == BoatInfoFieldType.BOAT_DISABLED) {
            if (infoFieldValue instanceof  Boolean) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == BoatInfoFieldType.BOAT_PRIVATE) {
            if(infoFieldValue instanceof  Boolean) {
                isCompatible = true;
            }
        }

        return isCompatible;
    }
}
