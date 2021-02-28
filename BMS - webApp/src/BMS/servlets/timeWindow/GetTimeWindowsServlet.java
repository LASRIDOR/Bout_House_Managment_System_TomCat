package BMS.servlets.timeWindow;
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


@WebServlet(name = "GetTimeWindowServlet", urlPatterns = "/get time windows")
public class GetTimeWindowsServlet extends HttpServlet {
    final String TIME_WINDOW = "Time Window";
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailOfUser = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        JSONObject fields = new JSONObject();
        PrintWriter out = resp.getWriter();

        try {
            boolean isTimeWindowListEmpty = boutHouseManager.isTimeWindowListEmpty();

            if (!isTimeWindowListEmpty) {
                BoutHouseDataType timeWindowDataType = BoutHouseDataType.createBoutHouseDataType(TIME_WINDOW);
                String allTimeWindows = boutHouseManager.getAllInstancesOfBoutHouseType(timeWindowDataType, emailOfUser);
                fields.put("message", allTimeWindows);
            }

            fields.put("isTimeWindowListEmpty", String.valueOf(isTimeWindowListEmpty));
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
