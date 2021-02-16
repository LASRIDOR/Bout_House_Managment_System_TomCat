package BMS.utils;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.*;
import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.server.BoutHouseDataType;
import BMS.xml.xmlManager.XmlManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static BMS.constants.Constants.BOUT_HOUSE_DATA_TYPE;

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
}
