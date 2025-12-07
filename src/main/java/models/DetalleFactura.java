package models;

import java.math.BigDecimal;

public class DetalleFactura {

    private int detalle_id;
    private int factura_id;
    private Integer tratamiento_id;
    private String descripcion;
    private int cantidad;
    private BigDecimal precio_unitario;
    private BigDecimal subtotal;

    // Campos adicionales (JOIN)
    private String tratamiento_nombre;
    private String tratamiento_categoria;

    // Constructor
    public DetalleFactura() {
        this.cantidad = 1;
        this.precio_unitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    // Getters y Setters
    public int getDetalle_id() {
        return detalle_id;
    }

    public void setDetalle_id(int detalle_id) {
        this.detalle_id = detalle_id;
    }

    public int getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(int factura_id) {
        this.factura_id = factura_id;
    }

    public Integer getTratamiento_id() {
        return tratamiento_id;
    }

    public void setTratamiento_id(Integer tratamiento_id) {
        this.tratamiento_id = tratamiento_id;
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
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getTratamiento_nombre() {
        return tratamiento_nombre;
    }

    public void setTratamiento_nombre(String tratamiento_nombre) {
        this.tratamiento_nombre = tratamiento_nombre;
    }

    public String getTratamiento_categoria() {
        return tratamiento_categoria;
    }

    public void setTratamiento_categoria(String tratamiento_categoria) {
        this.tratamiento_categoria = tratamiento_categoria;
    }

    // ========== MÉTODOS ==========

    public void calcularSubtotal() {
        this.subtotal = this.precio_unitario.multiply(new BigDecimal(this.cantidad));
    }

    public String getPrecioUnitarioFormateado() {
        return formatearMoneda(precio_unitario);
    }

    public String getSubtotalFormateado() {
        return formatearMoneda(subtotal);
    }

    private String formatearMoneda(BigDecimal monto) {
        if (monto == null) return "$0.00";
        return String.format("$%.2f", monto);
    }
    // ========== MÉTODOS ALIAS PARA COMPATIBILIDAD CON JSP (camelCase) ==========

    public int getDetalleId() {
        return detalle_id;
    }

    public void setDetalleId(int detalleId) {
        this.detalle_id = detalleId;
    }

    public int getFacturaId() {
        return factura_id;
    }

    public void setFacturaId(int facturaId) {
        this.factura_id = facturaId;
    }

    public Integer getTratamientoId() {
        return tratamiento_id;
    }

    public void setTratamientoId(Integer tratamientoId) {
        this.tratamiento_id = tratamientoId;
    }

    public BigDecimal getPrecioUnitario() {
        return precio_unitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precio_unitario = precioUnitario;
    }

    public String getTratamientoNombre() {
        return tratamiento_nombre;
    }

    public void setTratamientoNombre(String tratamientoNombre) {
        this.tratamiento_nombre = tratamientoNombre;
    }

    public String getTratamientoCategoria() {
        return tratamiento_categoria;
    }

    public void setTratamientoCategoria(String tratamientoCategoria) {
        this.tratamiento_categoria = tratamientoCategoria;
    }
}