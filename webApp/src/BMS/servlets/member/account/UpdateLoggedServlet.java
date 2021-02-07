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
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import BMS.xml.exceptions.ExtensionException;

import javax.persistence.criteria.CriteriaBuilder;
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
    private static final String MEMBER_ACCOUNT_URL = "/webApp/Member/account/account.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        StringBuilder errors = new StringBuilder();
        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()){
            errors.append(updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.EMAIL)).append(System.lineSeparator());
            errors.append(updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.PASSWORD)).append(System.lineSeparator());
            errors.append(updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.PHONE_NUMBER)).append(System.lineSeparator());
            errors.append(updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.USERNAME)).append(System.lineSeparator());
            out.println(errors.toString());
        } catch (NeedToLoginException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        }

    }

    private String updateInBoutHouse(HttpServletResponse resp, HttpServletRequest req, BoutHouseManager boutHouseManager, InfoField<String> emailFromParameter, MemberFieldType fieldType) throws IOException, NeedToLoginException, ServletException {
        String message = "";

        try {
            InfoField<String> toUpdate = ServletUtils.getInfoField(req, fieldType);

            if(toUpdate != null) {
                boutHouseManager.updateInfoFieldOfLoggedMember(emailFromParameter, toUpdate);
                message = String.format("update: %s Successfully", fieldType.getNameOfField());
                System.out.println(message);
            }

        } catch (WrongTypeException | FieldContainException | ExtensionException | FileNotFoundException | JAXBException | ExistingException | FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            message = String.format("Error: couldnt update %s because %s", fieldType.getNameOfField(), e.getMessage());
            System.out.println(message);
        }

        return message;
    }
}
