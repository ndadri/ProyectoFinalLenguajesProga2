package models;
/**
 * =============================================================================
 * MODELO: Especialidad.java
 * =============================================================================
 * Descripción: Representa una especialidad odontológica. Se usa para
 * categorizar a los odontólogos del sistema.
 * =============================================================================
 */
public class Especialidad {
    private int especialidadId;
    private String nombre;
    private String descripcion;
    private boolean activo;

    // Constructor vacío
    public Especialidad() {
    }

    // Constructor con parámetros
    public Especialidad(int especialidadId, String nombre) {
        this.especialidadId = especialidadId;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}