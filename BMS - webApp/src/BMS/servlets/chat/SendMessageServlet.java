package BMS.servlets.chat;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.constants.Constants;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;
import chat.ChatManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SendMessageServlet", urlPatterns = {"/sendMessage"})
public class SendMessageServlet extends HttpServlet {
    private static final String SIGN_UP_URL = "/webApp/login";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InfoField<String> emailOfLoggedMember = SessionUtils.getEmail(req);

        if (emailOfLoggedMember == null) {
            resp.sendRedirect(SIGN_UP_URL);
            return;
        }

        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        String message = req.getParameter(Constants.MESSAGE_CONTENT_PARAMETER);
        if (message != null && !message.trim().isEmpty()) {
            System.out.println("Adding chat string from " + emailOfLoggedMember.getValue() + ": " + message);
            chatManager.addMessage(message, emailOfLoggedMember.getValue());
        }
    }
}
