package models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistorialPago {
    private int pagoId;
    private int facturaId;
    private Timestamp fechaPago;
    private BigDecimal monto;
    private String metodoPago; // Efectivo, Tarjeta, Transferencia, Cheque
    private String referencia;
    private String recibidoPor;
    private String observaciones;

    // Campos adicionales para mostrar
    private String numeroFactura;
    private String pacienteNombre;

    // Constructor vacío
    public HistorialPago() {
        this.monto = BigDecimal.ZERO;
        this.metodoPago = "Efectivo";
    }

    // Constructor con parámetros
    public HistorialPago(int facturaId, BigDecimal monto, String metodoPago, String recibidoPor) {
        this.facturaId = facturaId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.recibidoPor = recibidoPor;
    }

    // Getters y Setters
    public int getPagoId() {
        return pagoId;
    }

    public void setPagoId(int pagoId) {
        this.pagoId = pagoId;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public Timestamp getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Timestamp fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getRecibidoPor() {
        return recibidoPor;
    }

    public void setRecibidoPor(String recibidoPor) {
        this.recibidoPor = recibidoPor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    // Métodos de utilidad para formato
    public String getFechaFormateada() {
        if (fechaPago == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fechaPago);
    }

    public String getFechaHoraFormateada() {
        if (fechaPago == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(fechaPago);
    }

    public String getMontoFormateado() {
        if (monto == null) return "$0.00";
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "EC"));
        return formatter.format(monto);
    }

    // Icono del método de pago
    public String getIconoMetodoPago() {
        if (metodoPago == null) return "fa-money-bill";

        switch (metodoPago) {
            case "Efectivo":
                return "fa-money-bill-wave";
            case "Tarjeta":
                return "fa-credit-card";
            case "Transferencia":
                return "fa-exchange-alt";
            case "Cheque":
                return "fa-money-check";
            default:
                return "fa-money-bill";
        }
    }
}