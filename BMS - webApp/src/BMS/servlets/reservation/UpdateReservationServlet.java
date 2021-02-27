package BMS.servlets.reservation;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.boutHouse.form.field.type.ReservationInfoFieldType;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import BMS.xml.exceptions.ExtensionException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

@WebServlet(name = "UpdateServlet", urlPatterns = "/update reservation")
public class UpdateReservationServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        PrintWriter out = resp.getWriter();
        JSONObject fields = new JSONObject();

        try {
            String namesOfRowersString = "";
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            InfoField<String> idOfInstance = ServletUtils.getIdOfInstance(req, boutHouseDataType);
            ArrayList<InfoField> timeWindowFields = new ArrayList<>();
            Enumeration<String> parameterNames = req.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (!paramName.equals("BoutHouseDataType") && !paramName.equals("instanceId")) {
                    String paramValues = req.getParameter(paramName);
                    if (!paramValues.equals("")) {
                        if (paramName.contains("RowerNo.")) {
                            namesOfRowersString += paramValues + ",";
                        }
                        else {
                            if (paramName.equals(TimeWindowInfoFieldType.TIME_WINDOW_NAME.getNameOfField()) ||
                                    paramName.equals(TimeWindowInfoFieldType.ACTIVITY_START_TIME.getNameOfField()) ||
                                    paramName.equals(TimeWindowInfoFieldType.ACTIVITY_END_TIME.getNameOfField()) ||
                                    paramName.equals(TimeWindowInfoFieldType.BOAT_TYPE.getNameOfField())) {

                                Informable typeOfField = ServletUtils.getTypeOfField(BoutHouseDataType.TIME_WINDOW, paramName);
                                InfoField timeWindowField = ServletUtils.getInfoField(req, typeOfField);
                                timeWindowFields.add(timeWindowField);

                            } else {
                                Informable typeOfField = ServletUtils.getTypeOfField(boutHouseDataType, paramName);
                                InfoField fieldToUpdate = ServletUtils.getInfoField(req, typeOfField);
                                boutHouseManager.updateSystemInstance(boutHouseDataType, emailFromParameter, idOfInstance, fieldToUpdate);
                            }
                        }
                    }
                }
            }

            if (!namesOfRowersString.isEmpty()) {
                InfoField fieldToUpdate = InfoFieldMaker.createInfoField(namesOfRowersString, ReservationInfoFieldType.NAMES_OF_ROWERS);
                boutHouseManager.updateSystemInstance(boutHouseDataType, emailFromParameter, idOfInstance, fieldToUpdate);
            }

            if (!timeWindowFields.isEmpty()) {
                if (timeWindowFields.size() == 1) {
                    boutHouseManager.updateSystemInstance(boutHouseDataType, emailFromParameter, idOfInstance, ServletUtils.turnTimeWindowToInfoField(boutHouseManager.getTimeWindowByActivityName(timeWindowFields.get(0))));
                }
                else {
                    boutHouseManager.updateSystemInstance(boutHouseDataType, emailFromParameter, idOfInstance, ServletUtils.createTemporaryTimeWindowForReservation(timeWindowFields));
                }
            }

            fields.put("message", "Instance was updated successfully");
            out.print(fields);
            out.flush();
        } catch (NeedToLoginException | OnlyManagerAccessException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        } catch (WrongTypeException | UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion | IllegalAccessException | ExtensionException | ExistingException | JAXBException e) {
            System.out.println(e.getMessage());
            try {
                fields.put("message", String.format("Failed To Update: %s", e.getMessage()));
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                System.out.println(String.format("JSON Error: missed field (%s)", e.getMessage()));
            }
        } catch (JSONException e) {
            System.out.println(String.format("JSON Error: missed field (%s)", e.getMessage()));
        }
    }
}
