package controllers;

import dao.FacturaDAO;
import models.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
/**
 * FacturaServlet - Controlador para la gestión de facturación y pagos
 *
 * Descripción: Maneja todo el sistema de facturación incluyendo creación de facturas,
 * detalles de tratamientos, registro de pagos y seguimiento de cuentas por cobrar.
 * Permite generar facturas automáticas y gestionar el historial de pagos.
 */
@WebServlet("/factura")
public class FacturaServlet extends HttpServlet {
    private FacturaDAO facturaDAO;

    @Override
    public void init() throws ServletException {
        facturaDAO = new FacturaDAO();
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
                    listarFacturas(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "ver":
                    verDetalle(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "pagar":
                    mostrarFormularioPago(request, response);
                    break;
                case "eliminar":
                    eliminarFactura(request, response);
                    break;
                case "cancelar":
                    cancelarFactura(request, response);
                    break;
                default:
                    listarFacturas(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en operación de factura", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "guardar":
                    guardarFactura(request, response);
                    break;
                case "actualizar":
                    actualizarFactura(request, response);
                    break;
                case "registrar_pago":
                    registrarPago(request, response);
                    break;
                default:
                    listarFacturas(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en operación de factura", e);
        }
    }

    private void listarFacturas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String estado = request.getParameter("estado");
        List<Factura> facturas;

        if (estado != null && !estado.isEmpty()) {
            facturas = facturaDAO.obtenerPorEstado(estado);
            request.setAttribute("estadoFiltro", estado);
        } else {
            facturas = facturaDAO.obtenerTodos();
        }

        request.setAttribute("facturas", facturas);
        request.getRequestDispatcher("/views/facturacion.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        // Generar número de factura
        String numeroFactura = facturaDAO.generarNumeroFactura();
        request.setAttribute("numeroFactura", numeroFactura);

        // Obtener pacientes
        List<Paciente> pacientes = facturaDAO.obtenerPacientes();
        request.setAttribute("pacientes", pacientes);

        // Obtener tratamientos - ESTO ES CRÍTICO
        List<TratamientoOdontologico> tratamientos = facturaDAO.obtenerTratamientos();
        request.setAttribute("tratamientos", tratamientos);

        // DEBUG: Imprimir en consola
        System.out.println("Tratamientos obtenidos: " + tratamientos.size());
        for (TratamientoOdontologico t : tratamientos) {
            System.out.println("  - " + t.getTratamientoId() + ": " + t.getNombre() + " - $" + t.getPrecioBase());
        }

        request.getRequestDispatcher("/views/factura-form.jsp").forward(request, response);
    }


    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int facturaId = Integer.parseInt(request.getParameter("id"));

        Factura factura = facturaDAO.obtenerPorId(facturaId);
        List<DetalleFactura> detalles = facturaDAO.obtenerDetalles(facturaId);
        List<Paciente> pacientes = facturaDAO.obtenerPacientes();
        List<TratamientoOdontologico> tratamientos = facturaDAO.obtenerTratamientos();

        request.setAttribute("factura", factura);
        request.setAttribute("detalles", detalles);
        request.setAttribute("pacientes", pacientes);
        request.setAttribute("tratamientos", tratamientos);

        request.getRequestDispatcher("/views/factura-form.jsp").forward(request, response);
    }

    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int facturaId = Integer.parseInt(request.getParameter("id"));

        Factura factura = facturaDAO.obtenerPorId(facturaId);
        List<DetalleFactura> detalles = facturaDAO.obtenerDetalles(facturaId);
        List<HistorialPago> pagos = facturaDAO.obtenerPagos(facturaId);

        request.setAttribute("factura", factura);
        request.setAttribute("detalles", detalles);
        request.setAttribute("pagos", pagos);

        request.getRequestDispatcher("/views/factura-detalle.jsp").forward(request, response);
    }

    private void mostrarFormularioPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int facturaId = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(facturaId);

        request.setAttribute("factura", factura);
        request.getRequestDispatcher("/views/factura-pago.jsp").forward(request, response);
    }

    private void guardarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            // Crear objeto Factura
            Factura factura = new Factura();
            factura.setNumero_factura(request.getParameter("numero_factura"));
            factura.setPaciente_id(Integer.parseInt(request.getParameter("paciente_id")));

            BigDecimal subtotal = new BigDecimal(request.getParameter("subtotal"));
            BigDecimal descuento = new BigDecimal(request.getParameter("descuento"));

            factura.setSubtotal(subtotal);
            factura.setDescuento(descuento);
            factura.calcularTotales();

            factura.setEstado(request.getParameter("estado"));
            factura.setMetodo_pago(request.getParameter("metodo_pago"));
            factura.setObservaciones(request.getParameter("observaciones"));

            // Insertar factura
            int facturaId = facturaDAO.insertar(factura);

            // Guardar detalles
            String[] tratamientoIds = request.getParameterValues("tratamiento_id[]");
            String[] descripciones = request.getParameterValues("descripcion[]");
            String[] cantidades = request.getParameterValues("cantidad[]");
            String[] preciosUnitarios = request.getParameterValues("precio_unitario[]");

            if (descripciones != null) {
                for (int i = 0; i < descripciones.length; i++) {
                    DetalleFactura detalle = new DetalleFactura();
                    detalle.setFactura_id(facturaId);

                    if (tratamientoIds[i] != null && !tratamientoIds[i].isEmpty()) {
                        detalle.setTratamiento_id(Integer.parseInt(tratamientoIds[i]));
                    }

                    detalle.setDescripcion(descripciones[i]);
                    detalle.setCantidad(Integer.parseInt(cantidades[i]));
                    detalle.setPrecio_unitario(new BigDecimal(preciosUnitarios[i]));
                    detalle.calcularSubtotal();

                    facturaDAO.insertarDetalle(detalle);
                }
            }

            session.setAttribute("mensaje", "Factura creada exitosamente");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect("factura?action=ver&id=" + facturaId);

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error al crear la factura: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("factura?action=nuevo");
        }
    }

    private void actualizarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int facturaId = Integer.parseInt(request.getParameter("factura_id"));

            Factura factura = facturaDAO.obtenerPorId(facturaId);

            BigDecimal subtotal = new BigDecimal(request.getParameter("subtotal"));
            BigDecimal descuento = new BigDecimal(request.getParameter("descuento"));

            factura.setSubtotal(subtotal);
            factura.setDescuento(descuento);
            factura.calcularTotales();

            factura.setEstado(request.getParameter("estado"));
            factura.setMetodo_pago(request.getParameter("metodo_pago"));
            factura.setObservaciones(request.getParameter("observaciones"));

            facturaDAO.actualizar(factura);

            // Eliminar detalles anteriores
            facturaDAO.eliminarDetalles(facturaId);

            // Insertar nuevos detalles
            String[] tratamientoIds = request.getParameterValues("tratamiento_id[]");
            String[] descripciones = request.getParameterValues("descripcion[]");
            String[] cantidades = request.getParameterValues("cantidad[]");
            String[] preciosUnitarios = request.getParameterValues("precio_unitario[]");

            if (descripciones != null) {
                for (int i = 0; i < descripciones.length; i++) {
                    DetalleFactura detalle = new DetalleFactura();
                    detalle.setFactura_id(facturaId);

                    if (tratamientoIds[i] != null && !tratamientoIds[i].isEmpty()) {
                        detalle.setTratamiento_id(Integer.parseInt(tratamientoIds[i]));
                    }

                    detalle.setDescripcion(descripciones[i]);
                    detalle.setCantidad(Integer.parseInt(cantidades[i]));
                    detalle.setPrecio_unitario(new BigDecimal(preciosUnitarios[i]));
                    detalle.calcularSubtotal();

                    facturaDAO.insertarDetalle(detalle);
                }
            }

            session.setAttribute("mensaje", "Factura actualizada exitosamente");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect("factura?action=ver&id=" + facturaId);

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error al actualizar la factura: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("factura?action=listar");
        }
    }

    private void registrarPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int facturaId = Integer.parseInt(request.getParameter("factura_id"));

            HistorialPago pago = new HistorialPago();
            pago.setFactura_id(facturaId);
            pago.setMonto(new BigDecimal(request.getParameter("monto")));
            pago.setMetodo_pago(request.getParameter("metodo_pago"));
            pago.setReferencia(request.getParameter("referencia"));
            pago.setRecibido_por(request.getParameter("recibido_por"));
            pago.setObservaciones(request.getParameter("observaciones"));

            facturaDAO.insertarPago(pago);
            facturaDAO.actualizarEstadoFacturaPorPagos(facturaId);

            session.setAttribute("mensaje", "Pago registrado exitosamente");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect("factura?action=ver&id=" + facturaId);

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error al registrar el pago: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("factura?action=pagar&id=" + request.getParameter("factura_id"));
        }
    }

    private void eliminarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int facturaId = Integer.parseInt(request.getParameter("id"));

            Factura factura = facturaDAO.obtenerPorId(facturaId);

            if (factura.getTotal_pagado().compareTo(BigDecimal.ZERO) > 0) {
                session.setAttribute("mensaje", "No se puede eliminar una factura con pagos registrados");
                session.setAttribute("tipoMensaje", "error");
            } else {
                facturaDAO.eliminar(facturaId);
                session.setAttribute("mensaje", "Factura eliminada exitosamente");
                session.setAttribute("tipoMensaje", "success");
            }

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error al eliminar la factura: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("factura?action=listar");
    }

    private void cancelarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        try {
            int facturaId = Integer.parseInt(request.getParameter("id"));
            Factura factura = facturaDAO.obtenerPorId(facturaId);

            if (factura.getTotal_pagado().compareTo(BigDecimal.ZERO) > 0) {
                session.setAttribute("mensaje", "No se puede cancelar una factura con pagos registrados");
                session.setAttribute("tipoMensaje", "error");
            } else {
                factura.setEstado("Cancelada");
                facturaDAO.actualizar(factura);
                session.setAttribute("mensaje", "Factura cancelada exitosamente");
                session.setAttribute("tipoMensaje", "success");
            }

        } catch (Exception e) {
            session.setAttribute("mensaje", "Error al cancelar la factura: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("factura?action=listar");
    }
}