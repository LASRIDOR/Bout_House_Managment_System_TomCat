package BMS.servlets.reservation;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.constants.Constants;
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

@WebServlet(name = "assignBoatToReservation", urlPatterns = "/assign boat to reservation")
public class AssignBoatToReservationServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";
    private static final String NOTIFY_ALL_MEMBER_URL = "/notify all members";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        PrintWriter out = resp.getWriter();
        JSONObject fields = new JSONObject();


        try {
            InfoField<String> idOfInstance = ServletUtils.getReservationNumber(req);
            InfoField assignedBoat = ServletUtils.getAssignedBoat(req);

            boutHouseManager.updateSystemInstance(BoutHouseDataType.RESERVATION, emailFromParameter, idOfInstance, assignedBoat);

            getServletContext().getRequestDispatcher(NOTIFY_ALL_MEMBER_URL + "?" + Constants.NOTIFICATION_MESSAGE + "=Reservation Number: "+ idOfInstance.getValue() + " has been approved (" + "Assigned boat: " + assignedBoat.getValue() +")"+ "&" + Constants.NOTIFICATION_HEADER + "=Manager Approve Reservation").include(req, resp);
            fields.put("message", "Instance was updated successfully");
            out.print(fields);
            out.flush();
        } catch (WrongTypeException | UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion | IllegalAccessException | ExtensionException | ExistingException | JAXBException e) {
            System.out.println(e.getMessage());
            try {
                fields.put("message", String.format("Failed To Update: %s", e.getMessage()));
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                System.out.println(String.format("JSON Error: missed field (%s)", e.getMessage()));
            }
        } catch (JSONException e) {
            System.out.println(String.format("JSON Error: missed field (%s)", e.getMessage()));
        } catch (NeedToLoginException | OnlyManagerAccessException loginException) {
            resp.sendRedirect(SIGN_UP_URL);
        }
    }
}
