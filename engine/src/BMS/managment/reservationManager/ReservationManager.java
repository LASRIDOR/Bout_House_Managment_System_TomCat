package BMS.managment.reservationManager;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.*;
import BMS.bouthouse.activities.Reservation;
import BMS.managment.userManager.MemberManager;
import BMS.managment.utils.SystemManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.InvalidInstanceField;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationManager extends SystemManager {

    public ReservationManager() {}

    @Override
    public void createNewInstance(InfoField<String> emailOfCreator, ArrayList<InfoField> args) throws InvalidInstanceField, NeedToLoginException, WrongTypeException, ExistingException {
        MemberManager.checkLogged(emailOfCreator);
        Reservation newReservation = createReservation(args);
        checkIfReservationValid(newReservation);
        checkInstanceExistence(newReservation.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER), true);
        // NOT CHECKING IF A RESERVATION EXISTS BECAUSE THE C'TOR OF RESERVATION GENERATES A NEW RESERVATION NUMBER
        // THEREFORE, WHEN CREATING A NEW RESERVATION, THERE CANNOT BE ANY EXISTING RESERVATION AT THE SYSTEM WITH THE SAME
        // RESERVATION NUMBER
        instances.put(newReservation.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER), newReservation);
    }

    private Reservation createReservation(ArrayList<InfoField> args) throws WrongTypeException {
        Map<Informable, InfoField> fields = createMapOfArgs(args);

        InfoField<TimeWindow> windowTime = fields.remove(ReservationInfoFieldType.TIME_WINDOW);
        InfoField<String> nameRower = fields.remove(ReservationInfoFieldType.NAME_ROWER);
        InfoField<LocalDate> dateOfPractice = fields.remove(ReservationInfoFieldType.DATE_OF_PRACTICE);
        InfoField<BoatType> typeOfBoat = fields.remove(ReservationInfoFieldType.BOAT_TYPE);
        InfoField<String> nameOfReservationMaker = fields.remove(ReservationInfoFieldType.NAME_OF_RESERVATION_MAKER);

        Reservation reservation = new Reservation(nameRower, dateOfPractice, windowTime, typeOfBoat, nameOfReservationMaker);

        if (fields.containsKey(ReservationInfoFieldType.NAMES_OF_ROWERS)) {
            InfoField<String[]> namesOfRowers = fields.remove(ReservationInfoFieldType.NAMES_OF_ROWERS);

            reservation.setField(namesOfRowers);
        }

        if (fields.containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
            InfoField<String> coxswainName = fields.remove(ReservationInfoFieldType.NAME_COXSWAIN);

            reservation.setField(coxswainName);
        }

        return reservation;
    }

    private Map<Informable, InfoField> createMapOfArgs(ArrayList<InfoField> args) {
        Map<Informable, InfoField> fields = new HashMap<>();

        for (InfoField arg : args) {
            fields.put(arg.getType(), arg);
        }

        return fields;
    }

    private void checkIfReservationValid(Reservation newReservation) throws InvalidInstanceField{
        if (!ReservationValidator.isReservationValid(newReservation)) {
            throw new InvalidInstanceField();
        }
    }

    private boolean checkUserAuthorizationToUpdate(InfoField<String> emailOfUpdater, InfoField<String> reservationNumber) throws NeedToLoginException {
        boolean isLoggedMemberAuthorized = false;
        Reservation reservation = (Reservation) instances.get(reservationNumber);
        String nameRower = (String) reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue();
        String nameCoxswain;
        List<String> namesOfRowers;

        if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
            nameCoxswain = (String) reservation.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN).getValue();
            if (nameCoxswain.equals(MemberManager.getNameOfLoggedMember(emailOfUpdater))) {
                isLoggedMemberAuthorized = true;
            }
        }

        if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAMES_OF_ROWERS)) {
            namesOfRowers = Arrays.asList((String[]) reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue());
            if (namesOfRowers.contains(MemberManager.getNameOfLoggedMember(emailOfUpdater))) {
                isLoggedMemberAuthorized = true;
            }
        }

        return (isLoggedMemberAuthorized || MemberManager.isManagerLogged(emailOfUpdater) || nameRower.equals(MemberManager.getNameOfLoggedMember(emailOfUpdater)));
    }

    @Override
    public void updateInstance(InfoField<String> emailOfUpdater, InfoField<String> reservationNumber, InfoField newInfoField) throws ExistingException, WrongTypeException, NeedToLoginException, IllegalAccessException, OnlyManagerAccessException, FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        MemberManager.checkLogged(emailOfUpdater);
        checkInstanceExistence(reservationNumber, false);

        if (checkUserAuthorizationToUpdate(emailOfUpdater, reservationNumber)) {
            if (newInfoField.getType() == ReservationInfoFieldType.NAME_COXSWAIN) {
                try {
                    if (!isCoxswainNeeded(reservationNumber)) {
                        throw new WrongTypeException("The reservation you've typed does not require a coxswain");
                    }
                } catch (FieldContainException e) {
                    throw new WrongTypeException("The form does not contain the field you've typed");
                }
            }
            if (newInfoField.getType() == ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER) {
                approveReservation(emailOfUpdater, reservationNumber);
            }

            if (newInfoField.getType() == ReservationInfoFieldType.NAME_ROWER) {
                Reservation reservation = (Reservation) getInstanceWithId(reservationNumber);
                String nameOfRower = (String) reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue();
                String[] namesOfRowers = (String[]) reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue();

                for (int i = 0; i < namesOfRowers.length; i++) {
                    if (namesOfRowers[i].equals(nameOfRower)) {
                        namesOfRowers[i] = (String) newInfoField.getValue();
                    }
                }

                InfoField namesOfRowersInfoField = InfoFieldMaker.createInfoField(String.join(",", namesOfRowers), ReservationInfoFieldType.NAMES_OF_ROWERS);
                setFieldToReservation(reservationNumber, namesOfRowersInfoField);
            }

            if (newInfoField.getType() == ReservationInfoFieldType.NAMES_OF_ROWERS) {
                Reservation reservation = (Reservation) getInstanceWithId(reservationNumber);
                String nameOfRower = (String) reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue();

                String[] namesOfRowers = (String[]) newInfoField.getValue();

                newInfoField = InfoFieldMaker.createInfoField((String.join(",", namesOfRowers) + "," + nameOfRower), ReservationInfoFieldType.NAMES_OF_ROWERS);

            }

            setFieldToReservation(reservationNumber, newInfoField);
        } else {
            throw new IllegalAccessException("You don't have access to a reservation you're not in.");
        }
    }

    public boolean isCoxswainNeeded(InfoField<String> reservationNumber) throws ExistingException, FieldContainException, WrongTypeException {
        Reservation theReservation = (Reservation) getInstanceWithId(reservationNumber);
        BoatType reservationBoatType = (BoatType) theReservation.getField(ReservationInfoFieldType.BOAT_TYPE).getValue();

        return (reservationBoatType.getBoatClassification().contains("+"));
    }
    @Override
    public void deleteInstance(InfoField<String> emailOfDeleter, InfoField<String> reservationNumber) throws OnlyManagerAccessException, ExistingException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfDeleter);
        checkInstanceExistence(reservationNumber, false);
        this.instances.remove(reservationNumber);
    }

    @Override
    public Map getAllInstances() {
        return instances;
    }

    @Override
    public void loadXml(ArrayList<ArrayList<InfoField>> allInstancesArgs) throws InvalidActionException {
        throw new InvalidActionException();
    }

    private void setFieldToReservation(InfoField<String> reservationNumber, InfoField infoField) throws WrongTypeException {
        Reservation reservationToSet = (Reservation) instances.remove(reservationNumber);

        reservationToSet.setField(infoField);
        instances.put(reservationToSet.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER), reservationToSet);
    }

    public List<Reservation> getReservationsOfUpcomingWeek() throws IllegalStateException{
        Map<InfoField, Reservation> allReservations = null;
        List<Reservation> reservations = null;

        allReservations = (Map)instances;

        if (allReservations.isEmpty()) {
            throw new IllegalStateException("There are no items on the list");
        }
        else {
            reservations = allReservations.
                    entrySet().
                    stream().
                    filter(reservation -> {
                        boolean isReservationInRange = false;

                        try {
                            if ((((LocalDate) reservation.getValue().getField(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()).isAfter(LocalDate.now().minusDays(1))) &&
                                    (((LocalDate) reservation.getValue().getField(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()).isBefore(LocalDate.now().plusDays(8)))) {
                                isReservationInRange = true;
                            }
                        } catch (WrongTypeException | FieldContainException e) {
                            System.out.println(e.getMessage());
                        }

                        return isReservationInRange;
                    }).map(reservation -> reservation.getValue()).collect(Collectors.toList());
        }

        return reservations;
    }

    public List<Reservation> getReservationsOfSpecificDate(InfoField dateToPrintReservations) throws IllegalArgumentException, IllegalStateException {
        Map<InfoField, Reservation> allReservations = null;
        List<Reservation> reservations = null;

        allReservations = (Map)instances;

        if (allReservations.isEmpty()) {
            throw new IllegalStateException("There are no items on the list");
        }
        else {
            if (((LocalDate) dateToPrintReservations.getValue()).isBefore(LocalDate.now().plusDays(8)) && ((LocalDate) dateToPrintReservations.getValue()).isAfter(LocalDate.now().minusDays(1)))
            {
                reservations = allReservations.
                        entrySet().
                        stream().
                        filter(reservation -> {
                            boolean isReservationInRange = false;

                            if (((LocalDate) dateToPrintReservations.getValue()).isBefore(LocalDate.now().plusDays(8)) &&
                                    ((LocalDate) dateToPrintReservations.getValue()).isAfter(LocalDate.now().minusDays(1))) {
                                try {
                                    if (((LocalDate) reservation.getValue().getField(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()).equals((LocalDate) dateToPrintReservations.getValue())) {
                                        isReservationInRange = true;
                                    }
                                } catch (WrongTypeException | FieldContainException e) {}
                            }

                            return isReservationInRange;
                        }).map(reservation -> reservation.getValue()).collect(Collectors.toList());
            }
            else {
                throw new IllegalArgumentException("The date you've typed is not in the range of the upcoming week");
            }
        }

        return reservations;
    }

    public List<Reservation> getReservationsOfPastWeek() throws IllegalStateException {
        Map<InfoField, Reservation> allReservations = null;
        List<Reservation> reservations = null;

        allReservations = (Map) instances;

        if (allReservations.isEmpty()) {
            throw new IllegalStateException("There are no items on the list");
        }
        else {
            reservations = allReservations.
                    entrySet().
                    stream().
                    filter(reservation -> {
                        boolean isReservationInRange = false;

                        try {
                            if (((LocalDate) reservation.getValue().getField(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()).isEqual(LocalDate.now())) {
                                if (((LocalTime) ((TimeWindow) reservation.getValue().getField(ReservationInfoFieldType.TIME_WINDOW).getValue()).getField(TimeWindowInfoFieldType.ACTIVITY_START_TIME).getValue()).isBefore(LocalTime.now())) {
                                    isReservationInRange = true;
                                }
                            } else if (((LocalDate) reservation.getValue().getField(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()).isAfter(LocalDate.now().minusDays(8))) {
                                if (((LocalDate) reservation.getValue().getField(ReservationInfoFieldType.DATE_OF_PRACTICE).getValue()).isBefore(LocalDate.now())) {
                                    isReservationInRange = true;
                                }
                            }
                        } catch (WrongTypeException | FieldContainException e) {
                            System.out.println(e.getMessage());
                        }

                        return isReservationInRange;
                    }).map(reservation -> reservation.getValue()).collect(Collectors.toList());
        }

        return reservations;
    }
    public boolean isMemberInReservation(Reservation reservation, InfoField<String> rowerName) {
        boolean isRowerInReservation = false;

        if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue().equals(rowerName.getValue())) {
            isRowerInReservation = true;
        } else if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
            if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN).getValue().equals(rowerName.getValue())) {
                isRowerInReservation = true;
            }
        } else if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAMES_OF_ROWERS)) {
            List<String> list = Arrays.asList((String[]) reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue());

            if (list.contains(rowerName.getValue())) {
                isRowerInReservation = true;
            }
        }
        return isRowerInReservation;
    }

    private boolean isLoggedMemberInReservation(Reservation reservation, InfoField<String> loggedMemberEmail) throws NeedToLoginException {
        boolean isCurrentUserInReservation = false;

        if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue().equals(MemberManager.getNameOfLoggedMember(loggedMemberEmail))) {
            isCurrentUserInReservation = true;
        } else if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
            if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN).getValue().equals(MemberManager.getNameOfLoggedMember(loggedMemberEmail))) {
                isCurrentUserInReservation = true;
            }
        } else if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAMES_OF_ROWERS)) {
            List<String> list = Arrays.asList((String[]) reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue());

            if (list.contains(MemberManager.getNameOfLoggedMember(loggedMemberEmail))) {
                isCurrentUserInReservation = true;
            }
        }
        return isCurrentUserInReservation;
    }

    public List<Reservation> getLoggedMemberFutureReservations(InfoField<String> loggedMemberEmail) throws IllegalStateException {
        List<Reservation> upcomingWeekReservations = getReservationsOfUpcomingWeek();

        List<Reservation> memberUpcomingWeekReservations = upcomingWeekReservations
                .stream()
                .filter(reservation -> {
                    try {
                        return isLoggedMemberInReservation(reservation, loggedMemberEmail);
                    } catch (NeedToLoginException e) {
                        throw new IllegalStateException("You need to log in first to do this");
                    }
                })
                .collect(Collectors.toList());

        if (memberUpcomingWeekReservations.isEmpty()) {
            throw new IllegalStateException("You have no reservations for the upcoming week");
        }
        else {
            return memberUpcomingWeekReservations;
        }
    }

    public List<Reservation> getLoggedMemberPastReservations(InfoField<String> loggedMemberEmail) throws IllegalStateException{
        List<Reservation> pastWeekReservations = getReservationsOfPastWeek();

        List<Reservation> memberPastWeekReservations = pastWeekReservations
                .stream()
                .filter(reservation -> {
                    try {
                        return isLoggedMemberInReservation(reservation, loggedMemberEmail);
                    } catch (NeedToLoginException e) {
                        throw new IllegalStateException("You need to log in first to do this");
                    }
                }).collect(Collectors.toList());

        if (memberPastWeekReservations.isEmpty()) {
            throw new IllegalStateException("You had no reservations on the past week");
        }
        else {
            return memberPastWeekReservations;
        }
    }

    public List<Reservation> getUnapprovedReservationOfSpecificDate(InfoField dateToPrintUnapprovedReservations) throws IllegalStateException{
        List<Reservation> reservationsOfSpecificDate = getReservationsOfSpecificDate(dateToPrintUnapprovedReservations);

        List<Reservation> unapprovedReservationsOfSpecificDate = reservationsOfSpecificDate
                .stream()
                .filter(reservation -> !reservation.isApproved())
                .collect(Collectors.toList());

        if (unapprovedReservationsOfSpecificDate.isEmpty()) {
            throw new IllegalStateException("There are no unapproved reservation on that day");
        }
        else {
            return unapprovedReservationsOfSpecificDate;
        }
    }

    public List<Reservation> getUnapprovedReservationOfUpcomingWeek() throws IllegalStateException {
        List<Reservation> reservationsOfUpcomingWeek = getReservationsOfUpcomingWeek();

        List<Reservation> unapprovedReservationOfUpcomingWeek = reservationsOfUpcomingWeek
                .stream()
                .filter(reservation -> !reservation.isApproved())
                .collect(Collectors.toList());

        if (unapprovedReservationOfUpcomingWeek.isEmpty()) {
            throw new IllegalStateException("There are no unapproved reservation in the following week");
        }
        else {
            return unapprovedReservationOfUpcomingWeek;
        }
    }

    public void approveReservation(InfoField<String> emailOfApprover, InfoField reservationNumber) throws ExistingException, OnlyManagerAccessException, NeedToLoginException {
        MemberManager.checkLoggedIsManager(emailOfApprover);
        checkInstanceExistence(reservationNumber, false);

        Reservation reservation = (Reservation) getInstanceWithId(reservationNumber);
        reservation.setApproved(true);
    }

    public void updateReservationsAfterDeletingInstance(InfoField<String> emailOfDeleter, InfoField instanceDeleted) {
        try {
            if (instanceDeleted.getType() instanceof BoatInfoFieldType) {
                updateReservationsAfterDeletingOrDisablingBoat(instanceDeleted);
            }
            else if (instanceDeleted.getType() instanceof MemberFieldType) {
                updateReservationsAfterDeletingMember(emailOfDeleter, instanceDeleted);
            }
            else if (instanceDeleted.getType() instanceof TimeWindowInfoFieldType) {
                updateReservationsAfterDeletingTimeWindow(emailOfDeleter, instanceDeleted);
            }

        }
        catch (IllegalStateException | ExistingException | WrongTypeException | OnlyManagerAccessException | NeedToLoginException ignored) {}
    }

    public void updateReservationsAfterDisablingBoat(InfoField disabledBoatSerialNumber) {
        updateReservationsAfterDeletingOrDisablingBoat(disabledBoatSerialNumber);
    }

    private void updateReservationsAfterDeletingOrDisablingBoat(InfoField instanceDeleted) {
        List<Reservation> upcomingWeekReservations  = getReservationsOfUpcomingWeek();

        for (Reservation reservation : upcomingWeekReservations) {
            if (reservation.isApproved()) {
                if (reservation.getAllFields().containsKey(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER)) {
                    if (instanceDeleted.getValue().equals(reservation.getAllFields().get(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER).getValue())) {
                        reservation.setApproved(false);
                        try {
                            reservation.deleteField(ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER);
                        } catch (FieldContainException e) {}
                    }
                }
            }
        }
    }

    private void updateReservationsAfterDeletingMember(InfoField<String> emailOfDeleter, InfoField<String> instanceDeleted) throws OnlyManagerAccessException, ExistingException, WrongTypeException, NeedToLoginException {
        List<Reservation> upcomingWeekReservations  = getReservationsOfUpcomingWeek();

        for (Reservation reservation : upcomingWeekReservations) {
            if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAME_ROWER)) {
                if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_ROWER).getValue().equals(instanceDeleted.getValue())) {
                    deleteInstance(emailOfDeleter, reservation.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER));
                }
            }
            else
            {
                if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAME_COXSWAIN)) {
                    if (reservation.getAllFields().get(ReservationInfoFieldType.NAME_COXSWAIN).getValue().equals(instanceDeleted.getValue())) {
                        reservation.setField(new InfoField(ReservationInfoFieldType.NAME_COXSWAIN, ""));
                    }

                    if (reservation.getAllFields().containsKey(ReservationInfoFieldType.NAMES_OF_ROWERS)) {
                        String[] namesOfRowers = (String[])reservation.getAllFields().get(ReservationInfoFieldType.NAMES_OF_ROWERS).getValue();
                        List<String> namesOfRowersList = Arrays.asList(namesOfRowers);

                        if (namesOfRowersList.contains(instanceDeleted.getValue())) {
                            namesOfRowersList.remove(instanceDeleted.getValue());
                            reservation.setField(new InfoField(ReservationInfoFieldType.NAMES_OF_ROWERS, namesOfRowersList.toArray()));
                        }
                    }
                }
            }
        }
    }

    private void updateReservationsAfterDeletingTimeWindow(InfoField<String> emailOfDeleter, InfoField instanceDeleted) throws OnlyManagerAccessException, ExistingException, NeedToLoginException {
        List<Reservation> upcomingWeekReservations  = getReservationsOfUpcomingWeek();

        for (Reservation reservation : upcomingWeekReservations) {
            if (reservation.getAllFields().containsKey(ReservationInfoFieldType.TIME_WINDOW)) {
                TimeWindow reservationTimeWindow = ((TimeWindow) reservation.getAllFields().get(ReservationInfoFieldType.TIME_WINDOW).getValue());
                if (instanceDeleted.getValue().equals(reservationTimeWindow.getAllFields().get(TimeWindowInfoFieldType.TIME_WINDOW_NAME).getValue())) {
                    deleteInstance(emailOfDeleter, reservation.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER));
                }
            }
        }
    }
}
