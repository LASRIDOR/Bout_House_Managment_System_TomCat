package BMS.servlets.reservation;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.bouthouse.storage.vessel.Boat;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.ExistingException;
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

@WebServlet(name = "getBoatsToAssignServlet", urlPatterns = "/get boats to assign")
public class GetBoatsToAssignServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //returning JSON objects, not HTML
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()){
            JSONObject json;
            try {
                BoutHouseManager manager = ServletUtils.getBoutHouseManager(getServletContext());
                List<Boat> optionalBoats = manager.getOptionalBoatsToAssignToReservation(ServletUtils.getReservationNumber(req));
                json = ServletUtils.createJSONBoats(optionalBoats);
                out.println(json);
                out.flush();
            }catch (IllegalStateException e){
                json = new JSONObject();
                json.put("0", "There are no boats to assign to reservation");
                out.println(json);
                out.flush();
            } catch (FieldTypeIsNotSupportExcpetion | ExistingException | UserInputForInfoFIeldException fieldTypeIsNotSupportExcpetion) {
                fieldTypeIsNotSupportExcpetion.printStackTrace();
            }
        }catch (IOException | JSONException e){
            System.out.println(e.getMessage());
        }
    }
}
