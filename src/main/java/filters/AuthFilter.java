package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("usuario") != null;

        String loginURI = httpRequest.getContextPath() + "/login";
        String requestURI = httpRequest.getRequestURI();

        boolean isLoginRequest = requestURI.equals(loginURI);
        boolean isLoginPage = requestURI.endsWith("login.jsp");
        boolean isPublicResource = requestURI.contains("/estilos/") ||
                requestURI.contains("/js/") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".js");

        if (loggedIn || isLoginRequest || isLoginPage || isPublicResource) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
        // Limpieza del filtro
    }
}