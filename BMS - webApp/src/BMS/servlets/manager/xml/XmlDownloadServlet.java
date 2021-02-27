package BMS.servlets.manager.xml;

import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.managment.CEO.BoutHouseManager;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "XmlDownloadServlet", urlPatterns = "/download xml database")
public class XmlDownloadServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        try {
            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            String filepath = boutHouseManager.getDatabaseLocation(boutHouseDataType);
            String filename = filepath.substring(filepath.lastIndexOf("/"));
            resp.setContentType("APPLICATION/OCTET-STREAM");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            FileInputStream fileInputStream = new FileInputStream(filepath);

            int i;
            while ((i = fileInputStream.read()) != -1) {
                out.write(i);
            }

            log(emailOfLoggedMember.getValue() + " Downloaded " + boutHouseDataType.getNameOfManager() + " XML");
            fileInputStream.close();
            out.close();
        } catch (WrongTypeException e) {
            log(e.getMessage());
        }
    }
}
