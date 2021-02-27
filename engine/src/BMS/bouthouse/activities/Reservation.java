package BMS.bouthouse.activities;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.Form;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.BoatType;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.ReservationInfoFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation extends Form implements BoutHouseInstance {
    private static int counter = 0;
    private boolean isApproved;

//    private String nameRower;
//    private LocalDateTime dateOfPractice;
//    private TimeWindow windowTime;
//    private BoatType typeOfBoat;
//    private LocalDateTime dateOfReservation;
//    private String nameOFReservationMaker;
//    private String[] namesOFRowers;

    public Reservation(InfoField<String> nameRower,
                       InfoField<LocalDate> dateOfPractice,
                       InfoField<TimeWindow> windowTime,
                       InfoField<BoatType> typeOfBoat,
                       InfoField<String> nameOfReservationMaker,
                       InfoField<String[]> namesOfRowers) throws WrongTypeException {
        counter++;

        this.setField(nameRower);
        this.setField(dateOfPractice);
        this.setField(windowTime);
        this.setField(typeOfBoat);
        this.setField(nameOfReservationMaker);
        this.setField(namesOfRowers);
        this.setField(new InfoField<>(ReservationInfoFieldType.DATE_OF_RESERVATION, LocalDateTime.now()));
        this.setField(new InfoField<>(ReservationInfoFieldType.RESERVATION_NUMBER, String.valueOf(counter)));
        this.isApproved = false;
    }

    public Reservation(InfoField<String> nameRower,
                       InfoField<LocalDate> dateOfPractice,
                       InfoField<TimeWindow> windowTime,
                       InfoField<BoatType> typeOfBoat,
                       InfoField<String> nameOfReservationMaker) throws WrongTypeException {
        counter++;

        this.setField(nameRower);
        this.setField(dateOfPractice);
        this.setField(windowTime);
        this.setField(typeOfBoat);
        this.setField(nameOfReservationMaker);
        this.setField(new InfoField<>(ReservationInfoFieldType.DATE_OF_RESERVATION, LocalDateTime.now()));
        this.setField(new InfoField<>(ReservationInfoFieldType.RESERVATION_NUMBER, String.valueOf(counter)));
        this.isApproved = false;
    }

    public String getAssignedBoatSerialNumber() {
        return (String) getAllFields().get(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER).getValue();
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public void setField(InfoField newInfoField) throws WrongTypeException {
        if (newInfoField.getType() instanceof ReservationInfoFieldType) {
            super.setField(newInfoField);
        }
        else {
            throw new WrongTypeException(newInfoField.getType().getNameOfField(), "Reservation Form");
        }
    }

    @Override
    public InfoField getField(Informable fieldType) throws WrongTypeException, FieldContainException {
        if (fieldType instanceof ReservationInfoFieldType) {
            return super.getField(fieldType);
        }
        else {
            throw new WrongTypeException(fieldType.getNameOfField(), "Reservation Form");
        }
    }

    @Override
    public String toString() {
        String theString = "Reservation number: " + this.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER).getValue()
                + "\nRower's name: " + this.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue()
                + "\nDate of practice: " + this.getAllFields().get(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()
                + "\nTime window: " + this.getAllFields().get(ReservationInfoFieldType.TIME_WINDOW).getValue()
                + "\nIs approved: " + isApproved + (isApproved() ? ("\nAssigned boat serial number :" + this.getAllFields().get(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER).getValue()) : (""));

        if (this.getAllFields().containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
            if (this.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN) != null) {
                theString += "\nCoxswain: " + this.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN).getValue();
            }
        }

        return theString;
    }

    @Override
    public int hashCode() {
        return this.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER).getValue().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return this.getAllFields().equals(that.getAllFields());
    }
}