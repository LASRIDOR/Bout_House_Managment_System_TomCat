package BMS.servlets.utils;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.managment.CEO.BoutHouseManager;
import BMS.server.BoutHouseDataType;
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

@WebServlet(name = "ExistingCheckOfInstanceServlet", urlPatterns = "/existing instance")
public class ExistingCheckOfInstanceServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        JSONObject json = new JSONObject();
        PrintWriter out = response.getWriter();

        try {
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(request);
            InfoField<String> instanceId = ServletUtils.getIdOfInstance(request, boutHouseDataType);
            boolean isExist = boutHouseManager.isInstanceExist(boutHouseDataType, instanceId);
            json.put("message", isExist ? "Instance Exist!" : "Instance doesn't exist");
            out.print(json);
            out.flush();
        } catch (WrongTypeException | FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException e) {
            try {
                json.put("message", e.getMessage());
                out.print(json);
                out.flush();
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
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
