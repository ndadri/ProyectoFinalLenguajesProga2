package controllers;

import dao.PacienteDAO;
import models.Paciente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/registrarPaciente")
public class RegistrarPacienteServlet extends HttpServlet {

    private PacienteDAO dao = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("views/registrarPaciente.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Paciente p = new Paciente();
        p.setNombre(req.getParameter("nombre"));
        p.setApellido(req.getParameter("apellido"));
        p.setTelefono(req.getParameter("telefono"));
        p.setEmail(req.getParameter("email"));
        dao.guardar(p);

        resp.sendRedirect("pacientes");
    }
}
