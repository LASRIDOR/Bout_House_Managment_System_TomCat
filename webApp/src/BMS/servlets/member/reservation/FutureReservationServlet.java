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


@WebServlet(name = "FutureReservationServlet", urlPatterns = "/future reservation")
public class FutureReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //returning JSON objects, not HTML
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()){
            JSONObject json;
            try {
                BoutHouseManager manager = ServletUtils.getBoutHouseManager(getServletContext());
                List<Reservation> futureReservations = manager.getLoggedMemberFutureReservations(SessionUtils.getEmail(req));
                json = createJSONReservations(futureReservations);
                out.println(json);
                out.flush();
            }catch (IllegalStateException e){
                json = new JSONObject();
                json.put("0", "You don't have future reservation at the moment");
                out.println(json);
                out.flush();
            }
        }catch (IOException | JSONException e){
            System.out.println(e.getMessage());
        }
    }

    private JSONObject createJSONReservations(List<Reservation> futureReservations) {
        JSONObject jsonOfReservations = new JSONObject();
        AtomicInteger counterOfReservation = new AtomicInteger();

        futureReservations.forEach((reservation -> {
            try {
                jsonOfReservations.put(String.valueOf(counterOfReservation.getAndIncrement()), reservation.toString());
            } catch (JSONException e) {
                System.out.println("Error: reservation #"+ reservation.getAllFields().get(ReservationInfoFieldType.RESERVATION_NUMBER) + "didnot enter to json");
            }
        }));

        return jsonOfReservations;
    }
}
