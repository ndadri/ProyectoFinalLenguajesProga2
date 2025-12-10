package controllers;

import dao.UsuarioDAO;
import models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
/**
 * LoginServlet - Controlador de autenticación de usuarios
 *
 * Descripción: Maneja el inicio de sesión de usuarios en el sistema.
 * Valida credenciales, crea sesiones y actualiza el último acceso.
 * Redirige a usuarios ya autenticados al dashboard.
 */
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

        // Usamos trim() para quitar espacios accidentales al inicio/final
        String usuario = request.getParameter("usuario") != null ? request.getParameter("usuario").trim() : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";

        // Validar vacíos usando nuestra clase nueva (más limpio)
        if(utils.Validador.esVacio(usuario) || utils.Validador.esVacio(password)) {
            request.setAttribute("error", "Usuario y contraseña son requeridos");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        Usuario user = usuarioDAO.validarCredenciales(usuario.trim(), password.trim());
        try{

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

        } catch (Exception e){
            System.err.println("Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();

            //Esto es en caso de errores random
            request.setAttribute("error", "Error al procesar el login, intentalo más tarde");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }


    }
}
