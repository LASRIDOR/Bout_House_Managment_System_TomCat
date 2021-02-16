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
import java.util.Map;

@WebServlet(name = "MemberFieldsServlet", urlPatterns = "/get member fields")
public class MemberFieldsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JSONObject fields = new JSONObject();

        try (PrintWriter out = resp.getWriter()) {
            BoutHouseManager manager = ServletUtils.getBoutHouseManager(getServletContext());
            InfoField<String> emailOfRequester = SessionUtils.getEmail(req);
            Map<Informable, InfoField> allFieldsOfInstance = manager.getAllFieldsOfInstance(BoutHouseDataType.MEMBERS, emailOfRequester);

            fields.put(MemberFieldType.EMAIL.getNameOfField(), allFieldsOfInstance.get(MemberFieldType.EMAIL).getValue());
            fields.put(MemberFieldType.PASSWORD.getNameOfField(), allFieldsOfInstance.get(MemberFieldType.PASSWORD).getValue());
            fields.put(MemberFieldType.USERNAME.getNameOfField(), allFieldsOfInstance.get(MemberFieldType.USERNAME).getValue());
            fields.put(MemberFieldType.PHONE_NUMBER.getNameOfField(), allFieldsOfInstance.get(MemberFieldType.PHONE_NUMBER).getValue());

            out.println(fields);
            out.flush();
        } catch (ExistingException e) {
            try {
                fields.put("error", "Member Doesn't exist");
            } catch (JSONException ignored) {
            }
        } catch (JSONException ignored) {
        }
    }
}
