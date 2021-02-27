package BMS.servlets.instances;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.constants.Constants;
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
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "UpdateServlet", urlPatterns = "/update")
public class UpdateServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";
    private static final String NOTIFY_MEMBER_URL = "/notify member";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        PrintWriter out = resp.getWriter();
        JSONObject fields = new JSONObject();
        StringBuilder messageOfUpdatedFields = new StringBuilder();
        messageOfUpdatedFields.append("Manager updated you profile and now: ").append(System.lineSeparator());

        try {
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            InfoField<String> idOfInstance = ServletUtils.getIdOfInstance(req, boutHouseDataType);
            Enumeration<String> parameterNames = req.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (!paramName.equals("BoutHouseDataType") && !paramName.equals("instanceId")) {
                    String paramValues = req.getParameter(paramName);
                    if (!paramValues.equals("")) {
                        Informable typeOfField = ServletUtils.getTypeOfField(boutHouseDataType, paramName);
                        InfoField fieldToUpdate = ServletUtils.getInfoField(req, typeOfField);
                        boutHouseManager.updateSystemInstance(boutHouseDataType, emailOfLoggedMember, idOfInstance, fieldToUpdate);
                        messageOfUpdatedFields.append(fieldToUpdate.getType().getNameOfField() + " is " + fieldToUpdate.getValue() + System.lineSeparator());
                    }
                }
            }

            if (boutHouseDataType == BoutHouseDataType.MEMBERS){
                getServletContext().getRequestDispatcher(NOTIFY_MEMBER_URL + "?" + Constants.NOTIFICATION_MESSAGE + "=" + messageOfUpdatedFields.toString() + "&" + Constants.NOTIFICATION_HEADER + "=Manager updated you profile" + "&Email=" + idOfInstance.getValue()).include(req, resp);
            }

            log(emailOfLoggedMember.getValue() + " Updated " + boutHouseDataType.getNameOfManager());
            fields.put("message", "Instance was updated successfully");
            out.print(fields);
            out.flush();
        } catch (NeedToLoginException | OnlyManagerAccessException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        } catch (WrongTypeException | UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion | IllegalAccessException | ExtensionException | ExistingException | JAXBException e) {
            log(e.getMessage());
            try {
                fields.put("message", String.format("Failed To Update: %s", e.getMessage()));
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                log(String.format("JSON Error: missed field (%s)", e.getMessage()));
            }
        } catch (JSONException e) {
            log(String.format("JSON Error: missed field (%s)", e.getMessage()));
        }
    }
}
