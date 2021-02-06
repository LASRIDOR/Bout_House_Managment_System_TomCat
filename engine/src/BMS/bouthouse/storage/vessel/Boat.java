package BMS.bouthouse.storage.vessel;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.Form;
import BMS.boutHouse.form.field.type.BoatInfoFieldType;
import BMS.boutHouse.form.field.type.BoatType;

import java.util.Objects;

public class Boat extends Form implements BoutHouseInstance {

    public Boat(){ }

    @Override
    public String toString() {
        String theString = null;
            String serialNumber = (String) this.getAllFields().get(BoatInfoFieldType.SERIAL_NUMBER).getValue();
            String boatName = (String) this.getAllFields().get(BoatInfoFieldType.BOAT_NAME).getValue();
            BoatType boatType = (BoatType) this.getAllFields().get(BoatInfoFieldType.BOAT_TYPE).getValue();
            boolean isBoatDisabled = (boolean) this.getAllFields().get(BoatInfoFieldType.BOAT_DISABLED).getValue();
            boolean isBoatPrivate = (boolean) this.getAllFields().get(BoatInfoFieldType.BOAT_DISABLED).getValue();
            theString =  "Serial Number: " + serialNumber + ", Boat Name: " + boatName + ", " + boatType.toString() + " " + (isBoatDisabled ? ", Boat is currently disabled" : "" + " " + (isBoatPrivate ? ", Boat is private" : ""));
        return theString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boat that = (Boat) o;
        return Objects.equals(this.getAllFields(), that.getAllFields());
    }

    @Override
    public int hashCode() {
        return this.getAllFields().get(BoatInfoFieldType.SERIAL_NUMBER).getValue().hashCode();
    }
}
