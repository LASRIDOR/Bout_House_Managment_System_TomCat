package BMS.servlets.instances;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
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

@WebServlet(name = "DisplayAllServlet", urlPatterns = "/display all")
public class DisplayAllServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailOfUser = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        JSONObject fields = new JSONObject();
        PrintWriter out = resp.getWriter();

        try {
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            String allInstances = boutHouseManager.getAllInstancesOfBoutHouseType(boutHouseDataType, emailOfUser);

            fields.put("message", allInstances);
            out.print(fields);
            out.flush();
        } catch (WrongTypeException e) {
            log(String.format("Failed To Get All Instances Reason: %s", e.getMessage()));
            try {
                fields.put("message", String.format("Error: %s", e.getMessage()));
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                System.out.println(String.format("JSON Error: missed field (%s)", e.getMessage()));
            }
        } catch (OnlyManagerAccessException | NeedToLoginException e) {
            resp.sendRedirect(SIGN_UP_URL);
        } catch (JSONException e) {
            System.out.println(String.format("JSON Error: missed field (%s)", e.getMessage()));
        }
    }
}
