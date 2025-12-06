<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:19 a. m.
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
    <title><%= request.getAttribute("factura") == null ? "Nueva Factura" : "Editar Factura" %> - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
        <%
        Factura factura = (Factura) request.getAttribute("factura");
        List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
        List<TratamientoOdontologico> tratamientos = (List<TratamientoOdontologico>) request.getAttribute("tratamientos");
        List<DetalleFactura> detalles = (List<DetalleFactura>) request.getAttribute("detalles");
        String numeroFactura = (String) request.getAttribute("numeroFactura");
        boolean esEdicion = factura != null;
    %>

    <div class="page-header">
        <div>
            <h1 class="page-title">
                <i class="fas fa-<%= esEdicion ? "edit" : "plus" %>"></i>
                <%= esEdicion ? "Editar Factura" : "Nueva Factura" %>
            </h1>
            <p class="page-subtitle">
                <%= esEdicion ? "Modifica los datos de la factura" : "Complete el formulario para crear una nueva factura" %>
            </p>
        </div>
        <a href="factura?action=listar" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver
        </a>
    </div>

    <div class="card">
        <div class="card-body">
            <form action="factura" method="post" id="facturaForm">
                <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "guardar" %>">
                    <% if (esEdicion) { %>
                <input type="hidden" name="factura_id" value="<%= factura.getFacturaId() %>">
                    <% } %>

                <h3 style="margin-bottom: 1rem; color: #374151;">
                    <i class="fas fa-info-circle"></i> Información Básica
                </h3>

                <div class="form-grid">
                    <div class="form-group">
                        <label for="numero_factura">Número de Factura *</label>
                        <input type="text" id="numero_factura" name="numero_factura" class="form-control"
                               value="<%= esEdicion ? factura.getNumeroFactura() : numeroFactura %>"
                               readonly style="background: #F3F4F6;">
                    </div>

                    <div class="form-group">
                        <label for="paciente_id">Paciente *</label>
                        <select id="paciente_id" name="paciente_id" class="form-control" required
                                <%= esEdicion ? "disabled" : "" %>>
                            <option value="">Seleccione un paciente</option>
                            <% for (Paciente p : pacientes) { %>
                            <option value="<%= p.getPacienteId() %>"
                                    <%= esEdicion && p.getPacienteId() == factura.getPacienteId() ? "selected" : "" %>>
                                <%= p.getNombres() %> <%= p.getApellidos() %> - <%= p.getCedula() %>
                            </option>
                            <% } %>
                        </select>
                        <% if (esEdicion) { %>
                        <input type="hidden" name="paciente_id" value="<%= factura.getPacienteId() %>">
                        <% } %>
                    </div>

                    <div class="form-group">
                        <label for="estado">Estado *</label>
                        <select id="estado" name="estado" class="form-control" required>
                            <option value="Pendiente" <%= esEdicion && "Pendiente".equals(factura.getEstado()) ? "selected" : !esEdicion ? "selected" : "" %>>Pendiente</option>
                            <option value="Parcial" <%= esEdicion && "Parcial".equals(factura.getEstado()) ? "selected" : "" %>>Pago Parcial</option>
                            <option value="Pagada" <%= esEdicion && "Pagada".equals(factura.getEstado()) ? "selected" : "" %>>Pagada</option>
                            <option value="Cancelada" <%= esEdicion && "Cancelada".equals(factura.getEstado()) ? "selected" : "" %>>Cancelada</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="metodo_pago">Método de Pago</label>
                        <select id="metodo_pago" name="metodo_pago" class="form-control">
                            <option value="Efectivo" <%= esEdicion && "Efectivo".equals(factura.getMetodoPago()) ? "selected" : !esEdicion ? "selected" : "" %>>Efectivo</option>
                            <option value="Tarjeta" <%= esEdicion && "Tarjeta".equals(factura.getMetodoPago()) ? "selected" : "" %>>Tarjeta</option>
                            <option value="Transferencia" <%= esEdicion && "Transferencia".equals(factura.getMetodoPago()) ? "selected" : "" %>>Transferencia</option>
                            <option value="Cheque" <%= esEdicion && "Cheque".equals(factura.getMetodoPago()) ? "selected" : "" %>>Cheque</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="observaciones">Observaciones</label>
                    <textarea id="observaciones" name="observaciones" class="form-control" rows="3"
                              placeholder="Notas adicionales sobre la factura"><%= esEdicion && factura.getObservaciones() != null ? factura.getObservaciones() : "" %></textarea>
                </div>

                <hr style="margin: 2rem 0; border-color: #E5E7EB;">

                <h3 style="margin-bottom: 1rem; color: #374151;">
                    <i class="fas fa-list"></i> Detalles de la Factura
                </h3>

                <div id="detallesContainer">
                    <% if (esEdicion && detalles != null && !detalles.isEmpty()) {
                        for (DetalleFactura d : detalles) {
                    %>
                    <div class="detalle-row">
                        <div class="form-group" style="margin: 0;">
                            <label>Tratamiento/Descripción</label>
                            <select class="form-control tratamiento-select" onchange="seleccionarTratamiento(this)">
                                <option value="">Escribir descripción manual</option>
                                <% for (TratamientoOdontologico t : tratamientos) { %>
                                <option value="<%= t.getTratamientoId() %>" data-precio="<%= t.getPrecioBase() %>"
                                        data-nombre="<%= t.getNombre() %>"
                                        <%= d.getTratamientoId() != null && d.getTratamientoId() == t.getTratamientoId() ? "selected" : "" %>>
                                    <%= t.getNombre() %> - <%= t.getPrecioFormateado() %>
                                </option>
                                <% } %>
                            </select>
                            <input type="hidden" name="tratamiento_id[]" value="<%= d.getTratamientoId() != null ? d.getTratamientoId() : "" %>">
                            <input type="text" name="descripcion[]" class="form-control descripcion-input"
                                   value="<%= d.getDescripcion() %>" placeholder="Descripción del tratamiento"
                                   required style="margin-top: 0.5rem;">
                        </div>
                        <div class="form-group" style="margin: 0;">
                            <label>Cantidad</label>
                            <input type="number" name="cantidad[]" class="form-control cantidad-input"
                                   value="<%= d.getCantidad() %>" min="1" required onchange="calcularTotales()">
                        </div>
                        <div class="form-group" style="margin: 0;">
                            <label>Precio Unitario</label>
                            <input type="number" name="precio_unitario[]" class="form-control precio-input"
                                   value="<%= d.getPrecioUnitario() %>" step="0.01" min="0" required onchange="calcularTotales()">
                        </div>
                        <div class="form-group" style="margin: 0;">
                            <label>Subtotal</label>
                            <input type="text" class="form-control subtotal-input"
                                   value="<%= d.getSubtotalFormateado() %>" readonly>
                        </div>
                        <div>
                            <label style="visibility: hidden;">Acción</label>
                            <button type="button" class="btn-remove" onclick="eliminarDetalle(this)">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>

                <button type="button" class="btn btn-secondary" onclick="agregarDetalle()">
                    <i class="fas fa-plus"></i> Agregar Línea
                </button>

                <div class="totales-section">
                    <h4 style="margin-bottom: 1rem; color: #374151;">Resumen</h4>

                    <div class="form-group" style="max-width: 300px;">
                        <label for="descuento">Descuento ($)</label>
                        <input type="number" id="descuento" name="descuento" class="form-control"
                               value="<%= esEdicion ? factura.getDescuento() : "0" %>"
                               step="0.01" min="0" onchange="calcularTotales()">
                    </div>

                    <input type="hidden" id="subtotal" name="subtotal" value="0">

                    <div class="total-row">
                        <span>Subtotal:</span>
                        <strong id="subtotal_display">$0.00</strong>
                    </div>
                    <div class="total-row">
                        <span>Descuento:</span>
                        <strong id="descuento_display">$0.00</strong>
                    </div>
                    <div class="total-row">
                        <span>IVA (15%):</span>
                        <strong id="iva_display">$0.00</strong>
                    </div>
                    <div class="total-row">
                        <span>TOTAL:</span>
                        <strong id="total_display">$0.00</strong>
                    </div>
                </div>

                <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i>
                        <%= esEdicion ? "Actualizar Factura" : "Crear Factura" %>
                    </button>
                    <a href="factura?action=listar" class="btn btn-secondary">