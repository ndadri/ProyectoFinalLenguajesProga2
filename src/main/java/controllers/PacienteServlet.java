package controllers;

import dao.PacienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/pacientes")
public class PacienteServlet extends HttpServlet {

    private PacienteDAO dao = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("listaPacientes", dao.listar());
        req.getRequestDispatcher("views/paciente.jsp").forward(req, resp);
    }
}
