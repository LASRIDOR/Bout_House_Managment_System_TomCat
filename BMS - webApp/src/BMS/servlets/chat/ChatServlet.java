package BMS.servlets.chat;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.constants.Constants;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import chat.ChatManager;
import chat.MessageEntry;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ChatServlet", urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        /*
          verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
          Obviously the UI should be ready for such a case and handle it properly
         */
        int chatVersion = ServletUtils.getIntParameter(req, Constants.CHAT_VERSION_PARAMETER);
        if (chatVersion == Constants.INT_PARAMETER_ERROR) {
            return;
        }

        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        int chatManagerVersion = chatManager.getVersion();
        List<MessageEntry> chatEntries = chatManager.getMessages(chatVersion);

        // log and create the response json string
        MessagesAndVersion cav = new MessagesAndVersion(chatEntries, chatManagerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cav);
        log("Server Chat version: " + chatManagerVersion + ", User '" + emailOfLoggedMember.getValue() + "' Chat version: " + chatVersion);
        log(jsonResponse);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    private static class MessagesAndVersion {

        final private List<MessageEntry> entries;
        final private int version;

        public MessagesAndVersion(List<MessageEntry> entries, int version) {
            this.entries = entries;
            this.version = version;
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
