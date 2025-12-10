package controllers;

import dao.PacienteDAO;
import models.Paciente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.Validador;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
/**
 * PacienteServlet - Controlador para la gestión de pacientes
 *
 * Descripción: Maneja el registro completo de pacientes incluyendo datos
 * personales, información médica, historial de alergias y enfermedades.
 * Calcula automáticamente la edad basándose en la fecha de nacimiento.
 * Implementa búsqueda por múltiples criterios.
 */
@WebServlet("/paciente")
public class PacienteServlet extends HttpServlet {
    private PacienteDAO pacienteDAO;

    @Override
    public void init() throws ServletException {
        pacienteDAO = new PacienteDAO();
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
                listarPacientes(request, response);
                break;
            case "test":
                testListar(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "eliminar":
                eliminarPaciente(request, response);
                break;
            case "buscar":
                buscarPacientes(request, response);
                break;
            default:
                listarPacientes(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            guardarPaciente(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarPaciente(request, response);
        }
    }

    // Test de listado
    private void testListar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Paciente> pacientes = pacienteDAO.obtenerTodos();
        System.out.println("TEST: Total pacientes = " + pacientes.size());
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("/views/test-pacientes.jsp").forward(request, response);
    }

    // Listar todos los pacientes
    private void listarPacientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Paciente> pacientes = pacienteDAO.obtenerTodos();

            // Debug - imprimir en consola
            System.out.println("=== DEBUG LISTAR PACIENTES ===");
            System.out.println("Total pacientes obtenidos: " + pacientes.size());

            if (!pacientes.isEmpty()) {
                System.out.println("Primer paciente: " + pacientes.get(0).getNombres() + " " + pacientes.get(0).getApellidos());
            }

            request.setAttribute("pacientes", pacientes);
            request.getRequestDispatcher("/views/paciente.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar pacientes: " + e.getMessage());
            request.getRequestDispatcher("/views/paciente.jsp").forward(request, response);
        }
    }

    // Mostrar formulario para nuevo paciente
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/paciente-form.jsp").forward(request, response);
    }

    // Mostrar formulario para editar paciente
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Paciente paciente = pacienteDAO.obtenerPorId(id);

        if (paciente != null) {
            request.setAttribute("paciente", paciente);
            request.getRequestDispatcher("/views/paciente-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("paciente?action=listar");
        }
    }

    // Guardar nuevo paciente con VALIDACIÓN
    private void guardarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Paciente paciente = extraerDatosFormulario(request);

            // 1. VALIDACIÓN BACKEND (Nivel Pepa)
            String errorValidacion = Validador.validarDatosPaciente(paciente);

            if (errorValidacion != null) {
                // Si hay error, no guardamos. Devolvemos el error.
                request.getSession().setAttribute("mensaje", "Error: " + errorValidacion);
                request.getSession().setAttribute("tipoMensaje", "error");
                // Redirigimos al formulario nuevo para que intente otra vez
                // (Ojo: idealmente usarías forward para no borrar los datos, pero mantendré tu estructura de redirect)
                response.sendRedirect("paciente?action=nuevo");
                return;
            }

            // 2. Si pasa la validación, intentamos guardar
            boolean exito = pacienteDAO.insertar(paciente);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Paciente registrado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al registrar paciente");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de duplicados SQL (por si acaso falle la validación lógica)
            String mensajeError = e.getMessage();
            if (mensajeError != null && mensajeError.contains("Duplicate entry")) {
                request.getSession().setAttribute("mensaje", "Ya existe un paciente con esa cédula.");
            } else {
                request.getSession().setAttribute("mensaje", "Error crítico: " + mensajeError);
            }
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("paciente?action=listar");
    }

    // Actualizar paciente con VALIDACIÓN
    private void actualizarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("paciente_id"));
            Paciente paciente = extraerDatosFormulario(request);
            paciente.setPacienteId(id);

            // 1. VALIDACIÓN BACKEND
            String errorValidacion = Validador.validarDatosPaciente(paciente);

            if (errorValidacion != null) {
                request.getSession().setAttribute("mensaje", "Error: " + errorValidacion);
                request.getSession().setAttribute("tipoMensaje", "error");
                // Volvemos al editar con el ID
                response.sendRedirect("paciente?action=editar&id=" + id);
                return;
            }

            // 2. Guardar
            boolean exito = pacienteDAO.actualizar(paciente);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Paciente actualizado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al actualizar paciente");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("paciente?action=listar");
    }

    // Eliminar paciente (eliminación lógica)
    private void eliminarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = pacienteDAO.eliminar(id);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Paciente eliminado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al eliminar paciente");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("paciente?action=listar");
    }

    // Buscar pacientes
    private void buscarPacientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String termino = request.getParameter("termino");
        List<Paciente> pacientes;

        if (termino != null && !termino.trim().isEmpty()) {
            pacientes = pacienteDAO.buscar(termino);
        } else {
            pacientes = pacienteDAO.obtenerTodos();
        }

        request.setAttribute("pacientes", pacientes);
        request.setAttribute("termino", termino);
        request.getRequestDispatcher("/views/paciente.jsp").forward(request, response);
    }

    // Método auxiliar para extraer datos del formulario
    private Paciente extraerDatosFormulario(HttpServletRequest request) {
        Paciente paciente = new Paciente();

        // Datos básicos
        paciente.setNombres(request.getParameter("nombres"));
        paciente.setApellidos(request.getParameter("apellidos"));
        paciente.setCedula(request.getParameter("cedula"));

        // Fecha de nacimiento y edad
        String fechaNacStr = request.getParameter("fecha_nacimiento");
        if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
            Date fechaNac = Date.valueOf(fechaNacStr);
            paciente.setFechaNacimiento(fechaNac);

            // Calcular edad
            LocalDate fechaNacimiento = fechaNac.toLocalDate();
            LocalDate ahora = LocalDate.now();
            int edad = Period.between(fechaNacimiento, ahora).getYears();
            paciente.setEdad(edad);
        }

        paciente.setGenero(request.getParameter("genero"));
        paciente.setTelefono(request.getParameter("telefono"));
        paciente.setEmail(request.getParameter("email"));
        paciente.setDireccion(request.getParameter("direccion"));
        paciente.setOcupacion(request.getParameter("ocupacion"));

        // Contacto de emergencia
        paciente.setContactoEmergencia(request.getParameter("contacto_emergencia"));
        paciente.setTelefonoEmergencia(request.getParameter("telefono_emergencia"));

        // Historial médico
        paciente.setAlergias(request.getParameter("alergias"));
        paciente.setEnfermedadesSistemicas(request.getParameter("enfermedades_sistemicas"));
        paciente.setMedicamentosActuales(request.getParameter("medicamentos_actuales"));

        // Embarazada (solo si es mujer)
        String embarazada = request.getParameter("embarazada");
        if (embarazada != null && !embarazada.isEmpty()) {
            paciente.setEmbarazada(Boolean.parseBoolean(embarazada));
        }

        paciente.setNotas(request.getParameter("notas"));

        return paciente;
    }
}