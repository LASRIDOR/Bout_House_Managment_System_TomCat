package BMS.servlets.manager.account;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.InvalidInstanceField;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import BMS.xml.exceptions.ExtensionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "createServlet", urlPatterns = "/manager create")
public class createServlet extends HttpServlet {
    private static final String MANAGER_CREATE_MEMBER_URL = "manager/account/create/create.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());

        try {
            ArrayList<InfoField> allNewInstance = new ArrayList<>();

            allNewInstance.add(ServletUtils.getInfoField(req, MemberFieldType.EMAIL));
            allNewInstance.add(ServletUtils.getInfoField(req, MemberFieldType.PASSWORD));
            allNewInstance.add(ServletUtils.getInfoField(req, MemberFieldType.USERNAME));
            allNewInstance.add(ServletUtils.getInfoField(req, MemberFieldType.IS_MANAGER));

            boutHouseManager.createNewInstance(BoutHouseDataType.MEMBERS, emailFromParameter, allNewInstance);
            resp.sendRedirect(MANAGER_CREATE_MEMBER_URL);
        } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException | OnlyManagerAccessException | ExistingException | WrongTypeException | InvalidInstanceField | NeedToLoginException | JAXBException | ExtensionException e) {
            System.out.println(e.getMessage());
            resp.getWriter().println(e.getMessage());
            req.getRequestDispatcher(MANAGER_CREATE_MEMBER_URL).include(req, resp);
        }
    }
}
