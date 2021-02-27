package BMS.servlets.utils;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "GetAllFieldsServlet", urlPatterns = "/get all fields")
public class GetAllFieldsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject fields = new JSONObject();

        try {
            BoutHouseManager manager = ServletUtils.getBoutHouseManager(getServletContext());
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            Map<Informable, InfoField> allFieldsOfInstance = manager.getAllFieldsOfInstance(boutHouseDataType, ServletUtils.getIdOfInstance(req, boutHouseDataType));
            Informable[] allFields = ServletUtils.getTypeOfField(boutHouseDataType);

            for (Informable field : allFields) {
                InfoField fieldOfInstance =  allFieldsOfInstance.get(field);

                try {

                if (fieldOfInstance == null)
                    fields.put(field.getNameOfField(), "");
                else
                    fields.put(field.getNameOfField(), fieldOfInstance.getValue());

                } catch (JSONException e) {
                    System.out.printf("json error: missed field (%s)%n", e.getMessage());
                }
            }

            out.println(fields);
            out.flush();
        }catch (UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion e){
            try {
                fields.put("ERROR", "Wrong Input");
                out.println(fields);
                out.flush();
            } catch (JSONException ignored) {
            }

            System.out.println(e.getMessage());
        } catch (ExistingException | WrongTypeException e) {
            try {
                fields.put("ERROR", "can't find");
                out.println(fields);
                out.flush();
            } catch (JSONException ignored) {
            }

            System.out.println(e.getMessage());
        }
    }
}
