package BMS.servlets.notification.allMembers;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.constants.Constants;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import notification.NotificationManagerForAllMembers;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SendNotificationToAllMembersServlet", urlPatterns = "/notify all members")
public class SendNotificationToAllMembersServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("application/json");
        JSONObject response = new JSONObject();
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        try (PrintWriter out = resp.getWriter()) {
            NotificationManagerForAllMembers notificationManager = ServletUtils.getAllMemberNotificationManager(getServletContext());
            String message = req.getParameter(Constants.NOTIFICATION_MESSAGE);
            String header = req.getParameter(Constants.NOTIFICATION_HEADER);
            if (message != null && !message.trim().isEmpty()) {
                System.out.println("Adding notification string from " + emailOfLoggedMember.getValue() + ": " + "'" + header + " :" + message + "'");
                notificationManager.addNotification(header, message);
                try {
                    response.put("message", "Notification was sent!");
                    out.print(response);
                    out.flush();
                } catch (JSONException e) {
                    log("Json Problem");
                }
            }

            log(emailOfLoggedMember.getValue() + "Notify all member in:" + message);
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