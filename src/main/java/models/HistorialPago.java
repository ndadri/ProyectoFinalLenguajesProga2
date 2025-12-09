package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * =============================================================================
 * MODELO: HistorialPago.java
 * =============================================================================
 * Descripción: Representa un pago realizado contra una factura. Permite
 * pagos parciales y múltiples pagos para una misma factura.
 * =============================================================================
 */
public class HistorialPago {

    private int pago_id;
    private int factura_id;
    private LocalDateTime fecha_pago;
    private BigDecimal monto;
    private String metodo_pago;
    private String referencia;
    private String recibido_por;
    private String observaciones;

    // Constructor
    public HistorialPago() {
        this.monto = BigDecimal.ZERO;
    }

    // Getters y Setters
    public int getPago_id() {
        return pago_id;
    }

    public void setPago_id(int pago_id) {
        this.pago_id = pago_id;
    }

    public int getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(int factura_id) {
        this.factura_id = factura_id;
    }

    public LocalDateTime getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(LocalDateTime fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getRecibido_por() {
        return recibido_por;
    }

    public void setRecibido_por(String recibido_por) {
        this.recibido_por = recibido_por;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // ========== MÉTODOS DE FORMATEO ==========

    public String getFechaFormateada() {
        if (fecha_pago == null) return "";
        return fecha_pago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getFechaHoraFormateada() {
        if (fecha_pago == null) return "";
        return fecha_pago.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getMontoFormateado() {
        return formatearMoneda(monto);
    }

    private String formatearMoneda(BigDecimal monto) {
        if (monto == null) return "$0.00";
        return String.format("$%.2f", monto);
    }

    public String getIconoMetodoPago() {
        if (metodo_pago == null) return "fa-money-bill";
        switch (metodo_pago) {
            case "Efectivo": return "fa-money-bill-wave";
            case "Tarjeta": return "fa-credit-card";
            case "Transferencia": return "fa-exchange-alt";
            case "Cheque": return "fa-money-check";
            default: return "fa-dollar-sign";
        }
    }
    // ========== MÉTODOS ALIAS PARA COMPATIBILIDAD CON JSP (camelCase) ==========

    public int getPagoId() {
        return pago_id;
    }

    public void setPagoId(int pagoId) {
        this.pago_id = pagoId;
    }

    public int getFacturaId() {
        return factura_id;
    }

    public void setFacturaId(int facturaId) {
        this.factura_id = facturaId;
    }

    public LocalDateTime getFechaPago() {
        return fecha_pago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fecha_pago = fechaPago;
    }

    public String getMetodoPago() {
        return metodo_pago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodo_pago = metodoPago;
    }

    public String getRecibidoPor() {
        return recibido_por;
    }

    public void setRecibidoPor(String recibidoPor) {
        this.recibido_por = recibidoPor;
    }
}