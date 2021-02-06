package BMS.bouthouse.users;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.Form;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.MemberFieldType;

import java.time.LocalDateTime;
import java.util.Objects;

// paddler
// add implements compareable maybe
public class MemberForm extends Form implements BoutHouseInstance {
    public MemberForm() {
        try {
            this.setField(new InfoField<>(MemberFieldType.DATE_OF_JOIN, LocalDateTime.now()));
            this.setField(new InfoField(MemberFieldType.IS_MANAGER, false));
        } catch (WrongTypeException ignored) {
        }
    }

    public boolean isOwnerOfPrivateBoat(){
        boolean isOwner;

        try {
            this.getField(MemberFieldType.PRIVATE_BOAT_HASH);
            isOwner = true;
        } catch (FieldContainException | WrongTypeException e) {
            isOwner = false;
        }

        return isOwner;
    }

    public boolean isManager(){
        return (boolean) getAllFields().get(MemberFieldType.IS_MANAGER).getValue();
    }

    public void setField(InfoField newForm) throws WrongTypeException {
        if (newForm.getType() instanceof MemberFieldType){
            super.setField(newForm);
        }
        else{
            throw new WrongTypeException(newForm.getType().getNameOfField(), "Member Form");
        }
    }

    public InfoField getField(Informable fieldType) throws WrongTypeException, FieldContainException {
        InfoField fieldToReturn;

        if (fieldType instanceof MemberFieldType){
            fieldToReturn = super.getField(fieldType);
        }
        else{
            throw new WrongTypeException(fieldType.getNameOfField(), "Member Form");
        }

        return fieldToReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberForm member = (MemberForm) o;
        return Objects.equals(this.getAllFields(), member.getAllFields());
    }

    @Override
    public int hashCode() {
        return this.getAllFields().get(MemberFieldType.EMAIL).hashCode();
    }

    @Override
    public String toString() {
        StringBuilder allFields = new StringBuilder();

        allFields.append(System.lineSeparator());
        this.getAllFields().forEach((type, value) -> { allFields.append(value.toString());});
        allFields.delete(allFields.lastIndexOf(System.lineSeparator()), allFields.length());

        return allFields.toString();
    }
}

