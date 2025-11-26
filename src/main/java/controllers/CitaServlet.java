package controllers;

import dao.CitaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/citas")
public class CitaServlet extends HttpServlet {

    private CitaDAO dao = new CitaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("listaCitas", dao.listar());
        req.getRequestDispatcher("views/citas.jsp").forward(req, resp);
    }
}
