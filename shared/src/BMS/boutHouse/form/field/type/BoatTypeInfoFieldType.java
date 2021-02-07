package BMS.boutHouse.form.field.type;

import BMS.boutHouse.form.exceptions.WrongTypeException;

public enum BoatTypeInfoFieldType implements Informable {
    CLASSIFICATION("Classification (1X, 2-, 2+, 2X, 2X+, 4+, 4-, 4X+, 4X, 8X+, 8+)", "^(?:1X|2\\-|2X\\+|2\\+|2X|4\\+|4\\-|4X\\+|4X|8X\\+|8\\+)$"),
    WIDTH("Width (Narrow/Wide)", "^(?:Narrow|Wide)$"),
    ENVIRONMENT("Rowing environment (Flat water/Coastal)", "^(?:Flat water|Coastal)$)");

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

    public Informable createInformable(String nameOfInformable) throws WrongTypeException {
        Informable informableToReturn;

        switch (nameOfInformable) {
            case "Classification (1X, 2-, 2+, 2X, 2X+, 4+, 4-, 4X+, 4X, 8X+, 8+)":
                informableToReturn = BoatTypeInfoFieldType.CLASSIFICATION;
                break;
            case "Width (Narrow/Wide)":
                informableToReturn = BoatTypeInfoFieldType.WIDTH;
                break;
            case "Rowing environment (Flat water/Coastal)":
                informableToReturn = BoatTypeInfoFieldType.ENVIRONMENT;
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
