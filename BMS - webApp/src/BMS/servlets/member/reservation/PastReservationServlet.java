package BMS.servlets.member.reservation;


import BMS.boutHouse.form.field.type.ReservationInfoFieldType;
import BMS.bouthouse.activities.Reservation;
import BMS.managment.CEO.BoutHouseManager;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static BMS.utils.ServletUtils.createJSONReservations;


@WebServlet(name = "PastReservationServlet", urlPatterns = "/past reservation")
public class PastReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //returning JSON objects, not HTML
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()){
            JSONObject json;
            try {
                BoutHouseManager manager = ServletUtils.getBoutHouseManager(getServletContext());
                List<Reservation> futureReservations = manager.getLoggedMemberPastReservations(SessionUtils.getEmail(req));
                json = createJSONReservations(futureReservations);
                out.println(json);
                out.flush();
            }catch (IllegalStateException e){
                json = new JSONObject();
                json.put("0", "You don't have any past reservations");
                out.println(json);
                out.flush();
            }
        }catch (IOException | JSONException e){
            System.out.println(e.getMessage());
        }
    }
}
