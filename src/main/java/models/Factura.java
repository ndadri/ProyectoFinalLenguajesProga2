package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factura {

    // Campos de la tabla
    private int factura_id;
    private int paciente_id;
    private Integer consulta_id;
    private LocalDateTime fecha_emision;
    private String numero_factura;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal iva;
    private BigDecimal total;
    private String estado;
    private String metodo_pago;
    private String observaciones;

    // Campos adicionales (JOIN)
    private String paciente_nombre;
    private String paciente_cedula;
    private String paciente_telefono;
    private BigDecimal total_pagado;

    // Constructor vacío
    public Factura() {
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.iva = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.total_pagado = BigDecimal.ZERO;
    }

    // Getters y Setters
    public int getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(int factura_id) {
        this.factura_id = factura_id;
    }

    public int getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(int paciente_id) {
        this.paciente_id = paciente_id;
    }

    public Integer getConsulta_id() {
        return consulta_id;
    }

    public void setConsulta_id(Integer consulta_id) {
        this.consulta_id = consulta_id;
    }

    public LocalDateTime getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDateTime fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPaciente_nombre() {
        return paciente_nombre;
    }

    public void setPaciente_nombre(String paciente_nombre) {
        this.paciente_nombre = paciente_nombre;
    }

    public String getPaciente_cedula() {
        return paciente_cedula;
    }

    public void setPaciente_cedula(String paciente_cedula) {
        this.paciente_cedula = paciente_cedula;
    }

    public String getPaciente_telefono() {
        return paciente_telefono;
    }

    public void setPaciente_telefono(String paciente_telefono) {
        this.paciente_telefono = paciente_telefono;
    }

    public BigDecimal getTotal_pagado() {
        return total_pagado;
    }

    public void setTotal_pagado(BigDecimal total_pagado) {
        this.total_pagado = total_pagado;
    }

    // ========== MÉTODOS CALCULADOS ==========

    public void calcularTotales() {
        BigDecimal baseImponible = this.subtotal.subtract(this.descuento);
        this.iva = baseImponible.multiply(new BigDecimal("0.15"));
        this.total = baseImponible.add(this.iva);
    }

    public BigDecimal getSaldoPendiente() {
        return this.total.subtract(this.total_pagado);
    }

    public boolean isPagada() {
        return "Pagada".equals(this.estado);
    }

    public boolean isPendiente() {
        return "Pendiente".equals(this.estado);
    }

    public boolean isParcial() {
        return "Parcial".equals(this.estado);
    }

    // ========== MÉTODOS DE FORMATEO ==========

    public String getFechaFormateada() {
        if (fecha_emision == null) return "";
        return fecha_emision.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getFechaHoraFormateada() {
        if (fecha_emision == null) return "";
        return fecha_emision.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getSubtotalFormateado() {
        return formatearMoneda(subtotal);
    }

    public String getDescuentoFormateado() {
        return formatearMoneda(descuento);
    }

    public String getIvaFormateado() {
        return formatearMoneda(iva);
    }

    public String getTotalFormateado() {
        return formatearMoneda(total);
    }

    public String getTotalPagadoFormateado() {
        return formatearMoneda(total_pagado);
    }

    public String getSaldoPendienteFormateado() {
        return formatearMoneda(getSaldoPendiente());
    }

    private String formatearMoneda(BigDecimal monto) {
        if (monto == null) return "$0.00";
        return String.format("$%.2f", monto);
    }

    public String getEstadoColor() {
        if (estado == null) return "secondary";
        switch (estado) {
            case "Pagada": return "success";
            case "Pendiente": return "warning";
            case "Parcial": return "info";
            case "Cancelada": return "danger";
            default: return "secondary";
        }
    }

    // ========== MÉTODOS ALIAS PARA COMPATIBILIDAD CON JSP (camelCase) ==========

    public String getNumeroFactura() {
        return numero_factura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numero_factura = numeroFactura;
    }

    public int getFacturaId() {
        return factura_id;
    }

    public void setFacturaId(int facturaId) {
        this.factura_id = facturaId;
    }

    public int getPacienteId() {
        return paciente_id;
    }

    public void setPacienteId(int pacienteId) {
        this.paciente_id = pacienteId;
    }

    public Integer getConsultaId() {
        return consulta_id;
    }

    public void setConsultaId(Integer consultaId) {
        this.consulta_id = consultaId;
    }

    public String getPacienteNombre() {
        return paciente_nombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.paciente_nombre = pacienteNombre;
    }

    public String getPacienteCedula() {
        return paciente_cedula;
    }

    public void setPacienteCedula(String pacienteCedula) {
        this.paciente_cedula = pacienteCedula;
    }

    public String getPacienteTelefono() {
        return paciente_telefono;
    }

    public void setPacienteTelefono(String pacienteTelefono) {
        this.paciente_telefono = pacienteTelefono;
    }

    public BigDecimal getTotalPagado() {
        return total_pagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.total_pagado = totalPagado;
    }

    public LocalDateTime getFechaEmision() {
        return fecha_emision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fecha_emision = fechaEmision;
    }

    public String getMetodoPago() {
        return metodo_pago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodo_pago = metodoPago;
    }
}