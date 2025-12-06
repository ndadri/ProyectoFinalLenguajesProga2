package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {

    private int factura_id;
    private int paciente_id;
    private int consulta_id;

    private LocalDateTime fecha_emision;
    private String numero_factura;

    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal iva;
    private BigDecimal total;

    private String estado;       // 'Pendiente', 'Pagada', 'Anulada'
    private String metodo_pago;  // 'Efectivo', 'Tarjeta', 'Transferencia'
    private String observaciones;

    public Factura() {}

    // =======================
    // Getters y Setters
    // =======================

    public int getFactura_id() { return factura_id; }
    public void setFactura_id(int factura_id) { this.factura_id = factura_id; }

    public int getPaciente_id() { return paciente_id; }
    public void setPaciente_id(int paciente_id) { this.paciente_id = paciente_id; }

    public int getConsulta_id() { return consulta_id; }
    public void setConsulta_id(int consulta_id) { this.consulta_id = consulta_id; }

    public LocalDateTime getFecha_emision() { return fecha_emision; }
    public void setFecha_emision(LocalDateTime fecha_emision) { this.fecha_emision = fecha_emision; }

    public String getNumero_factura() { return numero_factura; }
    public void setNumero_factura(String numero_factura) { this.numero_factura = numero_factura; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodo_pago() { return metodo_pago; }
    public void setMetodo_pago(String metodo_pago) { this.metodo_pago = metodo_pago; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    // =======================
    // Helpers (para JSP)
    // =======================

    public String getEstadoTexto() {
        return switch (estado) {
            case "Pendiente" -> "Pendiente de pago";
            case "Pagada" -> "Pagada";
            case "Anulada" -> "Factura anulada";
            default -> "Desconocido";
        };
    }
}
