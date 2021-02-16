package BMS.boutHouse.form.field.type;

import BMS.boutHouse.form.exceptions.WrongTypeException;

public enum XmlFieldType implements Informable {
    XML_PATH("XML Path", ".*");

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

    public XmlFieldType createInformable(String nameOfInformable) throws WrongTypeException {
        XmlFieldType fieldTypeToReturn;

        if ("XML Path".equals(nameOfInformable)) {
            fieldTypeToReturn = XmlFieldType.XML_PATH;
        } else {
            throw new WrongTypeException(nameOfInformable);
        }

        return fieldTypeToReturn;
    }

    @Override
    public String toString() {
        return "nameOFField='" + nameOFField + "'";
    }
}
