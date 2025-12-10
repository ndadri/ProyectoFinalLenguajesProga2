package controllers;

import dao.CitaDAO;
import models.Cita;
import models.Paciente;
import models.Odontologo;
import models.Usuario;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
/**
 * CitaServlet - Controlador para la gestión de citas odontológicas
 *
 * Descripción: Maneja todas las operaciones CRUD de citas médicas, incluyendo
 * la creación, edición, confirmación, cancelación y eliminación de citas.
 * Implementa control de acceso basado en roles (Admin, Recepción, Odontólogo).
 */
@WebServlet("/cita")
public class CitaServlet extends HttpServlet {
    private CitaDAO citaDAO;

    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
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
                listarCitas(request, response);
                break;
            case "hoy":
                listarCitasHoy(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "confirmar":
                confirmarCita(request, response);
                break;
            case "cancelar":
                cancelarCita(request, response);
                break;
            case "completar":
                completarCita(request, response);
                break;
            case "eliminar":
                eliminarCita(request, response);
                break;
            default:
                listarCitas(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            guardarCita(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarCita(request, response);
        }
    }

    // Listar todas las citas
    // Listar todas las citas
    private void listarCitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            String estadoFiltro = request.getParameter("estado");
            List<Cita> citas;

            // FILTRAR SEGÚN EL ROL
            if (usuario.tieneAccesoTotal()) {
                // Admin y Recepción ven TODAS las citas
                if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
                    citas = citaDAO.obtenerPorEstado(estadoFiltro);
                } else {
                    citas = citaDAO.obtenerTodos();
                }
            } else if (usuario.isOdontologo()) {
                // Odontólogo solo ve SUS citas
                if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
                    // Filtrar SUS citas por estado
                    citas = citaDAO.obtenerPorOdontologo(usuario.getOdontologoId());
                    List<Cita> citasFiltradas = new ArrayList<>();
                    for (Cita c : citas) {
                        if (c.getEstado().equals(estadoFiltro)) {
                            citasFiltradas.add(c);
                        }
                    }
                    citas = citasFiltradas;
                } else {
                    citas = citaDAO.obtenerPorOdontologo(usuario.getOdontologoId());
                }
            } else {
                // Rol desconocido - sin acceso
                citas = new ArrayList<>();
            }

            request.setAttribute("citas", citas);
            request.setAttribute("estadoFiltro", estadoFiltro);
            request.getRequestDispatcher("/views/cita.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar citas: " + e.getMessage());
            request.getRequestDispatcher("/views/cita.jsp").forward(request, response);
        }
    }

    // Listar citas de hoy
    private void listarCitasHoy(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Cita> citas = citaDAO.obtenerCitasHoy();
            request.setAttribute("citas", citas);
            request.setAttribute("soloHoy", true);
            request.getRequestDispatcher("/views/cita.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar citas: " + e.getMessage());
            request.getRequestDispatcher("/views/cita.jsp").forward(request, response);
        }
    }

    // Mostrar formulario para nueva cita
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Paciente> pacientes = citaDAO.obtenerPacientes();
        List<Odontologo> odontologos = citaDAO.obtenerOdontologos();

        // Si es odontólogo, pre-seleccionar su ID y deshabilitar el selector
        if (usuario.isOdontologo()) {
            request.setAttribute("odontologoPreseleccionado", usuario.getOdontologoId());
            request.setAttribute("soloLectura", true);
        }

        request.setAttribute("pacientes", pacientes);
        request.setAttribute("odontologos", odontologos);
        request.getRequestDispatcher("/views/cita-form.jsp").forward(request, response);
    }

    // Mostrar formulario para editar cita
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Cita cita = citaDAO.obtenerPorId(id);
        List<Paciente> pacientes = citaDAO.obtenerPacientes();
        List<Odontologo> odontologos = citaDAO.obtenerOdontologos();

        if (cita != null) {
            request.setAttribute("cita", cita);
            request.setAttribute("pacientes", pacientes);
            request.setAttribute("odontologos", odontologos);
            request.getRequestDispatcher("/views/cita-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("cita?action=listar");
        }
    }

    // Guardar nueva cita con VALIDACIÓN DE FECHA
    private void guardarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Cita cita = extraerDatosFormulario(request);

            // --- VALIDACIÓN DE FECHA ---
            Timestamp ahora = new Timestamp(System.currentTimeMillis());

            // Si la cita es antes de "ahora", error.
            if (cita.getFechaHora().before(ahora)) {
                request.getSession().setAttribute("mensaje", "Error: No puedes agendar citas en el pasado.");
                request.getSession().setAttribute("tipoMensaje", "error");
                response.sendRedirect("cita?action=nuevo");
                return;
            }

            if (cita.getDuracionMinutos() <= 0) {
                request.getSession().setAttribute("mensaje", "Error: La duración debe ser mayor a 0.");
                request.getSession().setAttribute("tipoMensaje", "error");
                response.sendRedirect("cita?action=nuevo");
                return;
            }
            // ---------------------------

            boolean exito = citaDAO.insertar(cita);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Cita agendada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al agendar cita");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("cita?action=listar");
    }

    // Actualizar cita existente
    private void actualizarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("cita_id"));
            Cita cita = extraerDatosFormulario(request);
            cita.setCitaId(id);

            boolean exito = citaDAO.actualizar(cita);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Cita actualizada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al actualizar cita");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("cita?action=listar");
    }

    // Confirmar cita
    private void confirmarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = citaDAO.cambiarEstado(id, "Confirmada");

            if (exito) {
                request.getSession().setAttribute("mensaje", "Cita confirmada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al confirmar cita");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("cita?action=listar");
    }

    // Cancelar cita
    private void cancelarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = citaDAO.cambiarEstado(id, "Cancelada");

            if (exito) {
                request.getSession().setAttribute("mensaje", "Cita cancelada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al cancelar cita");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("cita?action=listar");
    }

    // Completar cita
    private void completarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = citaDAO.cambiarEstado(id, "Completada");

            if (exito) {
                request.getSession().setAttribute("mensaje", "Cita marcada como completada");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al completar cita");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("cita?action=listar");
    }

    // Eliminar cita
    private void eliminarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = citaDAO.eliminar(id);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Cita eliminada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al eliminar cita");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("cita?action=listar");
    }

    // Método auxiliar para extraer datos del formulario
    private Cita extraerDatosFormulario(HttpServletRequest request) {
        Cita cita = new Cita();

        cita.setPacienteId(Integer.parseInt(request.getParameter("paciente_id")));
        cita.setOdontologoId(Integer.parseInt(request.getParameter("odontologo_id")));

        // Combinar fecha y hora
        String fecha = request.getParameter("fecha");
        String hora = request.getParameter("hora");
        String fechaHoraStr = fecha + " " + hora + ":00";
        cita.setFechaHora(Timestamp.valueOf(fechaHoraStr));

        cita.setDuracionMinutos(Integer.parseInt(request.getParameter("duracion_minutos")));
        cita.setMotivo(request.getParameter("motivo"));
        cita.setTipoCita(request.getParameter("tipo_cita"));
        cita.setEstado(request.getParameter("estado"));
        cita.setConsultorio(request.getParameter("consultorio"));
        cita.setObservaciones(request.getParameter("observaciones"));

        return cita;
    }
}