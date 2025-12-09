package models;

import java.sql.Date;
import java.sql.Timestamp;
/**
 * =============================================================================
 * MODELO: Odontologo.java
 * =============================================================================
 * Descripción: Representa un odontólogo del sistema con datos profesionales
 * y personales. Se vincula con un usuario para permitir acceso al sistema.
 * =============================================================================
 */

// DATOS DE CONTACTO:
// - telefono, celular, email, direccion: Información de contacto
// - fechaNacimiento, genero: Datos demográficos

public class Odontologo {
    private int odontologoId;
    private Integer usuarioId;
    private int especialidadId;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String numRegistro;
    private String telefono;
    private String celular;
    private String email;
    private String direccion;
    private Date fechaNacimiento;
    private String genero;
// - activo: Boolean para eliminación lógica
// - fechaCreacion: Timestamp automático de creación
    private boolean activo;
    private Timestamp fechaCreacion;
    private Integer usuario_Id;

    // CAMPOS ADICIONALES (JOIN):
// - especialidadNombre: Nombre de la especialidad (ej: Ortodoncia)
// - nombreUsuario: Nombre de usuario para login
    private String especialidadNombre;
    private String nombreUsuario;

    // Constructor vacío
    public Odontologo() {
    }

    // Constructor básico
    public Odontologo(String nombres, String apellidos, String cedula, int especialidadId) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.especialidadId = especialidadId;
        this.activo = true;
    }

    // Getters y Setters
    public int getOdontologoId() {
        return odontologoId;
    }

    public void setOdontologoId(int odontologoId) {
        this.odontologoId = odontologoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(String numRegistro) {
        this.numRegistro = numRegistro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEspecialidadNombre() {
        return especialidadNombre;
    }

    public void setEspecialidadNombre(String especialidadNombre) {
        this.especialidadNombre = especialidadNombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // MÉTODOS HELPER:
// - getNombreCompleto(): Retorna "Dr(a). " + nombres + " " + apellidos
    public String getNombreCompleto() {
        return "Dr(a). " + nombres + " " + apellidos;
    }

    public Integer getUsuario_Id() {
        return usuario_Id;
    }

    public void setUsuario_Id(Integer usuario_Id) {
        this.usuarioId = usuario_Id;
    }
}