package BMS.boutHouse.form.field.type;

import BMS.boutHouse.form.exceptions.WrongTypeException;

public enum ReservationInfoFieldType implements Informable {
    RESERVATION_NUMBER("Reservation Number", "^[0-9]\\d*$"),
    //^[a-zA-Z0-9,#-.\s]*$
    NAME_ROWER("Name of Rower", "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$"),
    DATE_OF_PRACTICE("Date of Practice", "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"),
    //NEW_TIME_WINDOW("New Time Window (separate with an underscore, boat type is optional, i.e. _11:00_12:00_4X+)", "^([A-Za-z0-9 ]*[A-Za-z0-9][A-Za-z0-9 ])_*([0-1]?[0-9]|2[0-3]):[0-5][0-9]_*([0-1]?[0-9]|2[0-3]):[0-5][0-9](_(?:1X|2\\-|2X\\+|2\\+|2\\X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+))?"),
    //EXISTING_TIME_WINDOW("Existing Time Window i.e. Monday rowing", "^([A-Za-z0-9 ]*[A-Za-z0-9][A-Za-z0-9 ])"),
    TIME_WINDOW("Time window", null),
    BOAT_TYPE("Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal) i.e. 2- Wide Coastal",
            "^(?:1X|2\\-|2X\\+|2\\+|2X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+)\\s*(?:Narrow|Wide)\\s*(?:FlatWater|Coastal)$"),
    NAME_OF_RESERVATION_MAKER("Name of Reservation Maker", "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$"),
    DATE_OF_RESERVATION("Date of Reservation", null),
    NAMES_OF_ROWERS("Names of Rowers", "^[a-zA-Z, ]*$"),
    NAME_COXSWAIN("Coxswain name (if coxswain has not been picked, type \"null\")", "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$"),
    NUMBER_OF_ROWERS("Number of rowers (including the lead rower)", "^[0-7]$"),
    ASSIGNED_BOAT_SERIAL_NUMBER("Assigned boat serial number", "^[0-9]*$");

    private String nameOfField;
    private String regexPattern;

    ReservationInfoFieldType(String nameOfField, String regexPattern) {
        this.nameOfField = nameOfField;
        this.regexPattern = regexPattern;
    }

    @Override
    public String getNameOfField() {
        return nameOfField;
    }

    @Override
    public String getRegexPattern() {
        return regexPattern;
    }

    public static ReservationInfoFieldType createReservationInfoField(String nameOfInformable) throws WrongTypeException {
        ReservationInfoFieldType informableToReturn;

        switch (nameOfInformable) {
            case "Reservation Number":
                informableToReturn = ReservationInfoFieldType.RESERVATION_NUMBER;
                break;
            case "Name of Rower":
                informableToReturn = ReservationInfoFieldType.NAME_ROWER;
                break;
            case "Date of Practice":
                informableToReturn = ReservationInfoFieldType.DATE_OF_PRACTICE;
                break;
            case "Time window":
                informableToReturn = ReservationInfoFieldType.TIME_WINDOW;
                break;
            case "Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal) i.e. 2- Wide Coastal":
                informableToReturn = ReservationInfoFieldType.BOAT_TYPE;
                break;
            case "Name of Reservation Maker":
                informableToReturn = ReservationInfoFieldType.NAME_OF_RESERVATION_MAKER;
                break;
            case "Date of Reservation":
                informableToReturn = ReservationInfoFieldType.DATE_OF_RESERVATION;
                break;
            case "Names of Rowers":
                informableToReturn = ReservationInfoFieldType.NAMES_OF_ROWERS;
                break;
            case "Coxswain name (if coxswain has not been picked, type \"null\")":
                informableToReturn = ReservationInfoFieldType.NAME_COXSWAIN;
                break;
            case "Number of rowers (including the lead rower)":
                informableToReturn = ReservationInfoFieldType.NUMBER_OF_ROWERS;
                break;
            case "Assigned boat serial number":
                informableToReturn = ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER;
                break;
            default:
                throw new WrongTypeException(nameOfInformable);
        }

        return informableToReturn;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public String toString() {
        return "ReservationInfoFieldType{" +
                "nameOfField='" + nameOfField + '\'' +
                '}';
    }
}