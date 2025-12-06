package controllers;

import dao.OdontologoDAO;
import models.Odontologo;
import models.Especialidad;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/odontologo")
public class OdontologoServlet extends HttpServlet {
    private OdontologoDAO odontologoDAO;

    @Override
    public void init() throws ServletException {
        odontologoDAO = new OdontologoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "listar":
                listarOdontologos(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "eliminar":
                eliminarOdontologo(request, response);
                break;
            default:
                listarOdontologos(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            guardarOdontologo(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarOdontologo(request, response);
        }
    }

    // Listar todos los odontólogos
    private void listarOdontologos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Odontologo> odontologos = odontologoDAO.obtenerTodos();
            request.setAttribute("odontologos", odontologos);
            request.getRequestDispatcher("/views/odontologo.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar odontólogos: " + e.getMessage());
            request.getRequestDispatcher("/views/odontologo.jsp").forward(request, response);
        }
    }

    // Mostrar formulario para nuevo odontólogo
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Especialidad> especialidades = odontologoDAO.obtenerEspecialidades();
        request.setAttribute("especialidades", especialidades);
        request.getRequestDispatcher("/views/odontologo-form.jsp").forward(request, response);
    }

    // Mostrar formulario para editar odontólogo
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Odontologo odontologo = odontologoDAO.obtenerPorId(id);
        List<Especialidad> especialidades = odontologoDAO.obtenerEspecialidades();

        if (odontologo != null) {
            request.setAttribute("odontologo", odontologo);
            request.setAttribute("especialidades", especialidades);
            request.getRequestDispatcher("/views/odontologo-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("odontologo?action=listar");
        }
    }

    // Guardar nuevo odontólogo
    private void guardarOdontologo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Odontologo odontologo = extraerDatosFormulario(request);

            boolean exito = odontologoDAO.insertar(odontologo);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Odontólogo registrado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al registrar odontólogo");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();

            String mensajeError = e.getMessage();

            if (mensajeError != null && mensajeError.contains("Duplicate entry") && mensajeError.contains("cedula")) {
                request.getSession().setAttribute("mensaje", "Ya existe un odontólogo con esta cédula");
                request.getSession().setAttribute("tipoMensaje", "error");
            } else {
                request.getSession().setAttribute("mensaje", "Error: " + mensajeError);
                request.getSession().setAttribute("tipoMensaje", "error");
            }
        }

        response.sendRedirect("odontologo?action=listar");
    }

    // Actualizar odontólogo existente
    private void actualizarOdontologo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("odontologo_id"));
            Odontologo odontologo = extraerDatosFormulario(request);
            odontologo.setOdontologoId(id);

            boolean exito = odontologoDAO.actualizar(odontologo);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Odontólogo actualizado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al actualizar odontólogo");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("odontologo?action=listar");
    }

    // Eliminar odontólogo
    private void eliminarOdontologo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = odontologoDAO.eliminar(id);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Odontólogo eliminado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al eliminar odontólogo");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("odontologo?action=listar");
    }

    // Método auxiliar para extraer datos del formulario
    private Odontologo extraerDatosFormulario(HttpServletRequest request) {
        Odontologo odontologo = new Odontologo();

        odontologo.setEspecialidadId(Integer.parseInt(request.getParameter("especialidad_id")));
        odontologo.setNombres(request.getParameter("nombres"));
        odontologo.setApellidos(request.getParameter("apellidos"));
        odontologo.setCedula(request.getParameter("cedula"));
        odontologo.setNumRegistro(request.getParameter("num_registro"));
        odontologo.setTelefono(request.getParameter("telefono"));
        odontologo.setCelular(request.getParameter("celular"));
        odontologo.setEmail(request.getParameter("email"));
        odontologo.setDireccion(request.getParameter("direccion"));

        String fechaNacStr = request.getParameter("fecha_nacimiento");
        if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
            odontologo.setFechaNacimiento(Date.valueOf(fechaNacStr));
        }

        odontologo.setGenero(request.getParameter("genero"));

        return odontologo;
    }
}