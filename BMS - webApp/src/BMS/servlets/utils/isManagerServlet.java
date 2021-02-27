package BMS.servlets.utils;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.NeedToLoginException;
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

@WebServlet(name = "isManagerServlet", urlPatterns = "/is manager")
public class isManagerServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);
        JSONObject json = new JSONObject();
        PrintWriter out = resp.getWriter();

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());

        try {
            if (boutHouseManager.isManagerLogin(emailOfLoggedMember)){
                json.put("message", "true");
            }else {
                json.put("message", "false");
            }

            out.print(json);
            out.flush();
        } catch (NeedToLoginException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        } catch (JSONException e) {
            System.out.printf("JSON Error: missed field (%s)%n", e.getMessage());
        }

    }
}
