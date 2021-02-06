package BMS.servlets.instances;

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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@WebServlet(name = "UpdateServlet", urlPatterns = "/member update")
public class UpdateServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/home.html";
    private static final String MEMBER_UPDATE_URL = "Member/account/update/update.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());

        try {

            updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.EMAIL);
            updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.PASSWORD);
            updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.PHONE_NUMBER);
            updateInBoutHouse(resp, req, boutHouseManager, emailFromParameter, MemberFieldType.USERNAME);
            req.getRequestDispatcher(MEMBER_UPDATE_URL).include(req, resp);
        } catch (NeedToLoginException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        }
    }

    private void updateInBoutHouse(HttpServletResponse resp, HttpServletRequest req, BoutHouseManager boutHouseManager, InfoField<String> emailFromParameter, MemberFieldType fieldType) throws IOException, NeedToLoginException, ServletException {
        try {
            InfoField<String> toUpdate = ServletUtils.getInfoField(req, fieldType);

            if(toUpdate != null) {
                boutHouseManager.updateInfoFieldOfLoggedMember(emailFromParameter, toUpdate);
                resp.getWriter().println(String.format("update: %s Successfully", fieldType.getNameOfField()));
                System.out.printf("update: %s Successfully%n", fieldType.getNameOfField());
            }
        } catch (WrongTypeException | FieldContainException | ExtensionException | FileNotFoundException | JAXBException | ExistingException | FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            resp.getWriter().println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
