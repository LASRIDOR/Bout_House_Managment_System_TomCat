package BMS.servlets.notification.perMember;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import notification.NotificationManagerForMember;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SentNotificationToMemberServlet", urlPatterns = "/notify member")
public class SentNotificationToMemberServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        JSONObject response = new JSONObject();
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        PrintWriter out = resp.getWriter();
        try {
            NotificationManagerForMember notificationManager = ServletUtils.getPerMemberNotificationManager(getServletContext());
            String message = req.getParameter(Constants.NOTIFICATION_MESSAGE);
            String header = req.getParameter(Constants.NOTIFICATION_HEADER);
            InfoField<String> email = ServletUtils.getInfoField(req, MemberFieldType.EMAIL);
            BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());

            if(email != null) {
                if (boutHouseManager.isInstanceExist(BoutHouseDataType.MEMBERS, email)) {
                    if (message != null && !message.trim().isEmpty()) {
                        System.out.println("Adding notification string from " + (emailOfLoggedMember != null ? emailOfLoggedMember.getValue() : "Unknown") + ": " + "'" + header + " :" + message + "'");
                        notificationManager.addNotification(email, header, message);
                        response.put("message", "Notification was sent!");
                    }
                } else {
                    response.put("message", "member does not exist");
                }
            }
            else{
                response.put("message", "email is not valid");
            }

            log(emailOfLoggedMember.getValue() + " notify " + email.getValue() + " in " + message);
            out.print(response);
            out.flush();
        } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            try {
                response.put("message", "email is not valid");
                out.print(response);
                out.flush();
            } catch (JSONException jsonException) {
                log(e.getMessage());
            }
            log(e.getMessage());
        } catch (JSONException e) {
            log(e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
