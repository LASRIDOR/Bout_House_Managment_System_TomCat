package BMS.servlets.notification.perMember;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import notification.Notification;
import notification.NotificationManagerForAllMembers;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(name = "DisplayMemberNotificationServlet", urlPatterns = "/member notification")
public class DisplayMemberNotificationServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        JSONObject json = new JSONObject();
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);
        PrintWriter out = resp.getWriter();

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        try {
            NotificationManagerForMember memberNotificationManager = ServletUtils.getPerMemberNotificationManager(getServletContext());
            InfoField<String> emailToDeleteNotification = ServletUtils.getInfoField(req, MemberFieldType.EMAIL);
            List<Notification> allNotification = memberNotificationManager.getMessages(emailToDeleteNotification, 0);
            AtomicInteger indexOfNotification = new AtomicInteger();

            allNotification.forEach((value) -> {
                try {
                    json.put(String.valueOf(indexOfNotification.getAndIncrement()), value.toString());
                } catch (JSONException e) {
                    log(e.getMessage());
                }
            });

            out.print(json);
            out.flush();
        } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException fieldTypeIsNotSupportExcpetion) {
            try {
                json.put("Error", "Email is Not Valid");
                out.print(json);
                out.flush();
            } catch (JSONException e) {
                log(e.getMessage());
            }
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
