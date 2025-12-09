package models;

import java.sql.Timestamp;
/**
 * =============================================================================
 * MODELO: Usuario.java
 * =============================================================================
 * Descripción: Representa un usuario del sistema con credenciales de acceso.
 * Define roles y permisos. Los odontólogos tienen una relación especial con
 * la tabla odontologo a través de odontologoId.
 * =============================================================================
 */
public class Usuario {
    private int usuarioId;
    private int tipoId;
    private String usuario;
    private String password;
    private String email;
    private boolean activo;
    private Timestamp ultimoAcceso;
    private Timestamp fechaCreacion;
    private Integer odontologoId; // Solo si es odontólogo
    private String tipoNombre; // Alias para compatibilidad

    // Campos adicionales para mostrar
    private String tipoUsuarioNombre;

    // Constructores
    public Usuario() {
    }

    public Usuario(int usuarioId, String usuario, String email, int tipoId) {
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.email = email;
        this.tipoId = tipoId;
    }

    // Getters y Setters
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getTipoUsuarioNombre() {
        return tipoUsuarioNombre;
    }

    public void setTipoUsuarioNombre(String tipoUsuarioNombre) {
        this.tipoUsuarioNombre = tipoUsuarioNombre;
    }

    public Integer getOdontologoId() {
        return odontologoId;
    }

    public void setOdontologoId(Integer odontologoId) {
        this.odontologoId = odontologoId;
    }

    public String getTipoNombre() {
        return tipoUsuarioNombre; // Usa el campo existente
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoUsuarioNombre = tipoNombre;
    }

    // Agregar estos métodos de validación de roles:
    public boolean isAdmin() {
        return tipoId == 1;
    }

    public boolean isOdontologo() {
        return tipoId == 2;
    }

    public boolean isRecepcion() {
        return tipoId == 4;
    }

    public boolean tieneAccesoTotal() {
        return isAdmin() || isRecepcion();
    }

    public boolean puedeVerTodo() {
        return tieneAccesoTotal();
    }
}