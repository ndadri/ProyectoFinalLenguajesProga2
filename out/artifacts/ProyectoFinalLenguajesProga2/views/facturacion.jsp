<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:17 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Factura" %>
<%@ page import="models.Usuario" %>
<%@ page import="java.util.List" %>
<%
    Usuario usuarioFact = (Usuario) session.getAttribute("usuario");
    if (usuarioFact == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!-- Vista: facturacion.jsp
Propósito: Listado y gestión de facturas. Permite filtrar por estado y realizar acciones (ver, pagar, editar, cancelar, eliminar).
Variables/atributos esperados:
- facturas (List<models.Factura>)
- estadoFiltro (String) (opcional)
- mensaje, tipoMensaje en session para notificaciones
Secciones principales:
1) Cabecera con botón para crear nueva factura.
2) Barra de filtros por estado.
3) Tabla de facturas o estado vacío.
4) Funciones JS para confirmación de acciones.
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Facturación - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- SIDEBAR -->
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <!-- HEADER -->
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-file-invoice-dollar"></i> Facturación
                </h1>
                <p class="page-subtitle">Gestiona facturas y pagos de la clínica</p>
            </div>
            <a href="factura?action=nuevo" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nueva Factura
            </a>
        </div>

        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String tipoMensaje = (String) session.getAttribute("tipoMensaje");
            if (mensaje != null) {
        %>
        <div class="alert alert-<%= tipoMensaje %>">
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

        <%
            String estadoFiltro = (String) request.getAttribute("estadoFiltro");
        %>
        <div class="filter-bar">
            <span style="font-weight: 600;">Filtrar por estado:</span>
            <a href="factura?action=listar" class="filter-btn <%= estadoFiltro == null ? "active" : "" %>">
                <i class="fas fa-list"></i> Todas
            </a>
            <a href="factura?action=listar&estado=Pendiente" class="filter-btn <%= "Pendiente".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-clock"></i> Pendientes
            </a>
            <a href="factura?action=listar&estado=Parcial" class="filter-btn <%= "Parcial".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-hourglass-half"></i> Parciales
            </a>
            <a href="factura?action=listar&estado=Pagada" class="filter-btn <%= "Pagada".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-check-circle"></i> Pagadas
            </a>
            <a href="factura?action=listar&estado=Cancelada" class="filter-btn <%= "Cancelada".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-times-circle"></i> Canceladas
            </a>
        </div>

        <div class="card">
            <div class="card-body">
                <%
                    @SuppressWarnings("unchecked")
                    List<Factura> facturas = (List<Factura>) request.getAttribute("facturas");
                    if (facturas == null || facturas.isEmpty()) {
                %>
                <div class="empty-state">
                    <i class="fas fa-receipt" style="font-size: 4rem; color: #D1D5DB;"></i>
                    <h3>No hay facturas registradas</h3>
                    <p>Comienza creando la primera factura</p>
                    <a href="factura?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Nueva Factura
                    </a>
                </div>
                <%
                } else {
                %>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Número</th>
                            <th>Fecha</th>
                            <th>Paciente</th>
                            <th>Cédula</th>
                            <th>Subtotal</th>
                            <th>IVA</th>
                            <th>Total</th>
                            <th>Pagado</th>
                            <th>Saldo</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Factura f : facturas) {
                        %>
                        <tr>
                            <td><strong><%= f.getNumeroFactura() %></strong></td>
                            <td><%= f.getFechaFormateada() %></td>
                            <td><%= f.getPacienteNombre() %></td>
                            <td><%= f.getPacienteCedula() %></td>
                            <td><%= f.getSubtotalFormateado() %></td>
                            <td><%= f.getIvaFormateado() %></td>
                            <td><strong><%= f.getTotalFormateado() %></strong></td>
                            <td style="color: #10B981;"><%= f.getTotalPagadoFormateado() %></td>
                            <td style="color: <%= f.getSaldoPendiente().doubleValue() > 0 ? "#EF4444" : "#10B981" %>;">
                                <%= f.getSaldoPendienteFormateado() %>
                            </td>
                            <td>
                                <span class="badge badge-<%= f.getEstadoColor() %>">
                                    <%= f.getEstado() %>
                                </span>
                            </td>
                            <td>
                                <div class="actions">
                                    <a href="factura?action=ver&id=<%= f.getFacturaId() %>" class="btn-icon" style="background: #DBEAFE; color: #1E40AF;" title="Ver detalle">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <% if (!f.isPagada() && !"Cancelada".equals(f.getEstado())) { %>
                                    <a href="factura?action=pagar&id=<%= f.getFacturaId() %>" class="btn-icon" style="background: #D1FAE5; color: #065F46;" title="Registrar pago">
                                        <i class="fas fa-dollar-sign"></i>
                                    </a>
                                    <% } %>
                                    <% if (!"Cancelada".equals(f.getEstado()) && !"Pagada".equals(f.getEstado())) { %>
                                    <a href="factura?action=editar&id=<%= f.getFacturaId() %>" class="btn-icon btn-edit" title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <% } %>
                                    <% if (!"Cancelada".equals(f.getEstado()) && f.getTotalPagado().doubleValue() == 0) { %>
                                    <button onclick="cancelarFactura(<%= f.getFacturaId() %>, '<%= f.getNumeroFactura() %>')" class="btn-icon" style="background: #FEF3C7; color: #92400E;" title="Cancelar">
                                        <i class="fas fa-ban"></i>
                                    </button>
                                    <% } %>
                                    <% if (f.getTotalPagado().doubleValue() == 0) { %>
                                    <button onclick="confirmarEliminacion(<%= f.getFacturaId() %>, '<%= f.getNumeroFactura() %>')" class="btn-icon btn-delete" title="Eliminar">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                    <% } %>
                                </div>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
                <div class="table-footer">
                    <p class="table-info">Mostrando <strong><%= facturas.size() %></strong> factura(s)</p>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminacion(id, numero) {
        if (confirm('¿Está seguro que desea eliminar la factura ' + numero + '?\n\nEsta acción no se puede deshacer.')) {
            window.location.href = 'factura?action=eliminar&id=' + id;
        }
    }

    function cancelarFactura(id, numero) {
        if (confirm('¿Está seguro que desea cancelar la factura ' + numero + '?')) {
            window.location.href = 'factura?action=cancelar&id=' + id;
        }
    }

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