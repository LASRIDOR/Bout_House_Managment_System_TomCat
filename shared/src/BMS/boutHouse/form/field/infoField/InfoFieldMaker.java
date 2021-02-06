package BMS.boutHouse.form.field.infoField;


import BMS.boutHouse.encrypt.password.EncryptPassword;
import BMS.boutHouse.form.field.type.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class InfoFieldMaker {
    public static InfoField createInfoField(String fieldValue, Informable fieldType) throws UserInputForInfoFIeldException, FieldTypeIsNotSupportExcpetion {
        if (Validator.isInfoFieldInputValid(fieldType, fieldValue)){
            return fromValueAndTypeToInfoField(fieldValue, fieldType);
        }
        else{
            throw new UserInputForInfoFIeldException(fieldValue);
        }
    }

    private static InfoField fromValueAndTypeToInfoField(String fieldValue, Informable fieldType) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        InfoField infoField;

        if(fieldType instanceof MemberFieldType){
            infoField = makeMemberInfoField(fieldValue, fieldType);
        }
        else if (fieldType instanceof XmlFieldType){
            infoField = makeXmlInfoField(fieldValue, fieldType);
        }
        else if (fieldType instanceof ReservationInfoFieldType) {
            infoField = makeReservationInfoField(fieldValue, fieldType);
        }
        else if (fieldType instanceof TimeWindowInfoFieldType) {
            infoField = makeTimeWindowInfoField(fieldValue, fieldType);
        }
        else if (fieldType instanceof BoatInfoFieldType) {
            infoField = makeBoatInfoField(fieldValue, fieldType);
        }
        else{
            throw new FieldTypeIsNotSupportExcpetion(fieldType.getNameOfField());
        }

        return infoField;
    }

    private static InfoField makeTimeWindowInfoField(String fieldValue, Informable fieldType) throws FieldTypeIsNotSupportExcpetion {
        InfoField infoField;

        if (fieldType == TimeWindowInfoFieldType.TIME_WINDOW_NAME) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == TimeWindowInfoFieldType.ACTIVITY_START_TIME) {
            infoField = makeLocalTimeField(fieldValue, fieldType);
        }
        else if (fieldType == TimeWindowInfoFieldType.ACTIVITY_END_TIME) {
            infoField = makeLocalTimeField(fieldValue, fieldType);
        }
        else if (fieldType == TimeWindowInfoFieldType.BOAT_TYPE) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else {
            throw new FieldTypeIsNotSupportExcpetion(fieldType.getNameOfField() + " in TimeWindow");
        }

        return infoField;
    }

    private static InfoField makeXmlInfoField(String fieldValue, Informable fieldType) {
        return makeStringInfoField(fieldValue, fieldType);
    }

    private static InfoField makeMemberInfoField(String fieldValue, Informable fieldType) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        InfoField infoField;

        if((fieldType == MemberFieldType.EMAIL || fieldType == MemberFieldType.FREE_COMMENT_PLACE || fieldType == MemberFieldType.PHONE_NUMBER || fieldType == MemberFieldType.USERNAME)){
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == MemberFieldType.PASSWORD){
            infoField = makeEncryptedStringInfoField(fieldValue, fieldType);
        }
        else if(fieldType == MemberFieldType.DATE_OF_EXPIRY || fieldType == MemberFieldType.DATE_OF_JOIN){
            infoField = makeLocalDateTimeField(fieldValue, fieldType);
        }
        else if(fieldType == MemberFieldType.LEVEL){
            infoField = makeLevelField(fieldValue, fieldType);
        }
        else if (fieldType == MemberFieldType.PRIVATE_BOAT_HASH || fieldType == MemberFieldType.AGE){
            infoField = makeIntegerField(fieldValue, fieldType);
        }
        else if (fieldType == MemberFieldType.IS_MANAGER){
            infoField = makeBooleanInfoField(fieldValue, fieldType);
        }
        else{
            throw new FieldTypeIsNotSupportExcpetion(fieldType.getNameOfField() + " in Member");
        }

        return infoField;
    }

    private static InfoField makeBoatInfoField(String fieldValue, Informable fieldType) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        InfoField infoField;

        if (fieldType == BoatInfoFieldType.SERIAL_NUMBER) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == BoatInfoFieldType.BOAT_TYPE) {
            infoField = makeBoatTypeInfoField(fieldValue, fieldType);
        }
        else if (fieldType == BoatInfoFieldType.BOAT_NAME) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == BoatInfoFieldType.BOAT_PRIVATE) {
            infoField = makeBooleanInfoField(fieldValue, fieldType);
        }
        else if (fieldType == BoatInfoFieldType.BOAT_DISABLED) {
            infoField = makeBooleanInfoField(fieldValue, fieldType);
        }
        else {
            throw new FieldTypeIsNotSupportExcpetion(fieldType.getNameOfField() + " in Boat");
        }

        return infoField;
    }

    private static InfoField makeReservationInfoField(String fieldValue, Informable fieldType) throws FieldTypeIsNotSupportExcpetion {
        InfoField infoField;
         if (fieldType == ReservationInfoFieldType.RESERVATION_NUMBER) {
             infoField = makeStringInfoField(fieldValue, fieldType);
         }
        else if (fieldType == ReservationInfoFieldType.NAME_ROWER) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.DATE_OF_PRACTICE) {
            infoField = makeLocalDateField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.BOAT_TYPE) {
            infoField = makeBoatTypeInfoField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.NAME_OF_RESERVATION_MAKER) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.NAMES_OF_ROWERS) {
            infoField = makeStringArrayInfoField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.NAME_COXSWAIN) {
            infoField = makeStringInfoField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.NUMBER_OF_ROWERS) {
            infoField = makeIntegerField(fieldValue, fieldType);
        }
        else if (fieldType == ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER) {
            infoField = makeStringInfoField(fieldValue, fieldType);
         }
        else {
            throw new FieldTypeIsNotSupportExcpetion(fieldType.getNameOfField() + " in Reservation");
        }
        // Also need to check the names of rowers array - what about it

        return infoField;
    }

    private static InfoField<String> makeStringInfoField(String fieldValue, Informable fieldType) {
        return new InfoField<>(fieldType, fieldValue);
    }

    private static InfoField<LevelRowing> makeLevelField(String fieldValue, Informable fieldType) throws UserInputForInfoFIeldException {
        LevelRowing valueLevel;

        if(fieldValue.equals(LevelRowing.BEGINNER.getNameOfLevel())){
            valueLevel = LevelRowing.BEGINNER;
        }
        else if(fieldValue.equals(LevelRowing.INTERMEDIATE.getNameOfLevel())){
            valueLevel = LevelRowing.INTERMEDIATE;
        }
        else if (fieldValue.equals(LevelRowing.ADVANCED.getNameOfLevel())){
            valueLevel = LevelRowing.ADVANCED;
        }
        else {
            throw new UserInputForInfoFIeldException(fieldValue);
        }

        return new InfoField<>(fieldType, valueLevel);
    }

    private static InfoField<Integer> makeIntegerField(String fieldValue, Informable fieldType) {
        int valueInt = Integer.parseInt(fieldValue);

        return new InfoField<>(fieldType, valueInt);
    }

    private static InfoField<LocalDateTime> makeLocalDateTimeField(String fieldValue, Informable fieldType){
        String[] localDateTimeParamInput = fieldValue.split("T");
        String[] localDateParamInput = localDateTimeParamInput[0].split("-");
        String[] localTimeParamInput = localDateTimeParamInput[1].split(":");

        LocalDate valueLocalDate = LocalDate.of(Integer.parseInt(localDateParamInput[0]), Integer.parseInt(localDateParamInput[1]), Integer.parseInt(localDateParamInput[2]));
        LocalTime valueLocalTime = LocalTime.of(Integer.parseInt(localTimeParamInput[0]), Integer.parseInt(localTimeParamInput[1]), Integer.parseInt(localTimeParamInput[2]));
        LocalDateTime valueLocalDateTime = LocalDateTime.of(valueLocalDate, valueLocalTime);

        return new InfoField<>(fieldType, valueLocalDateTime);
    }

    private static InfoField makeStringArrayInfoField(String fieldValue, Informable fieldType) {
        String[] namesOfRowers = fieldValue.split(",");

        return new InfoField<>(fieldType, namesOfRowers);
    }
/*
    private static InfoField<TimeWindow> makeContainedTimeWindowInfoField(String fieldValue, Informable fieldType) throws Exception {
        TimeWindow timeWindow;
        String[] timeWindowStringSplit = fieldValue.split("_");
        InfoField activityName = makeTimeWindowInfoField(timeWindowStringSplit[0], TimeWindowInfoFieldType.TIME_WINDOW_NAME);
        InfoField startTime = makeTimeWindowInfoField(timeWindowStringSplit[1], TimeWindowInfoFieldType.ACTIVITY_START_TIME);
        InfoField endTime = makeTimeWindowInfoField(timeWindowStringSplit[2], TimeWindowInfoFieldType.ACTIVITY_END_TIME);

        if (timeWindowStringSplit.length == 3) {
            timeWindow = new TimeWindow(activityName, startTime, endTime);
        }
        else if (timeWindowStringSplit.length == 4) {
            InfoField boatType = makeTimeWindowInfoField(timeWindowStringSplit[3], TimeWindowInfoFieldType.BOAT_TYPE);
            timeWindow = new TimeWindow(activityName, startTime, endTime, boatType);
        }
        else {
            throw new Exception("Input received is not compatible to format");
        }

        return new InfoField<>(fieldType, timeWindow);
    }
*/
    private static InfoField<BoatType> makeBoatTypeInfoField(String fieldValue, Informable fieldType) {
        String[] boatTypeStringSplit = fieldValue.split(" ");
        BoatType boatType;

        boatType = new BoatType(boatTypeStringSplit[0], boatTypeStringSplit[1], boatTypeStringSplit[2]);

        return new InfoField<>(fieldType, boatType);
    }

    private static InfoField<LocalDate> makeLocalDateField(String fieldValue, Informable fieldType) {
        String[] localDateSplit = fieldValue.split("-");

        LocalDate localDate = LocalDate.of(Integer.parseInt(localDateSplit[0]), Integer.parseInt(localDateSplit[1]), Integer.parseInt(localDateSplit[2]));

        return new InfoField<>(fieldType, localDate);
    }

    private static InfoField<LocalTime> makeLocalTimeField(String fieldValue, Informable fieldType) {
        String[] localTimeSplit = fieldValue.split(":");

        LocalTime localTime = LocalTime.of(Integer.parseInt(localTimeSplit[0]), Integer.parseInt(localTimeSplit[1]));

        return new InfoField<>(fieldType, localTime);
    }

    private static InfoField<Boolean> makeBooleanInfoField(String fieldValue, Informable fieldType) throws UserInputForInfoFIeldException {
        boolean aBoolean;

        if (fieldValue.equalsIgnoreCase("yes")) {
            aBoolean = true;
        }
        else if (fieldValue.equalsIgnoreCase("no")){
            aBoolean = false;
        }
        else{
            throw new UserInputForInfoFIeldException(fieldValue);
        }

        return new InfoField<Boolean>(fieldType, aBoolean);
    }

    private static InfoField<String> makeEncryptedStringInfoField(String fieldValue, Informable fieldType) {
        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = EncryptPassword.generateSecurePassword(fieldValue);

        return makeStringInfoField(mySecurePassword, fieldType);
    }
}
