package BMS.boutHouse.form.field.type;

public enum BoatInfoFieldType implements Informable {
    SERIAL_NUMBER("Serial Number", "^[0-9]*$"),
    BOAT_NAME("Boat Name", "^[A-Za-z0-9- ]*$"),
    BOAT_TYPE("Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal)\ni.e. 2- Wide Coastal", "^(?:1X|2\\-|2X\\+|2\\+|2X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+)\\s*(?:Narrow|Wide)\\s*(?:FlatWater|Coastal)$"),
    BOAT_DISABLED("Is boat disabled? (Yes/No)", "^(?:Yes|No|yes|no|NO|YES)$"),
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