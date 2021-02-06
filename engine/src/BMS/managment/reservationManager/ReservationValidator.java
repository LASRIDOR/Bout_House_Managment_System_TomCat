package BMS.managment.reservationManager;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.BoatType;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.ReservationInfoFieldType;
import BMS.bouthouse.activities.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class ReservationValidator {
    public static boolean isReservationValid(Reservation reservationToCheck) {
        boolean isReservationValid = false;

        if (isReservationFormValid(reservationToCheck)) {
            isReservationValid = true;
        }

        return isReservationValid;
    }

    public static boolean isReservationFormValid(Reservation reservationToCheck) {
        Map<Informable, InfoField> reservationFormFields = reservationToCheck.getAllFields();

        if (!areMustFieldsAllContained(reservationFormFields)) {
            return false;
        }

        for (InfoField infoField : reservationFormFields.values()) {
            if (!isReservationFormFieldValid(infoField)) {
                return false;
            }
        }

        return true;
    }

    public static boolean areMustFieldsAllContained(Map<Informable, InfoField> reservationFormFields) {
        return (reservationFormFields.containsKey(ReservationInfoFieldType.RESERVATION_NUMBER) &&
                reservationFormFields.containsKey(ReservationInfoFieldType.NAME_ROWER) &&
                reservationFormFields.containsKey(ReservationInfoFieldType.DATE_OF_PRACTICE) &&
                reservationFormFields.containsKey(ReservationInfoFieldType.TIME_WINDOW) &&
                reservationFormFields.containsKey(ReservationInfoFieldType.BOAT_TYPE) &&
                reservationFormFields.containsKey(ReservationInfoFieldType.NAME_OF_RESERVATION_MAKER));
    }

    public static boolean isReservationFormFieldValid(InfoField infoField) {
        boolean isCompatible = false;

        Informable infoFieldType = infoField.getType();
        Object infoFieldValue = infoField.getValue();

        if (infoFieldType == ReservationInfoFieldType.RESERVATION_NUMBER) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.NAME_ROWER) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.DATE_OF_PRACTICE) {
            if (infoFieldValue instanceof LocalDate) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.TIME_WINDOW) {
            if (infoFieldValue instanceof TimeWindow) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.BOAT_TYPE) {
            if (infoFieldValue instanceof BoatType) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.NAME_OF_RESERVATION_MAKER) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.NAMES_OF_ROWERS) {
            if (infoFieldValue instanceof String[]) {
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.DATE_OF_RESERVATION){
            if (infoFieldValue instanceof LocalDateTime){
                isCompatible = true;
            }
        }
        else if (infoFieldType == ReservationInfoFieldType.NAME_COXSWAIN) {
            if (infoFieldValue instanceof String) {
                isCompatible = true;
            }
        }

        return isCompatible;
    }
}
