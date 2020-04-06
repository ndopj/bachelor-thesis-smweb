package cz.sm.web.prototype.javaeejsf.security;

import javax.annotation.security.DeclareRoles;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet is only used to register user roles.
 * Application will still work even without this servlet,
 * but redirection after login/out may fail in some cases.
 */
@WebServlet("/servlet")
@DeclareRoles({ "PILOT", "ADMIN", "GENERAL" })
public class SecuredServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
    }

}
