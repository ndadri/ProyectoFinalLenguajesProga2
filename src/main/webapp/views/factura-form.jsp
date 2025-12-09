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
<!-- Vista: factura-form.jsp
Propósito: Formulario para crear o editar facturas, agregar detalles (líneas) y calcular totales.
Variables/atributos esperados:
- factura (models.Factura) (opcional) -> modo edición si presente.
- pacientes (List<models.Paciente>) para seleccionar paciente.
- tratamientos (List<models.TratamientoOdontologico>) para autocompletar líneas.
- detalles (List<models.DetalleFactura>) (opcional) para inicializar en edición.
- numeroFactura (String) para el nuevo número generado por el servidor.
Comportamiento JS importante:
- TRATAMIENTOS: objeto con data de tratamientos para autoselección.
- Funciones: agregarDetalle(), eliminarDetalle(), seleccionarTratamiento(), calcularTotales().
- Validaciones en submit: al menos una línea de detalle y subtotal > 0.
- IVA fijo en JS: 15%.
Notas:
- No se modifica la lógica; se añade documentación para facilitar mantenimiento.
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("factura") == null ? "Nueva Factura" : "Editar Factura" %> - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .detalle-row {
            display: grid;
            grid-template-columns: 3fr 1fr 1.5fr 1.5fr 80px;
            gap: 15px;
            align-items: start;
            padding: 15px;
            margin-bottom: 15px;
            background: #F9FAFB;
            border-radius: 8px;
            border: 1px solid #E5E7EB;
        }

        .totales-section {
            background: #F3F4F6;
            padding: 20px;
            border-radius: 8px;
            margin-top: 30px;
        }

        .total-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #E5E7EB;
        }

        .total-row:last-child {
            border-bottom: none;
            font-size: 1.2rem;
            color: #1F2937;
            margin-top: 10px;
            padding-top: 15px;
            border-top: 2px solid #D1D5DB;
        }

        .btn-remove {
            width: 40px;
            height: 40px;
            border-radius: 6px;
            border: none;
            background: #FEE2E2;
            color: #DC2626;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 24px;
        }

        .btn-remove:hover {
            background: #FECACA;
        }
    </style>
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
                            <% if (pacientes != null) {
                                for (Paciente p : pacientes) { %>
                            <option value="<%= p.getPacienteId() %>"
                                    <%= esEdicion && p.getPacienteId() == factura.getPacienteId() ? "selected" : "" %>>
                                <%= p.getNombres() %> <%= p.getApellidos() %> - <%= p.getCedula() %>
                            </option>
                            <% }} %>
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
                    <!-- Las filas de detalles se agregarán aquí dinámicamente -->
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
                        <i class="fas fa-times"></i> Cancelar
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // TRATAMIENTOS
    const TRATAMIENTOS = {
    <%
    if (tratamientos != null) {
        for (int i = 0; i < tratamientos.size(); i++) {
            TratamientoOdontologico t = tratamientos.get(i);
    %>
    <%= t.getTratamientoId() %>: {
        id: <%= t.getTratamientoId() %>,
        codigo: '<%= t.getCodigo() %>',
            nombre: '<%= t.getNombre().replace("'", "\\'") %>',
            precio: <%= t.getPrecioBase() %>
    }<%= i < tratamientos.size() - 1 ? "," : "" %>
    <%
        }
    }
    %>
    };

    console.log('[DEBUG] Tratamientos cargados:', Object.keys(TRATAMIENTOS).length);

    let contadorDetalles = 0;

    function agregarDetalle(tratamientoId, descripcion, cantidad, precioUnitario) {
        try {
            console.log('[DEBUG] agregarDetalle() llamado con:', {tratamientoId, descripcion, cantidad, precioUnitario});

            contadorDetalles++;

            const container = document.getElementById('detallesContainer');
            if (!container) {
                console.error('[ERROR] No se encontró detallesContainer');
                return;
            }

            const div = document.createElement('div');
            div.className = 'detalle-row';
            div.id = 'detalle-' + contadorDetalles;

            tratamientoId = tratamientoId || '';
            descripcion = descripcion || '';
            cantidad = cantidad || 1;
            precioUnitario = precioUnitario || 0;

            let optionsHTML = '<option value="">Escribir descripción manual</option>';
            for (let id in TRATAMIENTOS) {
                const t = TRATAMIENTOS[id];
                const selected = (tratamientoId == id) ? 'selected' : '';
                optionsHTML += '<option value="' + t.id + '" ' + selected + '>' + t.nombre + ' - $' + t.precio.toFixed(2) + '</option>';
            }

            div.innerHTML =
                '<div class="form-group" style="margin: 0;">' +
                '<label>Tratamiento/Descripción</label>' +
                '<select class="form-control tratamiento-select" onchange="seleccionarTratamiento(this)">' +
                optionsHTML +
                '</select>' +
                '<input type="hidden" name="tratamiento_id[]" class="tratamiento-id-input" value="' + tratamientoId + '">' +
                '<input type="text" name="descripcion[]" class="form-control descripcion-input" value="' + descripcion + '" placeholder="Descripción del tratamiento" required style="margin-top: 0.5rem;">' +
                '</div>' +
                '<div class="form-group" style="margin: 0;">' +
                '<label>Cantidad</label>' +
                '<input type="number" name="cantidad[]" class="form-control cantidad-input" value="' + cantidad + '" min="1" required onchange="calcularTotales()">' +
                '</div>' +
                '<div class="form-group" style="margin: 0;">' +
                '<label>Precio Unitario</label>' +
                '<input type="number" name="precio_unitario[]" class="form-control precio-input" value="' + precioUnitario + '" step="0.01" min="0" required onchange="calcularTotales()">' +
                '</div>' +
                '<div class="form-group" style="margin: 0;">' +
                '<label>Subtotal</label>' +
                '<input type="text" class="form-control subtotal-input" readonly value="$0.00">' +
                '</div>' +
                '<div>' +
                '<button type="button" class="btn-remove" onclick="eliminarDetalle(this)" title="Eliminar">' +
                '<i class="fas fa-trash"></i>' +
                '</button>' +
                '</div>';

            container.appendChild(div);
            console.log('[DEBUG] Fila agregada. Total filas:', document.querySelectorAll('.detalle-row').length);

            calcularTotales();
        } catch(e) {
            console.error('[ERROR] Error en agregarDetalle():', e);
        }
    }

    function eliminarDetalle(btn) {
        const rows = document.querySelectorAll('.detalle-row');
        if (rows.length <= 1) {
            alert('Debe haber al menos un detalle en la factura');
            return;
        }

        if (confirm('¿Eliminar esta línea?')) {
            btn.closest('.detalle-row').remove();
            calcularTotales();
        }
    }

    function seleccionarTratamiento(select) {
        const tratamientoId = select.value;
        const row = select.closest('.detalle-row');

        if (tratamientoId && TRATAMIENTOS[tratamientoId]) {
            const t = TRATAMIENTOS[tratamientoId];

            row.querySelector('.tratamiento-id-input').value = t.id;
            row.querySelector('.descripcion-input').value = t.nombre;
            row.querySelector('.precio-input').value = t.precio.toFixed(2);

            console.log('[DEBUG] Tratamiento seleccionado:', t);
            calcularTotales();
        } else {
            row.querySelector('.tratamiento-id-input').value = '';
        }
    }

    function calcularTotales() {
        const rows = document.querySelectorAll('.detalle-row');
        let subtotalGeneral = 0;

        rows.forEach(row => {
            const cantidad = parseFloat(row.querySelector('.cantidad-input').value) || 0;
            const precioUnitario = parseFloat(row.querySelector('.precio-input').value) || 0;
            const subtotal = cantidad * precioUnitario;

            row.querySelector('.subtotal-input').value = formatCurrency(subtotal);
            subtotalGeneral += subtotal;
        });

        const descuento = parseFloat(document.getElementById('descuento').value) || 0;
        const baseImponible = subtotalGeneral - descuento;
        const iva = baseImponible * 0.15;
        const total = baseImponible + iva;

        document.getElementById('subtotal_display').textContent = formatCurrency(subtotalGeneral);
        document.getElementById('descuento_display').textContent = formatCurrency(descuento);
        document.getElementById('iva_display').textContent = formatCurrency(iva);
        document.getElementById('total_display').textContent = formatCurrency(total);

        document.getElementById('subtotal').value = subtotalGeneral.toFixed(2);
    }

    function formatCurrency(amount) {
        return '$' + parseFloat(amount).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    }

    // INICIALIZACIÓN
    window.addEventListener('load', function() {
        console.log('[DEBUG] Página cargada, iniciando...');

        <%
        if (esEdicion && detalles != null && !detalles.isEmpty()) {
            for (DetalleFactura d : detalles) {
        %>
        agregarDetalle(
            <%= d.getTratamientoId() != null ? d.getTratamientoId() : "null" %>,
            '<%= d.getDescripcion().replace("'", "\\'") %>',
            <%= d.getCantidad() %>,
            <%= d.getPrecioUnitario() %>
        );
        <%
            }
        } else {
        %>
        console.log('[DEBUG] Agregando primera línea vacía...');
        agregarDetalle();
        <%
        }
        %>

        calcularTotales();
        console.log('[DEBUG] Inicialización completada');
    });

    // VALIDACIÓN
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('facturaForm').addEventListener('submit', function(e) {
            const detalles = document.querySelectorAll('.detalle-row');
            if (detalles.length === 0) {
                e.preventDefault();
                alert('Debe agregar al menos un detalle a la factura');
                return false;
            }

            const subtotal = parseFloat(document.getElementById('subtotal').value) || 0;
            if (subtotal <= 0) {
                e.preventDefault();
                alert('El subtotal debe ser mayor a cero');
                return false;
            }
        });
    });
</script>
</body>
</html>
