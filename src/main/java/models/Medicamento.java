package models;

import java.math.BigDecimal;

public class Medicamento {
    private int medicamentoId;
    private String nombre;
    private String descripcion;
    private String presentacion;
    private String concentracion;
    private BigDecimal precio;
    private int stock;
    private boolean activo;

    public Medicamento() {}

    // Getters y Setters
    public int getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(int medicamentoId) { this.medicamentoId = medicamentoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}