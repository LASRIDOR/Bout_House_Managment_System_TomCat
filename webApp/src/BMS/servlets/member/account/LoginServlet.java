package BMS.servlets.member.account;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.managment.CEO.BoutHouseManager;
import BMS.managment.userManager.LoggedAlreadyException;
import BMS.managment.utils.exceptions.ExistingException;
import BMS.managment.utils.exceptions.WrongCredentialException;
import BMS.utils.ServletUtils;
import BMS.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final String MEMBER_WEEK_RESERVATION = "home.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        InfoField<String> emailFromParameter = SessionUtils.getEmail(request);
        BoutHouseManager boutHouseManager = ServletUtils.getBoutHouseManager(getServletContext());

        if (emailFromParameter != null) {
            response.sendRedirect(MEMBER_WEEK_RESERVATION);
        }
        else{
            try (PrintWriter out = response.getWriter()) {
                out.println("<html lang=\"en\">");
                out.println("<head>");
                out.println("<title>Bout House Management System</title>");
                out.println("<link rel=\"canonical\" href=\"https://getbootstrap.com/docs/5.0/examples/sign-in/\">");
                out.println("<link href=\"assets/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
                out.println("<link href=\"assets/dist/css/signin.css\" rel=\"stylesheet\">");
                out.println("</head>");
                out.println("<body class=\"text-center\">");

                try {
                    InfoField<String> emailToConnect = ServletUtils.getInfoField(request, MemberFieldType.EMAIL);
                    InfoField<String> passwordToConnect = ServletUtils.getInfoField(request, MemberFieldType.PASSWORD);

                    if (emailToConnect == null || emailToConnect.getValue().trim().isEmpty() || passwordToConnect == null || passwordToConnect.getValue().trim().isEmpty()) {
                        //no username in session and no username in parameter -
                        writeLoginFormHTMLCode(out, "", "", "Email or Password are missing");
                    } else {
                        try {
                            boutHouseManager.loginToMemberManager(emailFromParameter, emailToConnect, passwordToConnect);
                            request.getSession(true).setAttribute(MemberFieldType.EMAIL.getNameOfField(), emailToConnect);
                            response.sendRedirect(MEMBER_WEEK_RESERVATION);
                        }catch (WrongCredentialException | ExistingException | LoggedAlreadyException LoginException){
                            writeLoginFormHTMLCode(out, emailToConnect.getValue(),  passwordToConnect.getValue(), LoginException.getMessage());
                            log(LoginException.getMessage());
                        }
                    }
                } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException wrongInputException) {
                    writeLoginFormHTMLCode(out, "", "", wrongInputException.getMessage());
                    log(wrongInputException.getMessage());
                }


                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    private void writeLoginFormHTMLCode(PrintWriter out, String email, String password, String errorMessage) {
        out.println("<main class=\"form-signin\">");
        out.println("<form method=\"post\" id=\"login form\" name=\"login form\">");
        out.println("<img class=\"mb-4\" src=\"assets/brand/boutLogo.png\" alt=\"\" width=\"173\" height=\"128\">");
        out.println("<h1 class=\"h3 mb-3 fw-normal\">Please sign in</h1>");
        out.println("<label for=\"inputEmail\" class=\"visually-hidden\">Email address</label>");
        out.println("<input value= '"+ email +"' name=\"Email\" type=\"Email\" id=\"inputEmail\" class=\"form-control\" placeholder=\"Email address\" required autofocus>\n");
        out.println("<label for=\"inputPassword\" class=\"visually-hidden\">Password</label>\n");
        out.println("<input value= '"+ password +"' name=\"Password\" type=\"Password\" id=\"inputPassword\" class=\"form-control\" placeholder=\"Password\" required>");
        out.println("<button class=\"w-100 btn btn-lg btn-primary\" type=\"submit\">Sign in</button>");
        out.println("<div id=\"error message\">'"+ errorMessage +"'</div>\n");
        out.println("<p class=\"mt-5 mb-3 text-muted\">&copy; 2020-2021</p>\n");
        out.println("</form>");
        out.println("</main>");
    }

}
