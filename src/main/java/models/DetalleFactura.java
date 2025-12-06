package models;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DetalleFactura {
    private int detalleId;
    private int facturaId;
    private Integer tratamientoId;
    private String descripcion;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Campos adicionales para mostrar
    private String tratamientoNombre;
    private String tratamientoCategoria;

    // Constructor vacío
    public DetalleFactura() {
        this.cantidad = 1;
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    // Constructor con parámetros
    public DetalleFactura(int facturaId, String descripcion, int cantidad, BigDecimal precioUnitario) {
        this.facturaId = facturaId;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    // Getters y Setters
    public int getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public Integer getTratamientoId() {
        return tratamientoId;
    }

    public void setTratamientoId(Integer tratamientoId) {
        this.tratamientoId = tratamientoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getTratamientoNombre() {
        return tratamientoNombre;
    }

    public void setTratamientoNombre(String tratamientoNombre) {
        this.tratamientoNombre = tratamientoNombre;
    }

    public String getTratamientoCategoria() {
        return tratamientoCategoria;
    }

    public void setTratamientoCategoria(String tratamientoCategoria) {
        this.tratamientoCategoria = tratamientoCategoria;
    }

    // Métodos de utilidad
    public void calcularSubtotal() {
        if (precioUnitario != null && cantidad > 0) {
            this.subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    // Formateo de moneda
    public String getPrecioUnitarioFormateado() {
        if (precioUnitario == null) return "$0.00";
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "EC"));
        return formatter.format(precioUnitario);
    }

    public String getSubtotalFormateado() {
        if (subtotal == null) return "$0.00";
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "EC"));
        return formatter.format(subtotal);
    }
}