package BMS.managment.utils;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.managment.utils.exceptions.ExistingException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class SystemManager implements BoutHouseManageable {
    protected final Map<InfoField, BoutHouseInstance> instances = new Hashtable<>();

    public void checkInstanceExistence(InfoField<String> instanceID, boolean isExist) throws ExistingException {
        if(instances.containsKey(instanceID) == isExist) {
            throw new ExistingException(instanceID.getValue(), isExist);
        }
    }

    public BoutHouseInstance getInstanceWithId(InfoField instanceID) throws ExistingException {
        checkInstanceExistence(instanceID, false);
        return instances.get(instanceID);
    }

    @Override
    public String toString() {
        List<BoutHouseInstance> list = new ArrayList<>(instances.values());
        StringBuilder theString = new StringBuilder();

        for (BoutHouseInstance boutHouseInstance : list) {
            theString.append("\n").append(boutHouseInstance.toString());
        }

        return theString.toString();
        //return instances.toString();
    }
}
