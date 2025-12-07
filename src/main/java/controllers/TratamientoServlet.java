package controllers;

import dao.TratamientoDAO;
import models.TratamientoOdontologico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/tratamiento")
public class TratamientoServlet extends HttpServlet {

    private TratamientoDAO tratamientoDAO;

    @Override
    public void init() throws ServletException {
        tratamientoDAO = new TratamientoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "eliminar":
                eliminarTratamiento(request, response);
                break;
            default:
                listarTratamientos(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        TratamientoOdontologico t = new TratamientoOdontologico();
        t.setCodigo(request.getParameter("codigo"));
        t.setNombre(request.getParameter("nombre"));
        t.setDescripcion(request.getParameter("descripcion"));
        t.setCategoria(request.getParameter("categoria"));

        // Precio Base
        String precio_baseStr = request.getParameter("precio_base");
        if (precio_baseStr != null && !precio_baseStr.isEmpty()) {
            t.setPrecio_base(new BigDecimal(precio_baseStr));
        } else {
            t.setPrecio_base(BigDecimal.ZERO);
        }

        // Duración
        String duracionStr = request.getParameter("duracion");
        if (duracionStr != null && !duracionStr.isEmpty()) {
            t.setDuracion_aproximada(Integer.parseInt(duracionStr));
        } else {
            t.setDuracion_aproximada(30);
        }

        // Requiere anestesia
        String anestesiaStr = request.getParameter("requiere_anestesia");
        if (anestesiaStr != null && !anestesiaStr.isEmpty()) {
            t.setRequiere_anestesia(Integer.parseInt(anestesiaStr));
        } else {
            t.setRequiere_anestesia(0);
        }

        // Activo
        String activoStr = request.getParameter("activo");
        if (activoStr != null && !activoStr.isEmpty()) {
            t.setActivo(Integer.parseInt(activoStr));
        } else {
            t.setActivo(1);
        }

        boolean exito;
        if (idStr == null || idStr.isEmpty()) {
            exito = tratamientoDAO.insertar(t);
        } else {
            t.setTratamiento_id(Integer.parseInt(idStr));
            exito = tratamientoDAO.actualizar(t);
        }

        if (exito) {
            request.getSession().setAttribute("mensaje", "Tratamiento guardado correctamente");
            request.getSession().setAttribute("tipoMensaje", "success");
        } else {
            request.getSession().setAttribute("mensaje", "Error al guardar el tratamiento");
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("tratamiento?action=listar");
    }

    private void listarTratamientos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TratamientoOdontologico> lista = tratamientoDAO.obtenerTodos();
        request.setAttribute("tratamientos", lista);
        request.getRequestDispatcher("/views/tratamiento.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/tratamiento-form.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        TratamientoOdontologico t = tratamientoDAO.obtenerPorId(id);
        request.setAttribute("tratamiento", t);
        request.getRequestDispatcher("/views/tratamiento-form.jsp").forward(request, response);
    }

    private void eliminarTratamiento(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        tratamientoDAO.eliminar(id);
        request.getSession().setAttribute("mensaje", "Tratamiento eliminado");
        request.getSession().setAttribute("tipoMensaje", "success");
        response.sendRedirect("tratamiento?action=listar");
    }
}