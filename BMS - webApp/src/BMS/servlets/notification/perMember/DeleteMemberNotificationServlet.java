package BMS.servlets.notification.perMember;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.constants.Constants;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import notification.NotificationManagerForAllMembers;
import notification.NotificationManagerForMember;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteMemberNotificationServlet", urlPatterns = "/delete notification from member")
public class DeleteMemberNotificationServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        try {
            NotificationManagerForMember allMemberNotificationManager = ServletUtils.getPerMemberNotificationManager(getServletContext());
            int indexToDelete = ServletUtils.getIntParameter(req, Constants.NOTIFICATION_INDEX);
            InfoField<String> emailOfMemberToDeleteNotification = ServletUtils.getInfoField(req, MemberFieldType.EMAIL);
            allMemberNotificationManager.deleteNotification(emailOfMemberToDeleteNotification, indexToDelete);
        } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            
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
