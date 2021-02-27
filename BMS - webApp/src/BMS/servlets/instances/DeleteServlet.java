package BMS.servlets.instances;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
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
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        JSONObject fields = new JSONObject();
        PrintWriter out = resp.getWriter();

        try{
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            InfoField<String> idOfInstance = ServletUtils.getIdOfInstance(req, boutHouseDataType);

            log(emailOfLoggedMember.getValue() + " Deleted " + boutHouseDataType.getNameOfManager());
            boutHouseManager.deleteInstance(boutHouseDataType, emailOfLoggedMember, idOfInstance);
            fields.put("message", "Instance Was Deleted Successfully");
             out.print(fields);
            out.flush();
        } catch (NeedToLoginException | OnlyManagerAccessException loginException) {
        resp.sendRedirect(SIGN_UP_URL);
        } catch (UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion | WrongTypeException | ExtensionException | ExistingException | JAXBException e) {
            try {
                fields.put("message", String.format("Error while deleting: %s", e.getMessage()));
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                String.format("JSON Error: missed field (%s)", e.getMessage()) ;           }
        } catch (JSONException e) {
            log(String.format("JSON Error: missed field (%s)", e.getMessage()));
        }
    }
}
