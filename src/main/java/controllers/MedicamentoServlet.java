package controllers;

import dao.MedicamentoDAO;
import models.Medicamento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/medicamento")
public class MedicamentoServlet extends HttpServlet {

    private MedicamentoDAO medicamentoDAO;

    @Override
    public void init() throws ServletException {
        medicamentoDAO = new MedicamentoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "listar":
            default:
                listarMedicamentos(req, resp);
                break;
        }
    }

    private void listarMedicamentos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Medicamento> lista = medicamentoDAO.obtenerTodos();
        req.setAttribute("medicamentos", lista);
        req.getRequestDispatcher("/views/medicamento.jsp").forward(req, resp);
    }
}