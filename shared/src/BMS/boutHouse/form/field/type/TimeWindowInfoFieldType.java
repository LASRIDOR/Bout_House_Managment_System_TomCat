package BMS.boutHouse.form.field.type;

import BMS.boutHouse.form.exceptions.WrongTypeException;

public enum TimeWindowInfoFieldType implements Informable {
    TIME_WINDOW_NAME("Time Window Name", "^[A-Za-z0-9 ]*[A-Za-z0-9][A-Za-z0-9 ]*$"),
    ACTIVITY_START_TIME("Activity Start Time (Pattern: HH:MM)", "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"),
    ACTIVITY_END_TIME("Activity End Time (Pattern: HH:MM)", "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"),
    BOAT_TYPE("(Optional) Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) " +
            "(Narrow/Wide) (FlatWater/Coastal)\ni.e. 2- Wide Coastal", "^(?:1X|2\\-|2X\\+|2\\+|2X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+)?\\s*(?:Narrow|Wide)?\\s*(?:FlatWater|Coastal)?$");

    private String nameOfField;
    private String regexPattern;

    TimeWindowInfoFieldType(String nameOfField, String regexPattern) {
        this.nameOfField = nameOfField;
        this.regexPattern = regexPattern;
    }

    @Override
    public String getNameOfField() {
        return this.nameOfField;
    }

    @Override
    public String getRegexPattern() {
        return this.regexPattern;
    }

    public static TimeWindowInfoFieldType createTimeWindowInfoField(String nameOfInformable) throws WrongTypeException {
        TimeWindowInfoFieldType informableToReturn;

        switch (nameOfInformable) {
            case "Time Window Name":
                informableToReturn = TimeWindowInfoFieldType.TIME_WINDOW_NAME;
                break;
            case "Activity Start Time (Pattern: HH:MM)":
                informableToReturn = TimeWindowInfoFieldType.ACTIVITY_START_TIME;
                break;
            case "Activity End Time (Pattern: HH:MM)":
                informableToReturn = TimeWindowInfoFieldType.ACTIVITY_END_TIME;
                break;
            case "(Optional) Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal)\ni.e. 2- Wide Coastal":
                informableToReturn = TimeWindowInfoFieldType.BOAT_TYPE;
                break;
            default:
                throw new WrongTypeException(nameOfInformable);
        }

        return informableToReturn;
    }

    public void setNameOfField(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public String toString() {
        return "TimeWindowInfoFieldType{" +
                "nameOfField='" + nameOfField + '\'' +
                '}';
    }
}