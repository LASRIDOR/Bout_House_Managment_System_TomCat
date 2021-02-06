package BMS.managment.userManager;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.bouthouse.users.MemberForm;
import BMS.managment.utils.SystemManager;
import BMS.managment.utils.exceptions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemberManager extends SystemManager {
    private static final Map<InfoField<String>, MemberForm> loggedMembers = new HashMap<>();

    public MemberManager() {
        MemberForm firstManager = null;

        try {
            firstManager = new MemberForm();
            firstManager.setField(new InfoField<>(MemberFieldType.USERNAME, "admin"));
            firstManager.setField(new InfoField<>(MemberFieldType.EMAIL, "admin@gmail.com"));
            firstManager.setField(InfoFieldMaker.createInfoField("admin", MemberFieldType.PASSWORD));
            firstManager.setField(new InfoField<>(MemberFieldType.IS_MANAGER, true));
        } catch (WrongTypeException | FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            e.printStackTrace();
        }

        instances.put(new InfoField<>(MemberFieldType.EMAIL, "admin@gmail.com"), firstManager);
    }

    public Map getAllInstances() {
        return Collections.unmodifiableMap(instances);
    }

    public void login(InfoField<String> emailOfRequester, InfoField<String> emailToLogin, InfoField<String> password) throws WrongCredentialException, ExistingException, LoggedAlreadyException {
        checkInstanceExistence(emailToLogin, false);
        checkLoggedAlready(emailToLogin);
        logout(emailOfRequester);
        MemberForm member = (MemberForm) instances.get(emailToLogin);
        checkCredentialCompatibleWithMember(member, password);
        loggedMembers.put(emailToLogin, member);
    }

    private void checkLoggedAlready(InfoField<String> email) throws LoggedAlreadyException {
        if(loggedMembers.containsKey(email)){
            throw new LoggedAlreadyException(email.getValue());
        }
    }

    private void checkCredentialCompatibleWithMember(MemberForm member, InfoField<String> password) throws WrongCredentialException {
        String memberGivenPassword = password.getValue();
        String memberRealPassword = (String) member.getAllFields().get(MemberFieldType.PASSWORD).getValue();

        if(!memberRealPassword.equals(memberGivenPassword)) {
            throw new WrongCredentialException();
        }
    }

    public void logout(InfoField<String> email) {
        if (email != null) {
            loggedMembers.remove(email);
        }
    }

    @Override
    public void createNewInstance(InfoField<String> emailOfCreator, ArrayList<InfoField> args) throws OnlyManagerAccessException, WrongTypeException, ExistingException, InvalidInstanceField, NeedToLoginException {
        MemberForm newMember = createMember(args);
        checkLoggedIsManager(emailOfCreator);
        checkValidMember(newMember);
        checkInstanceExistence(newMember.getAllFields().get(MemberFieldType.EMAIL), true);
        instances.put(newMember.getAllFields().get(MemberFieldType.EMAIL), newMember);
    }

    private MemberForm createMember(ArrayList<InfoField> args) throws WrongTypeException {
        Map<Informable, InfoField> fields = createMapOfArgs(args);
        MemberForm newMember = new MemberForm();

        fields.forEach(((informable, infoField) -> {
            try {
                newMember.setField(infoField);
            } catch (WrongTypeException ignored) {
            }
        } ));

        return newMember;
    }

    private void checkValidMember(MemberForm newMember) throws InvalidInstanceField {
        if(!UserValidator.isValidMember(newMember)){
            throw new InvalidInstanceField();
        };
    }

    private Map<Informable, InfoField> createMapOfArgs(ArrayList<InfoField> args) {
        Map<Informable, InfoField> fields = new HashMap<>();

        for (InfoField arg : args) {
            fields.put(arg.getType(), arg);
        }

        return fields;
    }

    public void updateInfoFieldOfLoggedMember(InfoField<String> email, InfoField<String> newMemberInfoField) throws WrongTypeException, FieldContainException, NeedToLoginException {
        checkLogged(email);
        MemberForm handledMember = (MemberForm) instances.remove(email);
        handledMember.setField(newMemberInfoField);
        instances.put(handledMember.getField(MemberFieldType.EMAIL), handledMember);
        inCaseOfLoggedMemberUpdatedEmail(email, handledMember);
    }

    private void inCaseOfLoggedMemberUpdatedEmail(InfoField<String> email, MemberForm handledMember) {
        if(loggedMembers.containsKey(email)){
            loggedMembers.remove(email);
            loggedMembers.put(handledMember.getAllFields().get(MemberFieldType.EMAIL), handledMember);
        }
    }

    @Override
    public void updateInstance(InfoField<String> emailOfUpdater, InfoField<String> emailToUpdate, InfoField newInfoField) throws OnlyManagerAccessException, ExistingException, WrongTypeException, NeedToLoginException {
        checkLoggedIsManager(emailOfUpdater);
        checkInstanceExistence(emailToUpdate, false);
        setFieldToMember(emailToUpdate, newInfoField);
    }

    private void setFieldToMember(InfoField<String> email, InfoField infoField) throws WrongTypeException {
        MemberForm memberToSet = (MemberForm) instances.remove(email);
        memberToSet.setField(infoField);
        instances.put(memberToSet.getAllFields().get(MemberFieldType.EMAIL), memberToSet);
        inCaseOfUpdatingLoggedMember(email, memberToSet);
    }

    private void inCaseOfUpdatingLoggedMember(InfoField<String> emailToUpdate, MemberForm memberForm) {
        loggedMembers.remove(emailToUpdate);
    }

    @Override
    public void deleteInstance(InfoField<String> emailOfDeleter, InfoField<String> emailToDelete) throws OnlyManagerAccessException, ExistingException, NeedToLoginException {
        checkLoggedIsManager(emailOfDeleter);
        checkInstanceExistence(emailToDelete, false);
        this.instances.remove(emailToDelete);
        loggedMembers.remove(emailToDelete);
    }

    public void loadXml(ArrayList<ArrayList<InfoField>> allInstancesArgs){

        for (ArrayList<InfoField> instanceArgs : allInstancesArgs) {
            try {
                MemberForm newMember = createMember(instanceArgs);
                instances.put(newMember.getAllFields().get(MemberFieldType.EMAIL), newMember);
            } catch (WrongTypeException ignored) {
            }
        }
    }

    public static void checkLogged(InfoField<String> email) throws NeedToLoginException {
        if(!isLoggedIn(email)){
            throw new NeedToLoginException();
        }
    }

    public static void checkLoggedIsManager(InfoField<String> email) throws OnlyManagerAccessException, NeedToLoginException {
        if(!isManagerLogged(email)) {
            throw new OnlyManagerAccessException();
        }
    }

    public static boolean isLoggedIn(InfoField<String> email){
        boolean isLogged;

        if(loggedMembers.containsKey(email)){
            isLogged = true;
        }
        else{
            isLogged = false;
        }

        return isLogged;
    }

    public static boolean isManagerLogged(InfoField<String> email) throws NeedToLoginException {
        boolean isLoggedManager;

        if(isLoggedIn(email)) {
            isLoggedManager = loggedMembers.get(email).isManager();
        }
        else{
            isLoggedManager = false;
            throw new NeedToLoginException();
        }

        return isLoggedManager;
    }

    public static String getNameOfLoggedMember(InfoField<String> loggedMemberEmail) throws NeedToLoginException {
        checkLogged(loggedMemberEmail);
        return (String) loggedMembers.get(loggedMemberEmail).getAllFields().get(MemberFieldType.USERNAME).getValue();
    }

    public MemberForm getLoggedMemberForm(InfoField<String> email){
        return loggedMembers.get(email);
    }

    @Override
    public String toString() {
        StringBuilder allMembers = new StringBuilder();

        this.getAllInstances().values().forEach((value) -> allMembers.append(value).append(System.lineSeparator()));

        return allMembers.toString();
    }
}