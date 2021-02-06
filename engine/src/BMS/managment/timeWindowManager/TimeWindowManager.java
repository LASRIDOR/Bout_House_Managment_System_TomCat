package BMS.managment.timeWindowManager;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;
import BMS.managment.userManager.MemberManager;
import BMS.managment.utils.SystemManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.InvalidInstanceField;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeWindowManager extends SystemManager {

    public TimeWindowManager() {}

    @Override
    public void createNewInstance(InfoField<String> emailOfCreator, ArrayList<InfoField> args) throws OnlyManagerAccessException, ExistingException, InvalidInstanceField, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfCreator);
        TimeWindow newTimeWindow = createTimeWindow(args);
        checkIfTimeWindowValid(newTimeWindow);
        checkInstanceExistence(newTimeWindow.getAllFields().get(TimeWindowInfoFieldType.TIME_WINDOW_NAME), true);
        instances.put(newTimeWindow.getAllFields().get(TimeWindowInfoFieldType.TIME_WINDOW_NAME), newTimeWindow);
    }

    private TimeWindow createTimeWindow(ArrayList<InfoField> args) {
        TimeWindow newTimeWindow = new TimeWindow();
        Map<Informable, InfoField> fields = createMapOfArgs(args);

        fields.forEach((informable, infoField) -> {
            try {
                newTimeWindow.setField(infoField);
            } catch (WrongTypeException ignored) {
            }
        } );

        return newTimeWindow;
    }

    private Map<Informable, InfoField> createMapOfArgs(ArrayList<InfoField> args) {
        Map<Informable, InfoField> fields = new HashMap<>();

        for (InfoField arg : args) {
            fields.put(arg.getType(), arg);
        }

        return fields;
    }

    private void checkIfTimeWindowValid(TimeWindow newTimeWindow) throws InvalidInstanceField {
        if (!TimeWindowValidator.isTimeWindowValid(newTimeWindow)) {
            throw new InvalidInstanceField();
        }
    }

    @Override
    public void deleteInstance(InfoField<String> emailOfDeleter, InfoField<String> activityName) throws ExistingException, OnlyManagerAccessException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfDeleter);
        checkInstanceExistence(activityName, false);
        this.instances.remove(activityName);
    }

    @Override
    public void updateInstance(InfoField<String> emailOfUpdater, InfoField<String> activityName, InfoField newInfoField) throws ExistingException, OnlyManagerAccessException, WrongTypeException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfUpdater);
        checkInstanceExistence(activityName, false);
        setFieldToTimeWindow(activityName, newInfoField);
    }

    @Override
    public Map getAllInstances() {
        return instances;
    }

    @Override
    public void loadXml(ArrayList<ArrayList<InfoField>> allInstancesArgs) {
        for (ArrayList<InfoField> instanceArgs : allInstancesArgs) {
            try {
                TimeWindow timeWindow = createTimeWindow(instanceArgs);
                instances.put(timeWindow.getField(TimeWindowInfoFieldType.TIME_WINDOW_NAME), timeWindow);
            } catch (WrongTypeException | FieldContainException ignored) {

            }
        }
    }

    private void setFieldToTimeWindow(InfoField<String> activityName, InfoField infoField) throws WrongTypeException {
        TimeWindow timeWindowToSet = (TimeWindow) instances.remove(activityName);

        InfoField oldField = timeWindowToSet.getAllFields().get(infoField.getType());
        timeWindowToSet.setField(infoField);

        if (TimeWindowValidator.isTimeWindowFormValid(timeWindowToSet)) {
            instances.put(timeWindowToSet.getAllFields().get(TimeWindowInfoFieldType.TIME_WINDOW_NAME), timeWindowToSet);
        }
        else {
            timeWindowToSet.setField(oldField);
            instances.put(timeWindowToSet.getAllFields().get(TimeWindowInfoFieldType.TIME_WINDOW_NAME), timeWindowToSet);
            throw new WrongTypeException("The field you've tried to type is invalid");
        }
    }

    public boolean isTimeWindowListNotEmpty(){
        return !instances.isEmpty();
    }
}
