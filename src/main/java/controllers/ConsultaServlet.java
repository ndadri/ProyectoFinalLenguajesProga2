package controllers;

import dao.ConsultaDAO;
import models.Consulta;
import models.Paciente;
import models.Odontologo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/consulta")
public class ConsultaServlet extends HttpServlet {
    private ConsultaDAO consultaDAO;

    @Override
    public void init() throws ServletException {
        consultaDAO = new ConsultaDAO();
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
                listarConsultas(request, response);
                break;
            case "historial":
                verHistorial(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "ver":
                verConsulta(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "eliminar":
                eliminarConsulta(request, response);
                break;
            default:
                listarConsultas(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            guardarConsulta(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarConsulta(request, response);
        }
    }

    // Listar todas las consultas
    private void listarConsultas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Consulta> consultas = consultaDAO.obtenerTodos();
            request.setAttribute("consultas", consultas);
            request.getRequestDispatcher("/views/consulta.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar consultas: " + e.getMessage());
            request.getRequestDispatcher("/views/consulta.jsp").forward(request, response);
        }
    }

    // Ver historial de un paciente
    private void verHistorial(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int pacienteId = Integer.parseInt(request.getParameter("paciente_id"));
            List<Consulta> consultas = consultaDAO.obtenerPorPaciente(pacienteId);

            request.setAttribute("consultas", consultas);
            request.setAttribute("historialPaciente", true);
            request.getRequestDispatcher("/views/consulta.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar historial: " + e.getMessage());
            request.getRequestDispatcher("/views/consulta.jsp").forward(request, response);
        }
    }

    // Mostrar formulario para nueva consulta
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Paciente> pacientes = consultaDAO.obtenerPacientes();
        List<Odontologo> odontologos = consultaDAO.obtenerOdontologos();

        request.setAttribute("pacientes", pacientes);
        request.setAttribute("odontologos", odontologos);
        request.getRequestDispatcher("/views/consulta-form.jsp").forward(request, response);
    }

    // Ver detalles de una consulta
    private void verConsulta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Consulta consulta = consultaDAO.obtenerPorId(id);

            if (consulta != null) {
                request.setAttribute("consulta", consulta);
                request.setAttribute("soloVer", true);
                request.getRequestDispatcher("/views/consulta-detalle.jsp").forward(request, response);
            } else {
                response.sendRedirect("consulta?action=listar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("consulta?action=listar");
        }
    }

    // Mostrar formulario para editar consulta
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Consulta consulta = consultaDAO.obtenerPorId(id);
        List<Paciente> pacientes = consultaDAO.obtenerPacientes();
        List<Odontologo> odontologos = consultaDAO.obtenerOdontologos();

        if (consulta != null) {
            request.setAttribute("consulta", consulta);
            request.setAttribute("pacientes", pacientes);
            request.setAttribute("odontologos", odontologos);
            request.getRequestDispatcher("/views/consulta-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("consulta?action=listar");
        }
    }

    // Guardar nueva consulta
    private void guardarConsulta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Consulta consulta = extraerDatosFormulario(request);

            boolean exito = consultaDAO.insertar(consulta);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Consulta registrada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al registrar consulta");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("consulta?action=listar");
    }

    // Actualizar consulta existente
    private void actualizarConsulta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("consulta_id"));
            Consulta consulta = extraerDatosFormulario(request);
            consulta.setConsultaId(id);

            boolean exito = consultaDAO.actualizar(consulta);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Consulta actualizada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al actualizar consulta");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("consulta?action=listar");
    }

    // Eliminar consulta
    private void eliminarConsulta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = consultaDAO.eliminar(id);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Consulta eliminada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al eliminar consulta");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("consulta?action=listar");
    }

    // Método auxiliar para extraer datos del formulario
    private Consulta extraerDatosFormulario(HttpServletRequest request) {
        Consulta consulta = new Consulta();

        consulta.setPacienteId(Integer.parseInt(request.getParameter("paciente_id")));
        consulta.setOdontologoId(Integer.parseInt(request.getParameter("odontologo_id")));
        consulta.setMotivoConsulta(request.getParameter("motivo_consulta"));
        consulta.setSintomas(request.getParameter("sintomas"));
        consulta.setDiagnostico(request.getParameter("diagnostico"));
        consulta.setTratamiento(request.getParameter("tratamiento"));
        consulta.setDientesTratados(request.getParameter("dientes_tratados"));
        consulta.setProcedimientos(request.getParameter("procedimientos"));
        consulta.setObservaciones(request.getParameter("observaciones"));
        consulta.setPronostico(request.getParameter("pronostico"));

        String proximaCitaStr = request.getParameter("proxima_cita");
        if (proximaCitaStr != null && !proximaCitaStr.isEmpty()) {
            consulta.setProximaCita(Date.valueOf(proximaCitaStr));
        }

        String requiereSeguimiento = request.getParameter("requiere_seguimiento");
        consulta.setRequiereSeguimiento("on".equals(requiereSeguimiento) || "true".equals(requiereSeguimiento));

        return consulta;
    }
}