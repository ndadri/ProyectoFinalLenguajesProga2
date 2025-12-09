package controllers;

import dao.OdontogramaDAO;
import models.Diente;
import models.Odontograma;
import models.Paciente;
import models.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/odontograma")
public class OdontogramaServlet extends HttpServlet {
    private OdontogramaDAO odontogramaDAO;

    @Override
    public void init() throws ServletException {
        odontogramaDAO = new OdontogramaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
                case "listar":
                    listarOdontogramas(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "ver":
                    verOdontograma(request, response);
                    break;
                case "eliminar":
                    eliminarOdontograma(request, response);
                    break;
                default:
                    listarOdontogramas(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en operación de odontograma", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "crear":
                    crearOdontograma(request, response);
                    break;
                case "actualizar":
                    actualizarOdontograma(request, response);
                    break;
                case "guardar_diente":
                    guardarDiente(request, response);
                    break;
                default:
                    listarOdontogramas(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en operación de odontograma", e);
        }
    }

    private void listarOdontogramas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String pacienteIdParam = request.getParameter("paciente_id");
        List<Odontograma> odontogramas;

        if (usuario.tieneAccesoTotal()) {
            // Admin y Recepción ven TODO
            if (pacienteIdParam != null && !pacienteIdParam.isEmpty()) {
                int pacienteId = Integer.parseInt(pacienteIdParam);
                odontogramas = odontogramaDAO.obtenerPorPaciente(pacienteId);
                request.setAttribute("pacienteIdFiltro", pacienteId);
            } else {
                odontogramas = odontogramaDAO.obtenerTodos();
            }
        } else if (usuario.isOdontologo()) {
            // Odontólogo solo ve odontogramas de SUS pacientes (los que tienen citas con él)
            odontogramas = odontogramaDAO.obtenerPorOdontologo(usuario.getOdontologoId());
        } else {
            odontogramas = new ArrayList<>();
        }

        List<Paciente> pacientes = odontogramaDAO.obtenerPacientes();

        request.setAttribute("odontogramas", odontogramas);
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("/views/odontograma.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        List<Paciente> pacientes = odontogramaDAO.obtenerPacientes();
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("/views/odontograma-form.jsp").forward(request, response);
    }

    private void verOdontograma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int odontogramaId = Integer.parseInt(request.getParameter("id"));

        Odontograma odontograma = odontogramaDAO.obtenerPorId(odontogramaId);
        List<Diente> dientes = odontogramaDAO.obtenerDientes(odontogramaId);

        request.setAttribute("odontograma", odontograma);
        request.setAttribute("dientes", dientes);
        request.getRequestDispatcher("/views/odontograma-detalle.jsp").forward(request, response);
    }

    private void crearOdontograma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int pacienteId = Integer.parseInt(request.getParameter("paciente_id"));
            String observaciones = request.getParameter("observaciones");

            Odontograma odontograma = new Odontograma();
            odontograma.setPacienteId(pacienteId);
            odontograma.setObservaciones(observaciones);

            int odontogramaId = odontogramaDAO.insertar(odontograma);

            if (odontogramaId > 0) {
                // Inicializar los 32 dientes con estado "Sano"
                odontogramaDAO.inicializarDientes(odontogramaId);

                session.setAttribute("mensaje", "Odontograma creado exitosamente");
                session.setAttribute("tipoMensaje", "success");
                response.sendRedirect("odontograma?action=ver&id=" + odontogramaId);
            } else {
                session.setAttribute("mensaje", "Error al crear el odontograma");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("odontograma?action=nuevo");
            }

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("odontograma?action=nuevo");
        }
    }

    private void actualizarOdontograma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int odontogramaId = Integer.parseInt(request.getParameter("odontograma_id"));
            String observaciones = request.getParameter("observaciones");

            Odontograma odontograma = odontogramaDAO.obtenerPorId(odontogramaId);
            odontograma.setObservaciones(observaciones);

            boolean exito = odontogramaDAO.actualizar(odontograma);

            if (exito) {
                session.setAttribute("mensaje", "Odontograma actualizado exitosamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al actualizar el odontograma");
                session.setAttribute("tipoMensaje", "error");
            }

            response.sendRedirect("odontograma?action=ver&id=" + odontogramaId);

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("odontograma?action=listar");
        }
    }

    private void guardarDiente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Log para debugging
            System.out.println("[DEBUG] Guardar diente - Parámetros recibidos:");
            System.out.println("  odontograma_id: " + request.getParameter("odontograma_id"));
            System.out.println("  numero_diente: " + request.getParameter("numero_diente"));
            System.out.println("  estado: " + request.getParameter("estado"));
            System.out.println("  observaciones: " + request.getParameter("observaciones"));

            int odontogramaId = Integer.parseInt(request.getParameter("odontograma_id"));
            String numeroDiente = request.getParameter("numero_diente");
            String estado = request.getParameter("estado");
            String observaciones = request.getParameter("observaciones");

            // Validaciones
            if (numeroDiente == null || numeroDiente.trim().isEmpty()) {
                out.write("{\"success\": false, \"message\": \"Número de diente inválido\"}");
                return;
            }

            if (estado == null || estado.trim().isEmpty()) {
                out.write("{\"success\": false, \"message\": \"Estado inválido\"}");
                return;
            }

            // Verificar si el diente ya existe
            Diente dienteExistente = odontogramaDAO.obtenerDiente(odontogramaId, numeroDiente);

            Diente diente = new Diente();
            diente.setOdontogramaId(odontogramaId);
            diente.setNumeroDiente(numeroDiente);
            diente.setEstado(estado);
            diente.setObservaciones(observaciones != null ? observaciones : "");

            boolean exito;
            String mensaje;

            if (dienteExistente != null) {
                // Actualizar diente existente
                System.out.println("[DEBUG] Actualizando diente existente: " + numeroDiente);
                exito = odontogramaDAO.actualizarDiente(diente);
                mensaje = "Diente actualizado correctamente";
            } else {
                // Insertar nuevo diente
                System.out.println("[DEBUG] Insertando nuevo diente: " + numeroDiente);
                exito = odontogramaDAO.insertarDiente(diente);
                mensaje = "Diente creado correctamente";
            }

            if (exito) {
                System.out.println("[DEBUG] Operación exitosa");
                out.write("{\"success\": true, \"message\": \"" + mensaje + "\"}");
            } else {
                System.out.println("[DEBUG] Operación fallida");
                out.write("{\"success\": false, \"message\": \"Error al guardar el diente en la base de datos\"}");
            }

        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Formato de número inválido: " + e.getMessage());
            out.write("{\"success\": false, \"message\": \"Parámetros numéricos inválidos\"}");
        } catch (SQLException e) {
            System.err.println("[ERROR] Error SQL: " + e.getMessage());
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error de base de datos: " + e.getMessage().replace("\"", "'") + "\"}");
        } catch (Exception e) {
            System.err.println("[ERROR] Error general: " + e.getMessage());
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage().replace("\"", "'") + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    private void eliminarOdontograma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int odontogramaId = Integer.parseInt(request.getParameter("id"));

            boolean exito = odontogramaDAO.eliminar(odontogramaId);

            if (exito) {
                session.setAttribute("mensaje", "Odontograma eliminado exitosamente");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "Error al eliminar el odontograma");
                session.setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("odontograma?action=listar");
    }
}