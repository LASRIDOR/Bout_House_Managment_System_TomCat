package BMS.boutHouse.form.field.type;

public enum XmlFieldType implements Informable {
    XML_PATH("XML Path", "^(.+\\/)+(.+\\.xml\\b)");

    private final String nameOFField;
    private final String regexPattern;

    XmlFieldType(String nameOfField, String regexPattern) {
        this.nameOFField = nameOfField;
        this.regexPattern = regexPattern;
    }

    @Override
    public String getNameOfField() {
        return nameOFField;
    }

    @Override
    public String getRegexPattern() {
        return regexPattern;
    }

    @Override
    public String toString() {
        return "XmlFieldType{" +
                "nameOFField='" + nameOFField + '\'' +
                '}';
    }
}
