package BMS.boutHouse.form.field.type;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.server.BoutHouseDataType;

public enum BoatInfoFieldType implements Informable {
    SERIAL_NUMBER("Serial Number", "^[0-9]*$"),
    BOAT_NAME("Boat Name", "^[A-Za-z0-9- ]*$"),
    BOAT_TYPE("Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal)\ni.e. 2- Wide Coastal", "^(?:1X|2\\-|2X\\+|2\\+|2X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+)\\s*(?:Narrow|Wide)\\s*(?:FlatWater|Coastal)$"),
    BOAT_DISABLED("Is boat disabled?", "^(?:Yes|No|yes|no|NO|YES)$"),
    BOAT_PRIVATE("Is boat private?", "^(?:Yes|No|yes|no|NO|YES)$");


    private String nameOfField;
    private String regexPattern;

    BoatInfoFieldType(String nameOfField, String regexPattern) {
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

    public static BoatInfoFieldType createBoatType(String nameOfInformable) throws WrongTypeException {
        BoatInfoFieldType informableToReturn;

        switch (nameOfInformable) {
            case "Serial Number":
                informableToReturn = BoatInfoFieldType.SERIAL_NUMBER;
                break;
            case "Boat Name":
                informableToReturn = BoatInfoFieldType.BOAT_NAME;
                break;
            case "Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal)\ni.e. 2- Wide Coastal":
                informableToReturn = BoatInfoFieldType.BOAT_TYPE;
                break;
            case "Is boat disabled?":
                informableToReturn = BoatInfoFieldType.BOAT_DISABLED;
                break;
            case "Is boat private?":
                informableToReturn = BoatInfoFieldType.BOAT_PRIVATE;
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
        return "BoatInfoFieldType{" +
                "nameOfField='" + nameOfField + '\'' +
                '}';
    }
}