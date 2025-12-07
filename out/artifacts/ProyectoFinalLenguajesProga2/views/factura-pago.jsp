<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:23 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Factura" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Pago - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <%
        Factura factura = (Factura) request.getAttribute("factura");
    %>

    <div class="page-header">
        <div>
            <h1 class="page-title">
                <i class="fas fa-dollar-sign"></i> Registrar Pago
            </h1>
            <p class="page-subtitle">Factura <%= factura.getNumeroFactura() %></p>
        </div>
        <a href="factura?action=ver&id=<%= factura.getFacturaId() %>" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver
        </a>
    </div>

    <div class="card" style="max-width: 800px; margin: 0 auto;">
        <div class="card-body">
            <div class="factura-info">
                <h3 style="color: #374151; margin-bottom: 1rem;">
                    <i class="fas fa-info-circle"></i> Información de la Factura
                </h3>

                <div class="info-row">
                    <span style="color: #6B7280;">Paciente:</span>
                    <strong><%= factura.getPacienteNombre() %></strong>
                </div>
                <div class="info-row">
                    <span style="color: #6B7280;">Total Factura:</span>
                    <strong><%= factura.getTotalFormateado() %></strong>
                </div>
                <div class="info-row">
                    <span style="color: #6B7280;">Total Pagado:</span>
                    <strong style="color: #10B981;"><%= factura.getTotalPagadoFormateado() %></strong>
                </div>
                <div class="info-row" style="background: #FEF3C7; padding: 0.75rem; border-radius: 8px; margin-top: 0.5rem;">
                    <span style="color: #92400E; font-weight: 600;">Saldo Pendiente:</span>
                    <strong style="color: #92400E; font-size: 20px;">
                        <%= factura.getSaldoPendienteFormateado() %>
                    </strong>
                </div>
            </div>

            <form action="factura" method="post" id="pagoForm">
                <input type="hidden" name="action" value="registrar_pago">
                <input type="hidden" name="factura_id" value="<%= factura.getFacturaId() %>">

                <h3 style="color: #374151; margin-bottom: 1rem;">
                    <i class="fas fa-money-bill-wave"></i> Datos del Pago
                </h3>

                <div class="form-group">
                    <label for="monto">Monto a Pagar * <small style="color: #6B7280;">(Máximo: <%= factura.getSaldoPendienteFormateado() %>)</small></label>
                    <input type="number" id="monto" name="monto" class="form-control" step="0.01" min="0.01"
                           max="<%= factura.getSaldoPendiente() %>" required
                           style="font-size: 18px; font-weight: 600;" placeholder="0.00">

                    <div class="monto-sugerido">
                        <button type="button" class="btn-monto" onclick="setMonto(<%= factura.getSaldoPendiente() %>)">
                            Pago Total (<%= factura.getSaldoPendienteFormateado() %>)
                        </button>
                        <%
                            double saldo = factura.getSaldoPendiente().doubleValue();
                            if (saldo >= 100) {
                        %>
                        <button type="button" class="btn-monto" onclick="setMonto(50)">$50.00</button>
                        <button type="button" class="btn-monto" onclick="setMonto(100)">$100.00</button>
                        <% } %>
                        <% if (saldo >= 10) { %>
                        <button type="button" class="btn-monto" onclick="setMonto(10)">$10.00</button>
                        <button type="button" class="btn-monto" onclick="setMonto(20)">$20.00</button>
                        <% } %>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label for="metodo_pago">Método de Pago *</label>
                        <select id="metodo_pago" name="metodo_pago" class="form-control" required onchange="toggleReferencia()">
                            <option value="Efectivo">Efectivo</option>
                            <option value="Tarjeta">Tarjeta de Crédito/Débito</option>
                            <option value="Transferencia">Transferencia Bancaria</option>
                            <option value="Cheque">Cheque</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="referencia">Referencia / Nº Transacción</label>
                        <input type="text" id="referencia" name="referencia" class="form-control"
                               placeholder="Número de transacción o cheque">
                    </div>
                </div>

                <div class="form-group">
                    <label for="recibido_por">Recibido Por *</label>
                    <input type="text" id="recibido_por" name="recibido_por" class="form-control" required
                           placeholder="Nombre de quien recibe el pago"
                           value="<%= session.getAttribute("nombreUsuario") != null ? session.getAttribute("nombreUsuario") : "" %>">
                </div>

                <div class="form-group">
                    <label for="observaciones">Observaciones</label>
                    <textarea id="observaciones" name="observaciones" class="form-control" rows="3"
                              placeholder="Notas adicionales sobre el pago (opcional)"></textarea>
                </div>

                <div style="background: #EFF6FF; border: 2px solid #3B82F6; padding: 1.5rem; border-radius: 12px; margin: 2rem 0;">
                    <h4 style="color: #1E40AF; margin-bottom: 1rem;">
                        <i class="fas fa-calculator"></i> Resumen del Pago
                    </h4>
                    <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                        <span>Monto a pagar:</span>
                        <strong id="monto_display" style="font-size: 18px; color: #1E40AF;">$0.00</strong>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                        <span>Nuevo total pagado:</span>
                        <strong id="nuevo_total_pagado" style="color: #10B981;">
                            <%= factura.getTotalPagadoFormateado() %>
                        </strong>
                    </div>
                    <div style="display: flex; justify-content: space-between; padding-top: 0.5rem; border-top: 2px solid #3B82F6;">
                        <span style="font-weight: 600;">Nuevo saldo pendiente:</span>
                        <strong id="nuevo_saldo" style="font-size: 18px; color: #EF4444;">
                            <%= factura.getSaldoPendienteFormateado() %>
                        </strong>
                    </div>
                </div>

                <div style="display: flex; gap: 1rem; justify-content: center;">
                    <button type="submit" class="btn btn-primary" style="min-width: 200px;">
                        <i class="fas fa-check"></i> Confirmar Pago
                    </button>
                    <a href="factura?action=ver&id=<%= factura.getFacturaId() %>" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Cancelar
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const totalFactura = <%= factura.getTotal() %>;
    const totalPagado = <%= factura.getTotalPagado() %>;
    const saldoPendiente = <%= factura.getSaldoPendiente() %>;

    document.getElementById('monto').addEventListener('input', function() {
        actualizarResumen();
    });

    function setMonto(valor) {
        document.getElementById('monto').value = valor.toFixed(2);
        actualizarResumen();

        document.querySelectorAll('.btn-monto').forEach(btn => {
            btn.classList.remove('active');
        });
        event.target.classList.add('active');
    }

    function actualizarResumen() {
        const monto = parseFloat(document.getElementById('monto').value) || 0;

        if (monto > saldoPendiente) {
            document.getElementById('monto').value = saldoPendiente.toFixed(2);
            return;
        }

        const nuevoTotalPagado = totalPagado + monto;
        const nuevoSaldo = saldoPendiente - monto;

        document.getElementById('monto_display').textContent = formatCurrency(monto);
        document.getElementById('nuevo_total_pagado').textContent = formatCurrency(nuevoTotalPagado);
        document.getElementById('nuevo_saldo').textContent = formatCurrency(nuevoSaldo);

        const saldoElement = document.getElementById('nuevo_saldo');
        saldoElement.style.color = nuevoSaldo === 0 ? '#10B981' : '#EF4444';
    }

    function formatCurrency(amount) {
        return '$' + parseFloat(amount).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    function toggleReferencia() {
        const metodo = document.getElementById('metodo_pago').value;
        const referenciaInput = document.getElementById('referencia');

        if (metodo === 'Efectivo') {
            referenciaInput.required = false;
            referenciaInput.parentElement.style.opacity = '0.5';
        } else {
            referenciaInput.required = true;
            referenciaInput.parentElement.style.opacity = '1';
        }
    }

    document.getElementById('pagoForm').addEventListener('submit', function(e) {
        const monto = parseFloat(document.getElementById('monto').value);

        if (monto <= 0) {
            e.preventDefault();
            alert('El monto debe ser mayor a $0.00');
            return false;
        }

        if (monto > saldoPendiente) {
            e.preventDefault();
            alert('El monto no puede ser mayor al saldo pendiente');
            return false;
        }

        return confirm('¿Confirmar el registro de este pago?');
    });

    toggleReferencia();
</script>
</body>
</html>