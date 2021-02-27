package BMS.servlets.member.reservation;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.bouthouse.activities.Reservation;
import BMS.managment.CEO.BoutHouseManager;
import BMS.utils.ServletUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static BMS.utils.ServletUtils.createJSONReservations;

@WebServlet(name = "DisplayReservationsServlet", urlPatterns = "/display reservations")
public class DisplayReservationsServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // returning JSON objects, not HTML
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json;
        List<Reservation> reservationToDisplayList = null;

        try {
            BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
            String displayReservationOption = ServletUtils.getDisplayReservationsOption(req);

            switch (displayReservationOption) {
                case "displayUpcomingWeekReservationsButton":
                    reservationToDisplayList = boutHouseManager.getReservationsOfUpcomingWeek();
                    break;
                case "displaySpecificDateReservationsButton":
                    reservationToDisplayList = boutHouseManager.getReservationsOfSpecificDate(ServletUtils.getDateToDisplayReservations(req));
                    break;
                case "displayUnapprovedUpcomingWeekReservationsButton":
                    reservationToDisplayList = boutHouseManager.getUnapprovedReservationOfUpcomingWeek();
                    break;
                case "displayUnapprovedSpecificDateReservationsButton":
                    reservationToDisplayList = boutHouseManager.getUnapprovedReservationOfSpecificDate(ServletUtils.getDateToDisplayReservations(req));
                    break;
            }

            json = createJSONReservations(reservationToDisplayList);
            out.println(json);
            out.flush();
        }catch (IllegalStateException e){
            json = new JSONObject();
            try {
                json.put("0", "There are no reservations to display");
                out.println(json);
                out.flush();
            } catch (JSONException jsonException) {
                System.out.println(jsonException.getMessage());
            }
        } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException fieldTypeIsNotSupportExcpetion) {
            System.out.println(fieldTypeIsNotSupportExcpetion.getMessage());
        }
    }
}
