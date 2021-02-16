package BMS.servlets.utils;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.type.Informable;
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

@WebServlet(name = "FieldOfTypeServlet", urlPatterns = "/field of type")
public class FieldOfTypeServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        JSONObject fields = new JSONObject();
        BoutHouseDataType boutHouseDataType = null;

        try (PrintWriter out = response.getWriter()){
            boutHouseDataType = ServletUtils.getTypeOfManager(request);
            Informable[] allFields = ServletUtils.getTypeOfField(boutHouseDataType);

            for (Informable field : allFields) {
                try {
                    fields.put(field.getNameOfField(), "");
                } catch (JSONException e) {
                    System.out.printf("json error: missed field (%s)%n", e.getMessage());
                }
            }

            out.print(fields);
            out.flush();
        } catch (WrongTypeException e) {
            System.out.println(e.getMessage());
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
