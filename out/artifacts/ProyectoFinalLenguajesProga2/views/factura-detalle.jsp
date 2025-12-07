<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:23 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle de Factura - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <%
        Factura factura = (Factura) request.getAttribute("factura");
        List<DetalleFactura> detalles = (List<DetalleFactura>) request.getAttribute("detalles");
        List<HistorialPago> pagos = (List<HistorialPago>) request.getAttribute("pagos");
    %>

    <div class="page-header no-print">
        <div>
            <h1 class="page-title">
                <i class="fas fa-file-invoice"></i> Detalle de Factura
            </h1>
            <p class="page-subtitle">Factura <%= factura.getNumeroFactura() %></p>
        </div>
        <div style="display: flex; gap: 0.5rem;">
            <button onclick="window.print()" class="btn btn-secondary">
                <i class="fas fa-print"></i> Imprimir
            </button>
            <% if (!factura.isPagada() && !"Cancelada".equals(factura.getEstado())) { %>
            <a href="factura?action=pagar&id=<%= factura.getFacturaId() %>" class="btn btn-primary">
                <i class="fas fa-dollar-sign"></i> Registrar Pago
            </a>
            <% } %>
            <a href="factura?action=listar" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>
    </div>

    <%
        String mensaje = (String) session.getAttribute("mensaje");
        String tipoMensaje = (String) session.getAttribute("tipoMensaje");
        if (mensaje != null) {
    %>
    <div class="alert alert-<%= tipoMensaje %> no-print">
        <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-circle" %>"></i>
        <%= mensaje %>
        <button class="close-alert" onclick="this.parentElement.style.display='none'">
            <i class="fas fa-times"></i>
        </button>
    </div>
    <%
            session.removeAttribute("mensaje");
            session.removeAttribute("tipoMensaje");
        }
    %>

    <div class="factura-header">
        <div>
            <h2 style="color: #111827; margin-bottom: 1rem;">
                <i class="fas fa-tooth" style="color: #3B82F6;"></i> DentalCare
            </h2>
            <div class="info-group">
                <div class="info-label">Número de Factura</div>
                <div class="info-value"><%= factura.getNumeroFactura() %></div>
            </div>
            <div class="info-group">
                <div class="info-label">Fecha de Emisión</div>
                <div class="info-value"><%= factura.getFechaHoraFormateada() %></div>
            </div>
            <div class="info-group">
                <div class="info-label">Estado</div>
                <div>
                    <span class="estado-badge-large badge badge-<%= factura.getEstadoColor() %>">
                        <i class="fas fa-<%= factura.isPagada() ? "check-circle" : factura.isPendiente() ? "clock" : factura.isParcial() ? "hourglass-half" : "times-circle" %>"></i>
                        <%= factura.getEstado() %>
                    </span>
                </div>
            </div>
        </div>

        <div>
            <h3 style="color: #374151; margin-bottom: 1rem;">Datos del Paciente</h3>
            <div class="info-group">
                <div class="info-label">Nombre</div>
                <div class="info-value"><%= factura.getPacienteNombre() %></div>
            </div>
            <div class="info-group">
                <div class="info-label">Cédula</div>
                <div class="info-value"><%= factura.getPacienteCedula() %></div>
            </div>
            <% if (factura.getPacienteTelefono() != null) { %>
            <div class="info-group">
                <div class="info-label">Teléfono</div>
                <div class="info-value"><%= factura.getPacienteTelefono() %></div>
            </div>
            <% } %>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <h3><i class="fas fa-list"></i> Detalles de la Factura</h3>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th style="width: 50%;">Descripción</th>
                        <th style="text-align: center;">Cantidad</th>
                        <th style="text-align: right;">Precio Unit.</th>
                        <th style="text-align: right;">Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (DetalleFactura d : detalles) { %>
                    <tr>
                        <td>
                            <strong><%= d.getDescripcion() %></strong>
                            <% if (d.getTratamientoCategoria() != null) { %>
                            <br>
                            <small style="color: #6B7280;">
                                <i class="fas fa-tag"></i> <%= d.getTratamientoCategoria() %>
                            </small>
                            <% } %>
                        </td>
                        <td style="text-align: center;"><%= d.getCantidad() %></td>
                        <td style="text-align: right;"><%= d.getPrecioUnitarioFormateado() %></td>
                        <td style="text-align: right;"><strong><%= d.getSubtotalFormateado() %></strong></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div class="totales-box" style="max-width: 400px; margin-left: auto;">
                <div class="total-row">
                    <span>Subtotal:</span>
                    <strong><%= factura.getSubtotalFormateado() %></strong>
                </div>
                <% if (factura.getDescuento().doubleValue() > 0) { %>
                <div class="total-row">
                    <span>Descuento:</span>
                    <strong style="color: #10B981;">- <%= factura.getDescuentoFormateado() %></strong>
                </div>
                <% } %>
                <div class="total-row">
                    <span>IVA (15%):</span>
                    <strong><%= factura.getIvaFormateado() %></strong>
                </div>
                <div class="total-row">
                    <span>TOTAL:</span>
                    <strong style="color: #3B82F6; font-size: 1.5rem;"><%= factura.getTotalFormateado() %></strong>
                </div>
            </div>
        </div>
    </div>

    <div class="pagos-section">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
            <h3 style="color: #374151;">
                <i class="fas fa-history"></i> Historial de Pagos
            </h3>
            <% if (!factura.isPagada() && !"Cancelada".equals(factura.getEstado())) { %>
            <a href="factura?action=pagar&id=<%= factura.getFacturaId() %>" class="btn btn-primary btn-sm no-print">
                <i class="fas fa-plus"></i> Nuevo Pago
            </a>
            <% } %>
        </div>

        <% if (pagos == null || pagos.isEmpty()) { %>
        <div style="text-align: center; padding: 2rem; color: #6B7280;">
            <i class="fas fa-inbox" style="font-size: 48px; margin-bottom: 1rem; opacity: 0.5;"></i>
            <p>No hay pagos registrados</p>
        </div>
        <% } else { %>
        <div style="display: grid; gap: 0.75rem;">
            <% for (HistorialPago p : pagos) { %>
            <div class="pago-card">
                <div style="display: flex; justify-content: space-between; align-items: start;">
                    <div>
                        <div style="font-weight: 600; font-size: 16px; color: #111827; margin-bottom: 0.25rem;">
                            <i class="fas <%= p.getIconoMetodoPago() %>"></i>
                            <%= p.getMontoFormateado() %>
                        </div>
                        <div style="font-size: 14px; color: #6B7280;">
                            <%= p.getFechaHoraFormateada() %> - <%= p.getMetodoPago() %>
                        </div>
                        <% if (p.getRecibidoPor() != null) { %>
                        <div style="font-size: 13px; color: #6B7280; margin-top: 0.25rem;">
                            Recibido por: <%= p.getRecibidoPor() %>
                        </div>
                        <% } %>
                        <% if (p.getReferencia() != null && !p.getReferencia().isEmpty()) { %>
                        <div style="font-size: 13px; color: #6B7280;">
                            Ref: <%= p.getReferencia() %>
                        </div>
                        <% } %>
                        <% if (p.getObservaciones() != null && !p.getObservaciones().isEmpty()) { %>
                        <div style="font-size: 13px; color: #6B7280; margin-top: 0.25rem;">
                            <i class="fas fa-sticky-note"></i> <%= p.getObservaciones() %>
                        </div>
                        <% } %>
                    </div>
                    <div style="background: #10B981; color: white; padding: 0.25rem 0.75rem; border-radius: 6px; font-size: 12px;">
                        <i class="fas fa-check"></i> Aplicado
                    </div>
                </div>
            </div>
            <% } %>
        </div>

        <div style="background: white; padding: 1rem; border-radius: 8px; margin-top: 1rem; border: 2px solid #E5E7EB;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                <span style="color: #6B7280;">Total Factura:</span>
                <strong><%= factura.getTotalFormateado() %></strong>
            </div>
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                <span style="color: #6B7280;">Total Pagado:</span>
                <strong style="color: #10B981;"><%= factura.getTotalPagadoFormateado() %></strong>
            </div>
            <div style="display: flex; justify-content: space-between; padding-top: 0.5rem; border-top: 2px solid #E5E7EB;">
                <span style="font-weight: 600; color: #111827;">Saldo Pendiente:</span>
                <strong style="color: <%= factura.getSaldoPendiente().doubleValue() > 0 ? "#EF4444" : "#10B981" %>; font-size: 18px;">
                    <%= factura.getSaldoPendienteFormateado() %>
                </strong>
            </div>
        </div>
        <% } %>
    </div>

    <% if (factura.getObservaciones() != null && !factura.getObservaciones().isEmpty()) { %>
    <div class="card" style="margin-top: 1.5rem;">
        <div class="card-body">
            <h4 style="color: #374151; margin-bottom: 0.5rem;">
                <i class="fas fa-sticky-note"></i> Observaciones
            </h4>
            <p style="color: #6B7280; margin: 0;"><%= factura.getObservaciones() %></p>
        </div>
    </div>
    <% } %>
</div>

<script>
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        });
    }, 5000);
</script>
</body>
</html>
