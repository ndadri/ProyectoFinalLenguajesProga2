package models;

import java.math.BigDecimal;

public class TratamientoOdontologico {

    private int tratamiento_id;      // auto increment
    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;        // enum en BD, String aquí
    private BigDecimal precio_base;
    private int duracion_aproximada;
    private int requiere_anestesia;  // tinyint(1)
    private int activo;              // tinyint(1)

    public TratamientoOdontologico() {}

    // ====== GETTERS & SETTERS ======

    public int getTratamiento_id() {
        return tratamiento_id;
    }

    public void setTratamiento_id(int tratamiento_id) {
        this.tratamiento_id = tratamiento_id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio_base() {
        return precio_base;
    }

    public void setPrecio_base(BigDecimal precio_base) {
        this.precio_base = precio_base;
    }

    public int getDuracion_aproximada() {
        return duracion_aproximada;
    }

    public void setDuracion_aproximada(int duracion_aproximada) {
        this.duracion_aproximada = duracion_aproximada;
    }

    public int getRequiere_anestesia() {
        return requiere_anestesia;
    }

    public void setRequiere_anestesia(int requiere_anestesia) {
        this.requiere_anestesia = requiere_anestesia;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    // ===== Helper para vistas (JSP) =====
    public String getEstadoTexto() {
        return activo == 1 ? "Activo" : "Inactivo";
    }

    public String getAnestesiaTexto() {
        return requiere_anestesia == 1 ? "Sí" : "No";
    }
}
