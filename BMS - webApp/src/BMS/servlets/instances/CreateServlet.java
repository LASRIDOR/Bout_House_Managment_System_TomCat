package BMS.servlets.instances;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
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

@WebServlet(name = "CreateServlet", urlPatterns = "/create")
public class CreateServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        JSONObject fields = new JSONObject();
        PrintWriter out = resp.getWriter();

        try {
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            ArrayList<InfoField> allNewInstance = new ArrayList<>();
            Enumeration<String> parameterNames = req.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (!paramName.equals("BoutHouseDataType")) {
                    String paramValues = req.getParameter(paramName);
                    if (!paramValues.equals("")) {
                        Informable typeOfField = ServletUtils.getTypeOfField(boutHouseDataType, paramName);
                        allNewInstance.add(ServletUtils.getInfoField(req, typeOfField));
                    }
                }
            }

            boutHouseManager.createNewInstance(boutHouseDataType, emailFromParameter, allNewInstance);
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
