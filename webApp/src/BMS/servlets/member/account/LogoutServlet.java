package BMS.servlets.member.account;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.managment.CEO.BoutHouseManager;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InfoField<String> usernameFromSession = null;
        usernameFromSession = SessionUtils.getEmail(request);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());

        if (usernameFromSession != null) {
            System.out.println("Clearing session for " + usernameFromSession);
            boutHouseManager.logoutFromMemberManager(usernameFromSession);
            SessionUtils.clearSession(request);

    /*
        when sending redirect, tomcat has a shitty logic how to calculate the URL given, weather its relative or not
        you can read about it here:
        https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletResponse.html#sendRedirect(java.lang.String)
        the best way (IMO) is to fetch the context path dynamically and build the redirection from it and on
     */
            response.sendRedirect("login");
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
