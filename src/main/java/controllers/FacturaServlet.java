package controllers;

import dao.FacturaDAO;
import models.Factura;
import models.DetalleFactura;
import models.HistorialPago;
import models.Paciente;
import models.TratamientoOdontologico;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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
            case "cancelar":
                cancelarFactura(request, response);
                break;
            case "eliminar":
                eliminarFactura(request, response);
                break;
            default:
                listarFacturas(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            guardarFactura(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarFactura(request, response);
        } else if ("registrar_pago".equals(action)) {
            registrarPago(request, response);
        }
    }

    // Listar todas las facturas
    private void listarFacturas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String estadoFiltro = request.getParameter("estado");
            List<Factura> facturas;

            if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
                facturas = facturaDAO.obtenerPorEstado(estadoFiltro);
            } else {
                facturas = facturaDAO.obtenerTodos();
            }

            request.setAttribute("facturas", facturas);
            request.setAttribute("estadoFiltro", estadoFiltro);
            request.getRequestDispatcher("/views/facturacion.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar facturas: " + e.getMessage());
            request.getRequestDispatcher("/views/facturacion.jsp").forward(request, response);
        }
    }

    // Mostrar formulario para nueva factura
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Paciente> pacientes = facturaDAO.obtenerPacientes();
        List<TratamientoOdontologico> tratamientos = facturaDAO.obtenerTratamientos();
        String numeroFactura = facturaDAO.generarNumeroFactura();

        request.setAttribute("pacientes", pacientes);
        request.setAttribute("tratamientos", tratamientos);
        request.setAttribute("numeroFactura", numeroFactura);
        request.getRequestDispatcher("/views/factura-form.jsp").forward(request, response);
    }

    // Mostrar formulario para editar factura
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(id);

        if (factura != null) {
            List<Paciente> pacientes = facturaDAO.obtenerPacientes();
            List<TratamientoOdontologico> tratamientos = facturaDAO.obtenerTratamientos();
            List<DetalleFactura> detalles = facturaDAO.obtenerDetalles(id);

            request.setAttribute("factura", factura);
            request.setAttribute("pacientes", pacientes);
            request.setAttribute("tratamientos", tratamientos);
            request.setAttribute("detalles", detalles);
            request.getRequestDispatcher("/views/factura-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("factura?action=listar");
        }
    }

    // Ver detalle de factura
    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(id);

        if (factura != null) {
            List<DetalleFactura> detalles = facturaDAO.obtenerDetalles(id);
            List<HistorialPago> pagos = facturaDAO.obtenerPagos(id);

            request.setAttribute("factura", factura);
            request.setAttribute("detalles", detalles);
            request.setAttribute("pagos", pagos);
            request.getRequestDispatcher("/views/factura-detalle.jsp").forward(request, response);
        } else {
            response.sendRedirect("factura?action=listar");
        }
    }

    // Mostrar formulario de pago
    private void mostrarFormularioPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(id);

        if (factura != null) {
            request.setAttribute("factura", factura);
            request.getRequestDispatcher("/views/factura-pago.jsp").forward(request, response);
        } else {
            response.sendRedirect("factura?action=listar");
        }
    }

    // Guardar nueva factura
    private void guardarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Crear factura
            Factura factura = extraerDatosFactura(request);

            // Insertar factura y obtener ID
            int facturaId = facturaDAO.insertar(factura);

            if (facturaId > 0) {
                // Guardar detalles
                String[] tratamientoIds = request.getParameterValues("tratamiento_id[]");
                String[] descripciones = request.getParameterValues("descripcion[]");
                String[] cantidades = request.getParameterValues("cantidad[]");
                String[] precios = request.getParameterValues("precio_unitario[]");

                if (tratamientoIds != null && tratamientoIds.length > 0) {
                    for (int i = 0; i < tratamientoIds.length; i++) {
                        DetalleFactura detalle = new DetalleFactura();
                        detalle.setFacturaId(facturaId);

                        if (!tratamientoIds[i].isEmpty()) {
                            detalle.setTratamientoId(Integer.parseInt(tratamientoIds[i]));
                        }

                        detalle.setDescripcion(descripciones[i]);
                        detalle.setCantidad(Integer.parseInt(cantidades[i]));
                        detalle.setPrecioUnitario(new BigDecimal(precios[i]));
                        detalle.calcularSubtotal();

                        facturaDAO.insertarDetalle(detalle);
                    }
                }

                request.getSession().setAttribute("mensaje", "Factura creada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
                response.sendRedirect("factura?action=ver&id=" + facturaId);
            } else {
                request.getSession().setAttribute("mensaje", "Error al crear factura");
                request.getSession().setAttribute("tipoMensaje", "error");
                response.sendRedirect("factura?action=nuevo");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
            response.sendRedirect("factura?action=nuevo");
        }
    }

    // Actualizar factura existente
    private void actualizarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("factura_id"));
            Factura factura = extraerDatosFactura(request);
            factura.setFacturaId(id);

            boolean exito = facturaDAO.actualizar(factura);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Factura actualizada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al actualizar factura");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

            response.sendRedirect("factura?action=ver&id=" + id);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
            response.sendRedirect("factura?action=listar");
        }
    }

    // Registrar pago
    private void registrarPago(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int facturaId = Integer.parseInt(request.getParameter("factura_id"));

            HistorialPago pago = new HistorialPago();
            pago.setFacturaId(facturaId);
            pago.setMonto(new BigDecimal(request.getParameter("monto")));
            pago.setMetodoPago(request.getParameter("metodo_pago"));
            pago.setReferencia(request.getParameter("referencia"));
            pago.setRecibidoPor(request.getParameter("recibido_por"));
            pago.setObservaciones(request.getParameter("observaciones"));

            boolean exito = facturaDAO.insertarPago(pago);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Pago registrado exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al registrar pago");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

            response.sendRedirect("factura?action=ver&id=" + facturaId);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
            response.sendRedirect("factura?action=listar");
        }
    }

    // Cancelar factura
    private void cancelarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = facturaDAO.cambiarEstado(id, "Cancelada");

            if (exito) {
                request.getSession().setAttribute("mensaje", "Factura cancelada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al cancelar factura");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("factura?action=listar");
    }

    // Eliminar factura
    private void eliminarFactura(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean exito = facturaDAO.eliminar(id);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Factura eliminada exitosamente");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "Error al eliminar factura");
                request.getSession().setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("factura?action=listar");
    }

    // Método auxiliar para extraer datos del formulario
    private Factura extraerDatosFactura(HttpServletRequest request) {
        Factura factura = new Factura();

        factura.setPacienteId(Integer.parseInt(request.getParameter("paciente_id")));

        String consultaIdStr = request.getParameter("consulta_id");
        if (consultaIdStr != null && !consultaIdStr.isEmpty()) {
            factura.setConsultaId(Integer.parseInt(consultaIdStr));
        }

        factura.setNumeroFactura(request.getParameter("numero_factura"));

        String subtotalStr = request.getParameter("subtotal");
        if (subtotalStr != null && !subtotalStr.isEmpty()) {
            factura.setSubtotal(new BigDecimal(subtotalStr));
        }

        String descuentoStr = request.getParameter("descuento");
        if (descuentoStr != null && !descuentoStr.isEmpty()) {
            factura.setDescuento(new BigDecimal(descuentoStr));
        } else {
            factura.setDescuento(BigDecimal.ZERO);
        }

        // Calcular IVA y total
        factura.calcularTotales();

        factura.setEstado(request.getParameter("estado"));
        factura.setMetodoPago(request.getParameter("metodo_pago"));
        factura.setObservaciones(request.getParameter("observaciones"));

        return factura;
    }
}