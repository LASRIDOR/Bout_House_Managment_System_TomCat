package BMS.managment.boatManager;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.BoatInfoFieldType;
import BMS.boutHouse.form.field.type.Informable;
import BMS.bouthouse.storage.vessel.Boat;
import BMS.managment.userManager.MemberManager;
import BMS.managment.utils.SystemManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.InvalidInstanceField;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoatManager extends SystemManager {
    public BoatManager() {}

    @Override
    public void createNewInstance(InfoField<String> emailOfCreator, ArrayList<InfoField> args) throws OnlyManagerAccessException, WrongTypeException, InvalidInstanceField, ExistingException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfCreator);
        Boat newBoat = createBoat(args);
        checkIfBoatValid(newBoat);
        InfoField<String> serialNumber = newBoat.getAllFields().get(BoatInfoFieldType.SERIAL_NUMBER);

        checkBoatExistence(serialNumber, true);
        // NOT CHECKING IF A BOAT EXISTS BECAUSE THE C'TOR OF BOAT GENERATES A NEW BOAT NUMBER
        // THEREFORE, WHEN CREATING A NEW BOAT, THERE CANNOT BE ANY EXISTING BOAT AT THE SYSTEM WITH THE SAME
        // SERIAL NUMBER
        instances.put(serialNumber, newBoat);
    }

    private Boat createBoat(ArrayList<InfoField> args) throws WrongTypeException {
        Boat newBoat = new Boat();
        Map<Informable, InfoField> fields = createMapOfArgs(args);

        for (InfoField arg : fields.values()) {
            newBoat.setField(arg);
        }

        return newBoat;
    }

    @Override
    public void loadXml(ArrayList<ArrayList<InfoField>> allInstancesArgs) {
        for (ArrayList<InfoField> instanceArgs : allInstancesArgs) {
            try {
                Boat boat = createBoat(instanceArgs);
                instances.put(boat.getField(BoatInfoFieldType.SERIAL_NUMBER), boat);
            } catch (WrongTypeException | FieldContainException ignored) {

            }
        }
    }

    private Map<Informable, InfoField> createMapOfArgs(ArrayList<InfoField> args) {
        Map<Informable, InfoField> fields = new HashMap<>();

        for (InfoField arg : args) {
            fields.put(arg.getType(), arg);
        }

        return fields;
    }

    private void checkIfBoatValid(Boat newBoat) throws InvalidInstanceField {
        if (!BoatValidator.isBoatValid(newBoat)) {
            throw new InvalidInstanceField();
        }
    }

    @Override
    public void updateInstance(InfoField<String> emailOfUpdater, InfoField<String> serialNumber, InfoField newInfoField) throws ExistingException, OnlyManagerAccessException, WrongTypeException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfUpdater);
        checkBoatExistence(serialNumber, false);
        setFieldToBoat(serialNumber, newInfoField);
    }

    @Override
    public void deleteInstance(InfoField<String> emailOfDeleter, InfoField<String> serialNumber) throws ExistingException, OnlyManagerAccessException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfDeleter);
        checkBoatExistence(serialNumber, false);
        instances.remove(serialNumber);
    }

    @Override
    public Map getAllInstances() {
        return instances;
    }


    private void setFieldToBoat(InfoField<String> serialNumber, InfoField infoField) throws WrongTypeException {
        Boat boatToSet = (Boat) instances.remove(serialNumber);

        boatToSet.setField(infoField);
        instances.put(serialNumber,boatToSet);
    }

    private void checkBoatExistence(InfoField<String> serialNumber, boolean isExist) throws ExistingException {
        if (instances.containsKey(serialNumber) == isExist) {
            throw new ExistingException(serialNumber.getValue(), isExist);
        }
    }
}
