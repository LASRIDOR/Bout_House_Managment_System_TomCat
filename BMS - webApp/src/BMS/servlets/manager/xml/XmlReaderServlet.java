package BMS.servlets.manager.xml;

import BMS.boutHouse.form.exceptions.FieldContainException;
import BMS.boutHouse.form.exceptions.WrongTypeException;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.XmlFieldType;
import BMS.constants.Constants;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.server.BoutHouseDataType;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.xmlManager.XmlManager;
import com.sun.org.apache.xml.internal.res.XMLErrorResources_ko;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.tools.FileObject;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@WebServlet(name = "XmlReaderServlet", urlPatterns = "/load xml")
@MultipartConfig(maxFileSize = 169999999)
public class XmlReaderServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        InfoField<String> emailFromParameter = SessionUtils.getEmail(req);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());
        XmlManager xmlManager = ServletUtils.getXmlManager(getServletContext());
        JSONObject fields = new JSONObject();
        PrintWriter out = resp.getWriter();
        String uploadFolderPath = System.getProperty("user.dir") + File.separator + Constants.XML_LOAD_TEMP_FOLDER;

        try {
            File uploadDir = new File(uploadFolderPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            Part xmlPart = req.getPart(XmlFieldType.XML_PATH.getNameOfField());
            String uploadFilePath = uploadFolderPath + File.separator + getFileName(xmlPart);
            xmlPart.write(uploadFilePath);

            BoutHouseDataType boutHouseDataType = ServletUtils.getTypeOfManager(req);
            InfoField<String> xmlFieldPath = ServletUtils.getInfoFieldXmlPath(uploadFilePath);
            ArrayList<ArrayList<InfoField>> xmlInstancesArgs = xmlManager.loadArgsFromXml(boutHouseDataType, xmlFieldPath);

            if (!req.getParameter("erase").equals("no")) {
                fields.put("message", boutHouseManager.createXmlInstances(boutHouseDataType, emailFromParameter, xmlInstancesArgs));
            }
            else {
                fields.put("message", boutHouseManager.createAndEraseXmlInstances(boutHouseDataType, emailFromParameter, xmlInstancesArgs));
            }

            new File(uploadFilePath).delete();
            new File(uploadFolderPath).delete();
            out.print(fields);
            out.flush();
        } catch (OnlyManagerAccessException | NeedToLoginException e) {
            resp.sendRedirect(SIGN_UP_URL);
        } catch (WrongTypeException | UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion | JAXBException | ExtensionException e) {
            try {
                fields.put("message", "Error While Loading XML: " + e.getMessage());
                out.print(fields);
                out.flush();
            } catch (JSONException jsonException) {
                System.out.printf("JSON Error: missed field (%s)%n", e.getMessage());
            }
        } catch (JSONException e) {
            System.out.printf("JSON Error: missed field (%s)%n", e.getMessage());
        }
        finally {
            //new File(uploadFilePath).delete();
            new File(uploadFolderPath).delete();
        }
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }

        return "DEFAULT_FILENAME";
    }
}