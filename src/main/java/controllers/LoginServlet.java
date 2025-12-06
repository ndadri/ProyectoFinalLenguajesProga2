package controllers;

import dao.UsuarioDAO;
import models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si ya está logueado, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect("dashboard");
            return;
        }

        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        // Validar credenciales
        Usuario user = usuarioDAO.validarCredenciales(usuario, password);

        if (user != null && user.isActivo()) {
            // Login exitoso
            HttpSession session = request.getSession();
            session.setAttribute("usuario", user);
            session.setMaxInactiveInterval(3600); // 1 hora

            // Actualizar último acceso
            usuarioDAO.actualizarUltimoAcceso(user.getUsuarioId());

            response.sendRedirect("dashboard");
        } else {
            // Login fallido
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}

@WebServlet("/logout")
class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("login");
    }
}