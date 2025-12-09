package controllers;

import dao.UsuarioDAO;
import models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/configuracion")
public class ConfiguracionServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/views/configuracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("cambiar_password".equals(action)) {
            cambiarPassword(request, response);
        } else if ("actualizar_perfil".equals(action)) {
            actualizarPerfil(request, response);
        }
    }

    private void cambiarPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            String passwordActual = request.getParameter("password_actual");
            String passwordNueva = request.getParameter("password_nueva");
            String passwordConfirmar = request.getParameter("password_confirmar");

            // Validar que las nuevas contraseñas coincidan
            if (!passwordNueva.equals(passwordConfirmar)) {
                request.setAttribute("mensaje", "Las contraseñas nuevas no coinciden");
                request.setAttribute("tipoMensaje", "error");
                request.getRequestDispatcher("/views/configuracion.jsp").forward(request, response);
                return;
            }

            // Verificar contraseña actual
            Usuario userVerify = usuarioDAO.validarCredenciales(usuario.getUsuario(), passwordActual);

            if (userVerify == null) {
                request.setAttribute("mensaje", "La contraseña actual es incorrecta");
                request.setAttribute("tipoMensaje", "error");
                request.getRequestDispatcher("/views/configuracion.jsp").forward(request, response);
                return;
            }

            // Cambiar contraseña
            boolean exito = usuarioDAO.cambiarPassword(usuario.getUsuarioId(), passwordNueva);

            if (exito) {
                request.setAttribute("mensaje", "Contraseña actualizada exitosamente");
                request.setAttribute("tipoMensaje", "success");
            } else {
                request.setAttribute("mensaje", "Error al cambiar la contraseña");
                request.setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error: " + e.getMessage());
            request.setAttribute("tipoMensaje", "error");
        }

        request.getRequestDispatcher("/views/configuracion.jsp").forward(request, response);
    }

    private void actualizarPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            String email = request.getParameter("email");

            // Actualizar email
            boolean exito = usuarioDAO.actualizarEmail(usuario.getUsuarioId(), email);

            if (exito) {
                // Actualizar en sesión
                usuario.setEmail(email);
                session.setAttribute("usuario", usuario);

                request.setAttribute("mensaje", "Perfil actualizado exitosamente");
                request.setAttribute("tipoMensaje", "success");
            } else {
                request.setAttribute("mensaje", "Error al actualizar el perfil");
                request.setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error: " + e.getMessage());
            request.setAttribute("tipoMensaje", "error");
        }

        request.getRequestDispatcher("/views/configuracion.jsp").forward(request, response);
    }
}