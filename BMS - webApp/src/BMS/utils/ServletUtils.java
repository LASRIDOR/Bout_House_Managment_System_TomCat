package BMS.utils;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.*;
import BMS.bouthouse.activities.Reservation;
import BMS.bouthouse.storage.vessel.Boat;
import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.timeWindowManager.TimeWindowManager;
import BMS.server.BoutHouseDataType;
import BMS.xml.xmlManager.XmlManager;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static BMS.constants.Constants.BOUT_HOUSE_DATA_TYPE;
import static BMS.constants.Constants.DISPLAY_RESERVATIONS_OPTIONS;

public class ServletUtils {
    public static BoutHouseManager getBoutHouseManager(ServletContext servletContext) {
        return (BoutHouseManager) servletContext.getAttribute(Constants.BOUT_HOUSE_MANAGER);
    }

    public static InfoField getInfoField(HttpServletRequest request, Informable typeOfInfoField) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        String inputValue = request.getParameter(typeOfInfoField.getNameOfField());
        return inputValue != null ? InfoFieldMaker.createInfoField(inputValue, typeOfInfoField) : null;
    }

    public static BoutHouseDataType getTypeOfManager(HttpServletRequest request) throws WrongTypeException {
        return BoutHouseDataType.createBoutHouseDataType(request.getParameter(BOUT_HOUSE_DATA_TYPE));
    }

    public static InfoField getIdOfInstance(HttpServletRequest request, BoutHouseDataType instanceType) throws UserInputForInfoFIeldException, FieldTypeIsNotSupportExcpetion {
        Informable typeOfId = getTypeOfIdInstance(instanceType);
        String inputValue = request.getParameter("instanceId");
        return inputValue != null ? InfoFieldMaker.createInfoField(inputValue, typeOfId) : null;
    }

    private static Informable getTypeOfIdInstance(BoutHouseDataType instanceType) {
        Informable typeOfId;

        if (instanceType == BoutHouseDataType.MEMBERS){
            typeOfId = MemberFieldType.EMAIL;
        }
        else if (instanceType == BoutHouseDataType.BOATS){
            typeOfId = BoatInfoFieldType.SERIAL_NUMBER;
        }
        else if (instanceType == BoutHouseDataType.RESERVATION){
            typeOfId = ReservationInfoFieldType.RESERVATION_NUMBER;
        }
        else{
            typeOfId = TimeWindowInfoFieldType.TIME_WINDOW_NAME;
        }

        return typeOfId;
    }

    public static Informable getTypeOfField(BoutHouseDataType boutHouseDataType, String typeString) throws WrongTypeException {
        Informable typeOfField;

        if (boutHouseDataType == BoutHouseDataType.MEMBERS){
            typeOfField = MemberFieldType.createMemberFieldType(typeString);
        }
        else if (boutHouseDataType == BoutHouseDataType.BOATS){
            typeOfField = BoatInfoFieldType.createBoatType(typeString);
        }
        else if (boutHouseDataType == BoutHouseDataType.RESERVATION){
            typeOfField = ReservationInfoFieldType.createReservationInfoField(typeString);
        }
        else {
            typeOfField = TimeWindowInfoFieldType.createTimeWindowInfoField(typeString);
        }

        return typeOfField;
    }

    public static Informable[] getTypeOfField(BoutHouseDataType boutHouseDataType) {
        Informable[] typeOfField;

        if (boutHouseDataType == BoutHouseDataType.MEMBERS){
            typeOfField = MemberFieldType.values();
        }
        else if (boutHouseDataType == BoutHouseDataType.BOATS){
            typeOfField = BoatInfoFieldType.values();
        }
        else if (boutHouseDataType == BoutHouseDataType.RESERVATION){
            typeOfField = ReservationInfoFieldType.values();
        }
        else {
            typeOfField = TimeWindowInfoFieldType.values();
        }

        return typeOfField;
    }

    public static XmlManager getXmlManager(ServletContext servletContext) {
        return (XmlManager) servletContext.getAttribute(Constants.XML_MANAGER);
    }

    public static InfoField<String> getInfoFieldXmlPath(String xmlPath) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        return InfoFieldMaker.createInfoField(xmlPath, XmlFieldType.XML_PATH);
    }

    public static String getDisplayReservationsOption(HttpServletRequest request) {
        return request.getParameter(DISPLAY_RESERVATIONS_OPTIONS);
    }

    public static InfoField getDateToDisplayReservations(HttpServletRequest request) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        return InfoFieldMaker.createInfoField(request.getParameter("dateId"), ReservationInfoFieldType.DATE_OF_PRACTICE);
    }

    public static InfoField getReservationNumber(HttpServletRequest request) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        return InfoFieldMaker.createInfoField(request.getParameter("reservationNumber"), ReservationInfoFieldType.RESERVATION_NUMBER);
    }

    public static InfoField getAssignedBoat(HttpServletRequest request) throws FieldTypeIsNotSupportExcpetion, UserInputForInfoFIeldException {
        return InfoFieldMaker.createInfoField(request.getParameter("assignedBoat"), ReservationInfoFieldType.ASSIGNED_BOAT_SERIAL_NUMBER);
    }

    public static InfoField createTemporaryTimeWindowForReservation(ArrayList<InfoField> timeWindowFields) throws WrongTypeException {
        TimeWindow newTimeWindowInstance = new TimeWindow();

        for (InfoField field : timeWindowFields) {
            newTimeWindowInstance.setField(field);
        }

        return new InfoField(ReservationInfoFieldType.TIME_WINDOW, newTimeWindowInstance);
    }

    public static boolean isFieldOfTimeWindowType(String nameOfField) {
        for (TimeWindowInfoFieldType timeWindowField: TimeWindowInfoFieldType.values()) {
            if (nameOfField.equals(timeWindowField.getNameOfField())) {
                return true;
            }
        }

        return false;
    }

    public static InfoField turnTimeWindowToInfoField(TimeWindow timeWindow) {
        return new InfoField(ReservationInfoFieldType.TIME_WINDOW, timeWindow);
    }

    public static JSONObject createJSONReservations(List<Reservation> futureReservations) {
        JSONObject jsonOfReservations = new JSONObject();
        AtomicInteger counterOfReservation = new AtomicInteger();

        futureReservations.forEach((reservation -> {
            try {
                jsonOfReservations.put(String.valueOf(counterOfReservation.getAndIncrement()), reservation.toString());
            } catch (JSONException e) {
                System.out.println("Error: reservation #"+ reservation.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER) + "didnot enter to json");
            }
        }));

        return jsonOfReservations;
    }

    public static JSONObject createJSONBoats(List<Boat> optionalBoats) {
        JSONObject jsonOfBoats = new JSONObject();
        AtomicInteger counterOfBoats = new AtomicInteger();

        optionalBoats.forEach((boat -> {
            try {
                jsonOfBoats.put(String.valueOf(counterOfBoats.getAndIncrement()), boat.toString());
            } catch (JSONException e) {
                System.out.println("Error: boat #"+ boat.getAllFields().get(BoatInfoFieldType.SERIAL_NUMBER + "didnot enter to json"));
            }
        }));

        return jsonOfBoats;
    }
}
