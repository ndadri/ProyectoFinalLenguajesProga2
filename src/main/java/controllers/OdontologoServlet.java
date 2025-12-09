package controllers;

import dao.OdontologoDAO;
import models.Odontologo;
import models.Especialidad;
import dao.UsuarioDAO;
import models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
/**
 * OdontologoServlet - Controlador para la gestión de odontólogos
 *
 * Descripción: Maneja el registro completo de odontólogos incluyendo sus
 * datos personales, profesionales y credenciales de acceso al sistema.
 * Crea automáticamente un usuario vinculado para que el odontólogo pueda
 * iniciar sesión en el sistema.
 */
@WebServlet("/odontologo")
public class OdontologoServlet extends HttpServlet {
    private OdontologoDAO odontologoDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        odontologoDAO = new OdontologoDAO();
        usuarioDAO = new UsuarioDAO();
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

    // Guardar nuevo odontólogo CON CREDENCIALES
    private void guardarOdontologo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // 1. PRIMERO CREAR EL USUARIO
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            // Validar que el usuario no exista
            if (usuarioDAO.existeUsuario(usuario)) {
                session.setAttribute("mensaje", "El nombre de usuario '" + usuario + "' ya existe. Por favor, elige otro.");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("odontologo?action=nuevo");
                return;
            }

            // Crear usuario con tipo_id = 2 (Odontólogo)
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setTipoId(2); // Tipo Odontólogo
            nuevoUsuario.setUsuario(usuario);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setActivo(true);

            System.out.println("[DEBUG] Creando usuario: " + usuario);
            boolean usuarioCreado = usuarioDAO.crear(nuevoUsuario);

            if (!usuarioCreado) {
                session.setAttribute("mensaje", "Error al crear las credenciales de acceso");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("odontologo?action=nuevo");
                return;
            }

            // Obtener el ID del usuario recién creado
            Usuario usuarioCreado2 = usuarioDAO.obtenerPorNombre(usuario);
            if (usuarioCreado2 == null) {
                session.setAttribute("mensaje", "Error: No se pudo obtener el usuario creado");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("odontologo?action=nuevo");
                return;
            }

            int usuarioId = usuarioCreado2.getUsuarioId();
            System.out.println("[DEBUG] Usuario creado con ID: " + usuarioId);

            // 2. LUEGO CREAR EL ODONTÓLOGO CON EL USUARIO_ID
            Odontologo odontologo = extraerDatosFormulario(request);
            odontologo.setUsuarioId(usuarioId); // IMPORTANTE: Asignar el usuario_id

            System.out.println("[DEBUG] Creando odontólogo con usuario_id: " + usuarioId);
            boolean odontologoCreado = odontologoDAO.insertar(odontologo);

            if (odontologoCreado) {
                session.setAttribute("mensaje", "Odontólogo creado exitosamente. Credenciales: Usuario='" + usuario + "'");
                session.setAttribute("tipoMensaje", "success");
                response.sendRedirect("odontologo?action=listar");
            } else {
                // Si falla, eliminar el usuario creado
                System.err.println("[ERROR] Fallo al crear odontólogo, eliminando usuario...");
                usuarioDAO.eliminar(usuarioId);
                session.setAttribute("mensaje", "Error al crear el odontólogo");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("odontologo?action=nuevo");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] Excepción al guardar odontólogo: " + e.getMessage());

            String mensajeError = e.getMessage();

            if (mensajeError != null && mensajeError.contains("Duplicate entry") && mensajeError.contains("cedula")) {
                session.setAttribute("mensaje", "Ya existe un odontólogo con esta cédula");
                session.setAttribute("tipoMensaje", "error");
            } else {
                session.setAttribute("mensaje", "Error: " + mensajeError);
                session.setAttribute("tipoMensaje", "error");
            }
            response.sendRedirect("odontologo?action=nuevo");
        }
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

        // Email solo si es edición (en creación viene del formulario de credenciales)
        String emailParam = request.getParameter("email_contacto");
        if (emailParam != null && !emailParam.isEmpty()) {
            odontologo.setEmail(emailParam);
        }

        odontologo.setDireccion(request.getParameter("direccion"));

        String fechaNacStr = request.getParameter("fecha_nacimiento");
        if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
            odontologo.setFechaNacimiento(Date.valueOf(fechaNacStr));
        }

        odontologo.setGenero(request.getParameter("genero"));

        return odontologo;
    }
}