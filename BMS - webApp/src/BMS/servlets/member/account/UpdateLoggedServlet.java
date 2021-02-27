package BMS.servlets.member.account;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.NeedToLoginException;
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

@WebServlet(name = "UpdateLoggedServlet", urlPatterns = "/logged member update")
public class UpdateLoggedServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/home.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        StringBuilder errors = new StringBuilder();
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()){
            JSONObject message = new JSONObject();
            errors.append(updateInBoutHouse(req, boutHouseManager, emailOfLoggedMember, MemberFieldType.EMAIL)).append(System.lineSeparator());
            errors.append(updateInBoutHouse(req, boutHouseManager, emailOfLoggedMember, MemberFieldType.PASSWORD)).append(System.lineSeparator());
            errors.append(updateInBoutHouse(req, boutHouseManager, emailOfLoggedMember, MemberFieldType.PHONE_NUMBER)).append(System.lineSeparator());
            errors.append(updateInBoutHouse(req, boutHouseManager, emailOfLoggedMember, MemberFieldType.USERNAME)).append(System.lineSeparator());
            message.put("message", errors.toString());
            out.println(message);
        } catch (NeedToLoginException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    private String updateInBoutHouse(HttpServletRequest req, BoutHouseManager boutHouseManager, InfoField<String> emailFromParameter, MemberFieldType fieldType) throws NeedToLoginException {
        String message = "";

        try {
            InfoField<String> toUpdate = ServletUtils.getInfoField(req, fieldType);

            if(toUpdate != null) {
                boutHouseManager.updateInfoFieldOfLoggedMember(emailFromParameter, toUpdate);
                message = String.format("update: %s Successfully", fieldType.getNameOfField());
                System.out.println(message);
            }

        } catch (WrongTypeException | FieldContainException | ExtensionException | FileNotFoundException | JAXBException | ExistingException | FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            message = String.format("Error: couldn't update %s because %s", fieldType.getNameOfField(), e.getMessage());
            System.out.println(message);
        }

        return message;
    }
}
