package BMS.servlets.instances;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.ExistingException;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "UpdateServlet", urlPatterns = "/update")
public class UpdateServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        StringBuilder errors = new StringBuilder();
        resp.setContentType("text/html;charset=UTF-8");

        try {
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            InfoField<String> idOfInstance = ServletUtils.getIdOfInstance(req, boutHouseDataType);
            Enumeration<String> parameterNames = req.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String paramValues = req.getParameter(paramName);
                Informable typeOfField = ServletUtils.getTypeOfField(boutHouseDataType, paramValues);
                InfoField fieldToUpdate = ServletUtils.getInfoField(req, typeOfField);
                boutHouseManager.updateSystemInstance(boutHouseDataType, emailFromParameter, idOfInstance, fieldToUpdate);
            }

        } catch (NeedToLoginException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        } catch (WrongTypeException | UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion | OnlyManagerAccessException | IllegalAccessException | ExtensionException | ExistingException | JAXBException e) {
            System.out.println(e.getMessage());
            errors.append(e.getMessage()).append(System.lineSeparator());
        }
    }
}
