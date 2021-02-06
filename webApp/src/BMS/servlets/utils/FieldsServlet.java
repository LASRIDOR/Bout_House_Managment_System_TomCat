package BMS.servlets.utils;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
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

@WebServlet(name = "FieldsServlet", urlPatterns = "/get all fields")
public class FieldsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //returning JSON objects, not HTML
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
                JSONObject fields = new JSONObject();
                BoutHouseManager manager = ServletUtils.getBoutHouseManager(getServletContext());
                BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
                Map<Informable, InfoField> allFieldsOfInstance = manager.getAllFieldsOfInstance(boutHouseDataType, SessionUtils.getEmail(req));

                allFieldsOfInstance.values().forEach((field) -> {
                    try {
                        fields.put(field.getType().getNameOfField(), field.getValue());
                    } catch (JSONException e) {
                        System.out.println(String.format("Error: missed field (%s)", e.getMessage()));
                    }
                });

                out.println(fields);
                out.flush();
        } catch (ExistingException | WrongTypeException e) {
            System.out.println(e.getMessage());
        }
    }
}
