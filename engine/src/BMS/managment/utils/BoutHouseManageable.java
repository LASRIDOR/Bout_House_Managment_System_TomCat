package BMS.managment.utils;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.managment.reservationManager.InvalidActionException;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.InvalidInstanceField;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;

import java.util.ArrayList;
import java.util.Map;

public interface BoutHouseManageable {
    void createNewInstance(InfoField<String> emailOfCreator, ArrayList<InfoField> args) throws OnlyManagerAccessException, WrongTypeException, ExistingException, InvalidInstanceField, NeedToLoginException;
    void deleteInstance(InfoField<String> emailOdDeleter, InfoField<String> instanceID) throws OnlyManagerAccessException, ExistingException, NeedToLoginException;
    void updateInstance(InfoField<String> emailOdUpdater, InfoField<String> toUpdateId, InfoField newField) throws OnlyManagerAccessException, ExistingException, WrongTypeException, NeedToLoginException, IllegalAccessException, FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException;
    Map<InfoField, BoutHouseInstance> getAllInstances() throws OnlyManagerAccessException;
    void loadXml(ArrayList<ArrayList<InfoField>> allInstancesArgs) throws InvalidActionException;
}
