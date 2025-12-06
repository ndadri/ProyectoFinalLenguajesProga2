package controllers;

import dao.PacienteDAO;
import dao.CitaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login");
            return;
        }

        // Obtener estadísticas
        try {
            PacienteDAO pacienteDAO = new PacienteDAO();
            CitaDAO citaDAO = new CitaDAO();

            int totalPacientes = pacienteDAO.contarPacientes();
            int citasHoy = citaDAO.contarCitasHoy();
            int citasPendientes = citaDAO.contarCitasPendientes();

            request.setAttribute("totalPacientes", totalPacientes);
            request.setAttribute("citasHoy", citasHoy);
            request.setAttribute("citasPendientes", citasPendientes);

            // Obtener citas recientes
            request.setAttribute("citasRecientes", citaDAO.obtenerCitasRecientes(5));

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}