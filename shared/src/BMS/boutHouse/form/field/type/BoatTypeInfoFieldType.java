package BMS.boutHouse.form.field.type;

public enum BoatTypeInfoFieldType implements Informable {
    CLASSIFICATION("Classification (1X, 2-, 2+, 2X, 2X+, 4+, 4-, 4X+, 4X, 8X+, 8+)", "^(?:1X|2\\-|2X\\+|2\\+|2X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+)$"),
    WIDTH("Width (Narrow/Wide)", "^(?:Narrow|Wide)$"),
    ENVIRONMENT("Rowing environment (Flat water/Coastal", "^(?:Flat water|Coastal)$)");

    private String nameOfField;
    private String regexPattern;

    BoatTypeInfoFieldType(String nameOfField, String regexPattern) {
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
