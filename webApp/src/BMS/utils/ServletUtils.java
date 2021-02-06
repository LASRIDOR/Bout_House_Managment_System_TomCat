package BMS.utils;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.server.BoutHouseDataType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}
