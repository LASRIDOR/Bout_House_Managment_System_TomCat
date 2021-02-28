package BMS.servlets.reservation;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.ReservationInfoFieldType;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.timeWindowManager.TimeWindowManager;
import BMS.managment.userManager.MemberManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.InvalidInstanceField;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

@WebServlet(name = "createReservationServlet", urlPatterns = "/create reservation")
public class CreateReservationServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        JSONObject fields = new JSONObject();
        PrintWriter out = resp.getWriter();

        try {
            StringBuilder namesOfRowersString = new StringBuilder();
            BoutHouseDataType boutHouseDataType = BoutHouseDataType.RESERVATION;// = ServletUtils.getTypeOfManager(req);
            InfoField nameOfReservationMaker = InfoFieldMaker.createInfoField(MemberManager.getNameOfLoggedMember(emailFromParameter), ReservationInfoFieldType.NAME_OF_RESERVATION_MAKER);
            ArrayList<InfoField> reservationFields = new ArrayList<>();
            ArrayList<InfoField> timeWindowFields = new ArrayList<>();
            Enumeration<String> parameterNames = req.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (!paramName.equals("BoutHouseDataType")) {
                    String paramValues = req.getParameter(paramName);
                    if (!paramValues.equals("")) {
                        if (paramName.contains("RowerNo.")) {
                            namesOfRowersString.append(paramValues).append(",");
                        }
                        else {
                            boutHouseDataType = (ServletUtils.isFieldOfTimeWindowType(paramName)) ? BoutHouseDataType.TIME_WINDOW : BoutHouseDataType.RESERVATION;
                            Informable typeOfField = ServletUtils.getTypeOfField(boutHouseDataType, paramName);
                            InfoField currentFieldAsInfoField = ServletUtils.getInfoField(req, typeOfField);

                            if (paramName.equals(ReservationInfoFieldType.NAME_ROWER.getNameOfField())) {
                                namesOfRowersString.append(paramValues);
                            }
                            if (typeOfField instanceof TimeWindowInfoFieldType) {
                                timeWindowFields.add(currentFieldAsInfoField);
                            }
                            else {
                                reservationFields.add(currentFieldAsInfoField);
                            }
                        }
                    }
                }
            }

            reservationFields.add(nameOfReservationMaker);
            if (timeWindowFields.size() == 1) {
                reservationFields.add(ServletUtils.turnTimeWindowToInfoField(boutHouseManager.getTimeWindowByActivityName(timeWindowFields.get(0))));
            }
            else {
                reservationFields.add(ServletUtils.createTemporaryTimeWindowForReservation(timeWindowFields));
            }
            reservationFields.add(InfoFieldMaker.createInfoField(namesOfRowersString.toString(), ReservationInfoFieldType.NAMES_OF_ROWERS));

            boutHouseManager.createNewInstance(boutHouseDataType, emailFromParameter, reservationFields);
            fields.put("message", "Instance was created successfully");
            out.print(fields);
            out.flush();
        }catch (NeedToLoginException | OnlyManagerAccessException e){
            resp.sendRedirect(SIGN_UP_URL);
        }
        catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException | ExistingException | WrongTypeException | InvalidInstanceField | JAXBException | ExtensionException e) {
            try {
                fields.put("message", "Error: " + e.getMessage());
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


