package BMS.managment.CEO;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.Form;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.*;
import BMS.bouthouse.activities.Reservation;
import BMS.bouthouse.storage.vessel.Boat;
import BMS.bouthouse.users.MemberForm;
import BMS.managment.boatManager.BoatManager;
import BMS.managment.reservationManager.InvalidActionException;
import BMS.managment.reservationManager.ReservationManager;
import BMS.managment.timeWindowManager.TimeWindowManager;
import BMS.managment.userManager.LoggedAlreadyException;
import BMS.managment.userManager.MemberManager;
import BMS.managment.utils.SystemManager;
import BMS.managment.utils.exceptions.*;
import BMS.managment.xmlManager.XmlManager;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;
import com.sun.javafx.collections.MappingChange;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

// todo maybe check is manager logged  here and not in member manager
public class BoutHouseManager {
    private Map<BoutHouseDataType, SystemManager> boutHouseManagers = new HashMap<>();
    private XmlManager xmlManager = new XmlManager();

    public BoutHouseManager() {
        boutHouseManagers.put(BoutHouseDataType.MEMBERS, new MemberManager());
        boutHouseManagers.put(BoutHouseDataType.TIME_WINDOW, new TimeWindowManager());
        boutHouseManagers.put(BoutHouseDataType.BOATS, new BoatManager());
        boutHouseManagers.put(BoutHouseDataType.RESERVATION, new ReservationManager());
    }

    public void loginToMemberManager(InfoField<String> emailOfRequester, InfoField<String> emailInfoField, InfoField<String> passwordInfoField) throws WrongCredentialException, ExistingException, LoggedAlreadyException {
        MemberManager memberManager = (MemberManager) boutHouseManagers.get(BoutHouseDataType.MEMBERS);
        memberManager.login(emailOfRequester, emailInfoField, passwordInfoField);
    }

    public void deleteInstance(BoutHouseDataType managerType, InfoField<String> emailOfDeleter, InfoField<String> toDelete) throws OnlyManagerAccessException, ExistingException, FileNotFoundException, ExtensionException, JAXBException, NeedToLoginException {
        SystemManager systemManager = boutHouseManagers.get(managerType);
        InfoField memberToDeleteNameInfoField = null;

        // What if a boat is all of a sudden inactive - reservation is no longer approved, boat name is deleted from reservation
        if (managerType == BoutHouseDataType.MEMBERS) {
            MemberForm memberForm = (MemberForm) systemManager.getInstanceWithId(toDelete);
            memberToDeleteNameInfoField = memberForm.getAllFields().get(MemberFieldType.USERNAME);
        }

        systemManager.deleteInstance(emailOfDeleter, toDelete);
        updateReservationsAfterDeletingInstance(emailOfDeleter, toDelete);
        updateXmlWithDelete(managerType, emailOfDeleter, toDelete);
    }

    private void updateXmlWithDelete(BoutHouseDataType managerType, InfoField<String> emailOfDeleter, InfoField deletedInstance) throws FileNotFoundException, ExtensionException, OnlyManagerAccessException, JAXBException, NeedToLoginException {
        if (managerType != BoutHouseDataType.RESERVATION) {
            xmlManager.deleteInstanceToExistingXml(managerType, emailOfDeleter, deletedInstance);
        }
    }

    private void updateReservationsAfterDeletingInstance(InfoField<String> emailOfDeleter, InfoField deletedInstanceID) {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        reservationManager.updateReservationsAfterDeletingInstance(emailOfDeleter, deletedInstanceID);
    }

    public void updateInfoFieldOfLoggedMember(InfoField<String> emailOfUpdater, InfoField<String> toUpdate) throws WrongTypeException, NeedToLoginException, FieldContainException, ExtensionException, FileNotFoundException, JAXBException, ExistingException {
        MemberManager memberManager = (MemberManager) boutHouseManagers.get(BoutHouseDataType.MEMBERS);
        memberManager.updateInfoFieldOfLoggedMember(emailOfUpdater, toUpdate);
        updateXmlWithInstanceUpdate(memberManager, BoutHouseDataType.MEMBERS, emailOfUpdater, toUpdate);
    }

    public void logoutFromMemberManager(InfoField<String> emailToLogout) {
        MemberManager memberManager = (MemberManager) boutHouseManagers.get(BoutHouseDataType.MEMBERS);
        memberManager.logout(emailToLogout);
    }

    public void createNewInstance(BoutHouseDataType type, InfoField<String> emailOfCreator, ArrayList<InfoField> args) throws WrongTypeException, OnlyManagerAccessException, ExistingException, InvalidInstanceField, FileNotFoundException, JAXBException, ExtensionException, NeedToLoginException {
        SystemManager systemManager = boutHouseManagers.get(type);
        systemManager.createNewInstance(emailOfCreator, args);
        addToDatabaseXml(type, emailOfCreator, args);
    }

    private void addToDatabaseXml(BoutHouseDataType type, InfoField<String> emailOfAdder, ArrayList<InfoField> args) throws ExistingException, FileNotFoundException, JAXBException, ExtensionException, OnlyManagerAccessException, NeedToLoginException {
        if (type != BoutHouseDataType.RESERVATION) {
            SystemManager systemManager = boutHouseManagers.get(type);
            InfoField instanceIdOfNew = findInstanceIdInArgs(args, type);
            xmlManager.addInstanceToExistingXml(type, emailOfAdder, systemManager.getInstanceWithId(instanceIdOfNew));
        }
    }

    private InfoField<String> findInstanceIdInArgs(ArrayList<InfoField> args, BoutHouseDataType type) {
        Informable typeOfId;
        InfoField<String> infoFieldInstanceId = null;

        if (type == BoutHouseDataType.MEMBERS) {
            typeOfId = MemberFieldType.EMAIL;
        } else if (type == BoutHouseDataType.BOATS) {
            typeOfId = BoatInfoFieldType.SERIAL_NUMBER;
        } else {
            typeOfId = TimeWindowInfoFieldType.TIME_WINDOW_NAME;
        }

        for (InfoField arg : args) {
            if (arg.getType() == typeOfId) {
                return arg;
            }
        }

        return infoFieldInstanceId;
    }

    public void updateSystemInstance(BoutHouseDataType managerType, InfoField<String> emailOfUpdater, InfoField<String> idOfInstance, InfoField toUpdate) throws WrongTypeException, OnlyManagerAccessException, ExistingException, ExtensionException, FileNotFoundException, JAXBException, NeedToLoginException, IllegalAccessException {
        SystemManager systemManager = boutHouseManagers.get(managerType);
        systemManager.updateInstance(emailOfUpdater, idOfInstance, toUpdate);
        updateXmlWithInstanceUpdate(systemManager, managerType, idOfInstance, toUpdate);
    }

    private void updateXmlWithInstanceUpdate(SystemManager manager, BoutHouseDataType managerType, InfoField<String> idOfInstance, InfoField toUpdate) throws ExistingException, FileNotFoundException, ExtensionException, JAXBException {
        if (managerType != BoutHouseDataType.RESERVATION) {
            InfoField<String> idAfterUpdate = getIdInCaseOfUpdatingId(idOfInstance, toUpdate);
            xmlManager.updateInstanceToExistingXml(managerType, idOfInstance, manager.getInstanceWithId(idAfterUpdate));
        }
    }

    private InfoField<String> getIdInCaseOfUpdatingId(InfoField<String> idOfInstance, InfoField toUpdate) {
        if(toUpdate.getType() == MemberFieldType.EMAIL || toUpdate.getType() == BoatInfoFieldType.SERIAL_NUMBER || toUpdate.getType() == TimeWindowInfoFieldType.TIME_WINDOW_NAME){
            return toUpdate;
        }
        else {
            return idOfInstance;
        }
    }

    public String getAllInstancesOfBoutHouseType(BoutHouseDataType typeOfManager, InfoField<String> emailOfAsker) throws OnlyManagerAccessException, NeedToLoginException {
        SystemManager systemManager = boutHouseManagers.get(typeOfManager);
        MemberManager.checkLoggedIsManager(emailOfAsker);
        return systemManager.toString();
    }

    public Map<Informable, InfoField> getAllFieldsOfInstance(BoutHouseDataType typeOfManager, InfoField<String> idOfInstance) throws ExistingException {
        SystemManager systemManager = boutHouseManagers.get(typeOfManager);
        Form instanceForm = (Form) systemManager.getInstanceWithId(idOfInstance);
        return instanceForm.getAllFields();
    }

    public boolean isInstanceExist(BoutHouseDataType typeOfManager, InfoField<String> idOfInstance){
        boolean isExist;
        SystemManager systemManager = boutHouseManagers.get(typeOfManager);

        try {
            systemManager.checkInstanceExistence(idOfInstance, false);
            isExist = true;
        } catch (ExistingException e) {
            isExist = false;
        }

        return isExist;
    }

    public String createXmlInstances(BoutHouseDataType typeOfManager, InfoField<String> emailOfCreator, ArrayList<ArrayList<InfoField>> instancesArgs) throws ExtensionException, JAXBException, FileNotFoundException, WrongTypeException, OnlyManagerAccessException, NeedToLoginException {
        StringBuilder loadingErrors = new StringBuilder("Xml Was Loaded Successfully (in case of errors list of errors will be attached)" + System.lineSeparator());

        for (ArrayList<InfoField> instanceArgs : instancesArgs) {
            try {
                this.createNewInstance(typeOfManager, emailOfCreator,instanceArgs);
            } catch (InvalidInstanceField | ExistingException e) {
                loadingErrors.append(e.getMessage()).append(System.lineSeparator());
            }
        }

        return loadingErrors.toString();
    }

    public String loadXml(BoutHouseDataType typeOfManager, InfoField<String> emailOfLoader, InfoField<String> xmlPath) throws FileNotFoundException, ExtensionException, OnlyManagerAccessException, JAXBException, FieldContainException, WrongTypeException, NeedToLoginException {
        ArrayList<ArrayList<InfoField>> instancesArgs = xmlManager.loadArgsFromXml(typeOfManager, emailOfLoader, xmlPath);
        StringBuilder loadingErrors = new StringBuilder("Xml Was Loaded Successfully (in case of errors list of errors will be attached)" + System.lineSeparator());

        for (ArrayList<InfoField> instanceArgs : instancesArgs) {
            try {
                this.createNewInstance(typeOfManager, emailOfLoader, instanceArgs);
            } catch (InvalidInstanceField | ExistingException e) {
                loadingErrors.append(e.getMessage() + System.lineSeparator());
            }
        }

        return loadingErrors.toString();
    }

    public String createAndEraseXmlInstances(BoutHouseDataType typeOfManager, InfoField<String> emailOfCreator, ArrayList<ArrayList<InfoField>> instancesArgs) throws ExtensionException, JAXBException, FileNotFoundException, WrongTypeException, OnlyManagerAccessException, NeedToLoginException {
        restartSystemManager(typeOfManager);
        return createXmlInstances(typeOfManager, emailOfCreator, instancesArgs);
    }

    public void restartSystemManager(BoutHouseDataType typeOfManager) throws FileNotFoundException, ExtensionException, JAXBException, WrongTypeException {
        xmlManager.resetDataBaseOfBoutHouseType(typeOfManager);
        boutHouseManagers.put(typeOfManager, createNewSystem(typeOfManager));
    }

    // throw new exception
    private SystemManager createNewSystem(BoutHouseDataType typeOfManager) throws WrongTypeException {
        SystemManager newSystemManager;

        if(typeOfManager == BoutHouseDataType.MEMBERS){
            newSystemManager = new MemberManager();
        }
        else if (typeOfManager == BoutHouseDataType.RESERVATION){
            newSystemManager = new ReservationManager();
        }
        else if (typeOfManager == BoutHouseDataType.BOATS){
            newSystemManager = new BoatManager();
        }
        else if (typeOfManager == BoutHouseDataType.TIME_WINDOW){
            newSystemManager = new TimeWindowManager();
        }
        else{
            throw new WrongTypeException(typeOfManager.getNameOfManager(), "System Manager");
        }

        return newSystemManager;
    }

    public void saveXml(BoutHouseDataType typeOfManager, InfoField<String> emailOfSaver) throws OnlyManagerAccessException, FileNotFoundException, ExtensionException, JAXBException, NeedToLoginException {
        SystemManager systemManager = boutHouseManagers.get(typeOfManager);

        xmlManager.saveInstancesToXml(typeOfManager, emailOfSaver, systemManager.getAllInstances().values());
    }

    public String getDatabaseLocation(BoutHouseDataType typeOfManager){
        return xmlManager.getDataBaseLocation(typeOfManager);
    }

    public boolean isTimeWindowListEmpty() {
        TimeWindowManager timeWindowManager = (TimeWindowManager) boutHouseManagers.get(BoutHouseDataType.TIME_WINDOW);
        return !timeWindowManager.isTimeWindowListNotEmpty();
    }


    public TimeWindow getTimeWindowByActivityName(InfoField activityName) throws ExistingException {
        TimeWindowManager timeWindowManager = (TimeWindowManager) boutHouseManagers.get(BoutHouseDataType.TIME_WINDOW);

        return (TimeWindow) timeWindowManager.getInstanceWithId(activityName);
    }

    public void loadDataBaseXml(BoutHouseDataType managerType) throws FileNotFoundException, ExtensionException, JAXBException, InvalidActionException {
        SystemManager systemManager = boutHouseManagers.get(managerType);
        systemManager.loadXml(xmlManager.loadDataBaseXml(managerType));
    }

    public List<Reservation> getReservationsOfUpcomingWeek() {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        return reservationManager.getReservationsOfUpcomingWeek();
    }

    public List<Reservation> getReservationsOfSpecificDate(InfoField dateToPrintReservations) throws IllegalArgumentException, IllegalStateException {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        return reservationManager.getReservationsOfSpecificDate(dateToPrintReservations);
    }

    public List<Reservation> getLoggedMemberFutureReservations(InfoField<String> loggedMemberEmail) throws IllegalStateException {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        return reservationManager.getLoggedMemberFutureReservations(loggedMemberEmail);
    }

    public List<Reservation> getLoggedMemberPastReservations(InfoField<String> loggedMemberEmail) {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        return reservationManager.getLoggedMemberPastReservations(loggedMemberEmail);
    }

    public List<Reservation> getUnapprovedReservationOfSpecificDate(InfoField dateToPrintUnapprovedReservations) throws IllegalStateException{
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        return reservationManager.getUnapprovedReservationOfSpecificDate(dateToPrintUnapprovedReservations);
    }

    public List<Reservation> getUnapprovedReservationOfUpcomingWeek() throws IllegalStateException{
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        return reservationManager.getUnapprovedReservationOfUpcomingWeek();
    }

    public List<Boat> getOptionalBoatsToAssignToReservation(InfoField reservationNumber) throws ExistingException, IllegalStateException{
        List<Boat> boats = null;

        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        Reservation reservation = (Reservation) reservationManager.getInstanceWithId(reservationNumber);

        BoatManager boatManager = (BoatManager) boutHouseManagers.get(BoutHouseDataType.BOATS);
        Map<InfoField, Boat> allBoats = boatManager.getAllInstances();

        if (allBoats.isEmpty()) {
            throw new IllegalStateException("There are no boats to be assigned to reservations");
        }
        else {
            boats = allBoats
                    .entrySet()
                    .stream()
                    .filter(boat -> !((boolean) boat.getValue().getAllFields().get(BoatInfoFieldType.BOAT_DISABLED).getValue()))
                    .filter(boat -> !((boolean) boat.getValue().getAllFields().get(BoatInfoFieldType.BOAT_PRIVATE).getValue()))
                    .filter(boat -> isBoatOptionalForReservation(boat.getValue(), reservation))
                    .map(boat -> boat.getValue())
                    .collect(Collectors.toList());
        }

        return boats;
    }

    public String getPerfectAssignmentBoat(InfoField reservationNumber) throws ExistingException, FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        // FIRST OFF, GET THE CURRENT RESERVATION'S NAME OF ROWER
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        Reservation reservation = (Reservation) reservationManager.getInstanceWithId(reservationNumber);
        InfoField<String> rowerName = reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER);

        // THEN, GET ALL THE RESERVATIONS ON THE SYSTEM
        List<Reservation> allReservationsList = new ArrayList<>(reservationManager.getAllInstances().values());
        Map<String, Long> allReservationsOfRowerWithBoatAssigned = allReservationsList
                .stream()
                .filter(randomReservation -> reservationManager.isMemberInReservation(randomReservation, rowerName))
                .filter(randomReservation -> randomReservation.getAllFields().containsKey(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER))
                .collect(Collectors.groupingBy(Reservation::getAssignedBoatSerialNumber, Collectors.counting()));

        Map<String, Long> sortedOccurrencesOfBoatsMap = new LinkedHashMap<>();
        allReservationsOfRowerWithBoatAssigned
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(x -> sortedOccurrencesOfBoatsMap.put(x.getKey(), x.getValue()));

        BoatManager boatManager = (BoatManager) boutHouseManagers.get(BoutHouseDataType.BOATS);

        for (Map.Entry<String, Long> entry : sortedOccurrencesOfBoatsMap.entrySet()) {
            Boat boat = (Boat) boatManager.getInstanceWithId(InfoFieldMaker.createInfoField(entry.getKey(), BoatInfoFieldType.SERIAL_NUMBER));

            if (isBoatOptionalForReservation(boat, reservation)) {
                return "We have a perfect assignment for this reservation! Boat " + entry.getKey() + " is a great option\n";
            }
        }

        return "There have been no boats that make a perfect assignment for this reservation\n";
    }

    private boolean isBoatOptionalForReservation(Boat boat, Reservation reservation) {
        boolean isBoatOptionalForReservation = false;

        if ((boat.getAllFields().get(BoatInfoFieldType.BOAT_TYPE).getValue()).equals(reservation.getAllFields().get(ReservationInfoFieldType.BOAT_TYPE).getValue())) {
            InfoField dateOfPractice = reservation.getAllFields().get(ReservationInfoFieldType.DATE_OF_PRACTICE);
            List<Reservation> allReservationOfSameDate = getReservationsOfSpecificDate(dateOfPractice);

            if (allReservationOfSameDate
                    .stream()
                    .filter(aReservation -> aReservation.isApproved())
                    .filter(aReservation -> aReservation.getAllFields().get(ReservationInfoFieldType.TIME_WINDOW).equals(reservation.getAllFields().get(ReservationInfoFieldType.TIME_WINDOW)))
                    .filter(aReservation -> aReservation.getAllFields().get(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER).getValue().equals(boat.getAllFields().get(BoatInfoFieldType.SERIAL_NUMBER).getValue()))
                    .filter(aReservation -> (getMaximumNumberOfRowersInABoat(boat) < getTotalNumberOfRowersInReservation(reservation) + getTotalNumberOfRowersInReservation(aReservation)))
                    .collect(Collectors.toList()).size() == 0) {
                isBoatOptionalForReservation = true;
            }
        }

        return isBoatOptionalForReservation;
    }

    private Integer getMaximumNumberOfRowersInABoat(Boat boat) {
        return Character.getNumericValue(((BoatType) boat.getAllFields().get(BoatInfoFieldType.BOAT_TYPE).getValue()).getBoatClassification().charAt(0));
    }

    private Integer getTotalNumberOfRowersInReservation(Reservation reservation) {
        int totalNumberOfRowers = 0;

        if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAMES_OF_ROWERS)) {
            totalNumberOfRowers += ((String[])reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue()).length;
        }

        if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
            if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN) != null) {
                totalNumberOfRowers++;
            }
        }

        return totalNumberOfRowers;
    }

    public void updateReservationsAfterDisablingBoat(InfoField disabledBoatSerialNumber) {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);
        reservationManager.updateReservationsAfterDisablingBoat(disabledBoatSerialNumber);
    }

    public boolean isManagerLogin(InfoField<String> email) throws NeedToLoginException {
        return MemberManager.isManagerLogged(email);
    }

    public String getSuggestionsForRowersToClient(InfoField<String> nameOfRower) {
        ReservationManager reservationManager = (ReservationManager) boutHouseManagers.get(BoutHouseDataType.RESERVATION);

        Map<String , Integer> rowersAndTimesOfRowers = new HashMap<>();
        Map<InfoField<String>, Reservation> allReservations = reservationManager.getAllInstances();

        for (Reservation reservation : allReservations.values()) {
            if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).equals(nameOfRower)) {
                String[] rowers = (String[]) reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue();

                for (String rower : rowers) {
                    if (rowersAndTimesOfRowers.containsKey(rower)) {
                        int timesOfRows = rowersAndTimesOfRowers.get(rower);
                        rowersAndTimesOfRowers.put(rower, timesOfRows++);
                    } else {
                        rowersAndTimesOfRowers.put(rower, 1);
                    }
                }
            }
        }

        rowersAndTimesOfRowers.remove(nameOfRower.getValue());
        return arrangeAllRowersInSortedWay(rowersAndTimesOfRowers);
    }

    private String arrangeAllRowersInSortedWay(Map<String, Integer> rowersAndTimesOfRowers) {
        StringBuilder suggestionOfRowers = new StringBuilder();

        if (rowersAndTimesOfRowers.size() > 0) {
            suggestionOfRowers.append(System.lineSeparator()).append("Rowers Smart Suggestion: ").append(System.lineSeparator());
            rowersAndTimesOfRowers.forEach((key, value) -> {
                suggestionOfRowers.append(String.format("You rowed with %s: %d Times", key, value)).append(System.lineSeparator());
            });
        }
        else {
            suggestionOfRowers.append("You never rowed in this club so the system can't propose you about members you are used to row with");
         }

        return suggestionOfRowers.toString();
    }
}
