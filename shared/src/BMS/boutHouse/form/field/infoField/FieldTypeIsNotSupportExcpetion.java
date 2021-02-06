package BMS.boutHouse.form.field.infoField;

public class FieldTypeIsNotSupportExcpetion extends Exception {
    public FieldTypeIsNotSupportExcpetion(String type) {
        super(String.format("%s is not supported yet in the system", type));
    }
}
