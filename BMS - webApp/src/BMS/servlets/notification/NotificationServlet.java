package BMS.servlets.notification;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.constants.Constants;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import com.google.gson.Gson;
import notification.NotificationManagerForAllMembers;
import notification.Notification;
import notification.NotificationManagerForMember;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "NotificationServlet", urlPatterns = "/notification")
public class NotificationServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        int memberVersion = ServletUtils.getIntParameter(req, Constants.NOTIFICATION_MEMBER_VERSION_PARAMETER);
        int allMembersVersion = ServletUtils.getIntParameter(req, Constants.NOTIFICATION_ALL_MEMBERS_VERSION_PARAMETER);

        if (memberVersion == Constants.INT_PARAMETER_ERROR || allMembersVersion == Constants.INT_PARAMETER_ERROR) {
            return;
        }

        NotificationManagerForAllMembers allMemberNotification = ServletUtils.getAllMemberNotificationManager(getServletContext());
        NotificationManagerForMember perMemberNotificationManager = ServletUtils.getPerMemberNotificationManager(getServletContext());
        int allMembersUpdatedVersion = allMemberNotification.getVersion();
        int perMembersUpdatedVersion = perMemberNotificationManager.getVersion(emailOfLoggedMember);
        List<Notification> allMemberNotificationMessages = allMemberNotification.getMessages(allMembersVersion);
        List<Notification> memberNotificationMessages = perMemberNotificationManager.getMessages(emailOfLoggedMember, memberVersion);

        // log and create the response json string
        NotificationServlet.MessagesAndVersions cav = new NotificationServlet.MessagesAndVersions(allMemberNotificationMessages, memberNotificationMessages, allMembersUpdatedVersion, perMembersUpdatedVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cav);
        log("Server All Member Notification version: " + allMembersUpdatedVersion + ", User '" + emailOfLoggedMember.getValue() + "' Notification version: " + allMembersVersion);
        log("Server Per Member Notification version: " + perMembersUpdatedVersion + ", User '" + emailOfLoggedMember.getValue() + "' Notification version: " + memberVersion);
        log(jsonResponse);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    private static class MessagesAndVersions {
        final private List<Notification> memberEntries;
        final private List<Notification> allMembersEntries;
        final private int allMembersVersion;
        final private int memberVersion;

        public MessagesAndVersions(List<Notification> allMembersEntries, List<Notification> memberEntries, int allMembersVersion, int memberVersion) {
            this.memberEntries = memberEntries;
            this.allMembersEntries = allMembersEntries;
            this.allMembersVersion = allMembersVersion;
            this.memberVersion = memberVersion;
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
