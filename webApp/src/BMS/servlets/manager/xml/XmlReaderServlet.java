package BMS.servlets.manager.xml;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.XmlFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import BMS.xml.exceptions.ExtensionException;
import com.sun.org.apache.xml.internal.res.XMLErrorResources_ko;
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

@WebServlet(name = "XmlReaderServlet", urlPatterns = "load xml")
public class XmlReaderServlet extends HttpServlet {
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
            InfoField<String> xmlPath = ServletUtils.getInfoField(req, XmlFieldType.XML_PATH);

            if(req.getParameter("erase").equals("false")){
                fields.put("message", boutHouseManager.loadXml(boutHouseDataType, emailFromParameter, xmlPath));
            }
            else {
                boutHouseManager.restartSystemManager(boutHouseDataType);
                fields.put("message", boutHouseManager.loadXml(boutHouseDataType, emailFromParameter, xmlPath));
            }

            out.print(fields);
            out.flush();
        } catch (OnlyManagerAccessException | NeedToLoginException e) {
            resp.sendRedirect(SIGN_UP_URL);
        }catch (WrongTypeException | UserInputForInfoFIeldException | FieldContainException | FieldTypeIsNotSupportExcpetion | JAXBException | ExtensionException e) {
            try {
                fields.put("message", "Error While Loading XML: " + e.getMessage());
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                System.out.printf("JSON Error: missed field (%s)%n", e.getMessage());
            }
        } catch (JSONException e) {
            System.out.printf("JSON Error: missed field (%s)%n", e.getMessage());
        }
    }
}