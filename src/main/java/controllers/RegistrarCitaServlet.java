package controllers;

import dao.CitaDAO;
import models.Cita;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/registrarCita")
public class RegistrarCitaServlet extends HttpServlet {

    private CitaDAO dao = new CitaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("views/registrarCita.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Cita c = new Cita();
        c.setPacienteId(Integer.parseInt(req.getParameter("paciente_id")));
        c.setMedicoId(Integer.parseInt(req.getParameter("medico_id"))); // Agregado
        c.setFechaHora(req.getParameter("fecha_hora")); // Corregido
        c.setMotivo(req.getParameter("motivo"));
        c.setEstado("Pendiente"); // Valor por defecto

        dao.guardar(c);

        resp.sendRedirect("citas");
    }
}