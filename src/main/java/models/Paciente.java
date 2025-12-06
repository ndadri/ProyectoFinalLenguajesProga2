package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Paciente {
    private int pacienteId;
    private String nombres;
    private String apellidos;
    private String cedula;
    private Date fechaNacimiento;
    private int edad;
    private String genero;
    private String telefono;
    private String email;
    private String direccion;
    private String ocupacion;

    // Contacto emergencia
    private String contactoEmergencia;
    private String telefonoEmergencia;

    // Historial médico
    private String alergias;
    private String enfermedadesSistemicas;
    private String medicamentosActuales;
    private Boolean embarazada;

    // Sistema
    private boolean activo;
    private String notas;
    private Timestamp fechaRegistro;
    private Date ultimaVisita;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor básico
    public Paciente(String nombres, String apellidos, String cedula, Date fechaNacimiento) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = true;
    }

    // Getters y Setters
    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedadesSistemicas() {
        return enfermedadesSistemicas;
    }

    public void setEnfermedadesSistemicas(String enfermedadesSistemicas) {
        this.enfermedadesSistemicas = enfermedadesSistemicas;
    }

    public String getMedicamentosActuales() {
        return medicamentosActuales;
    }

    public void setMedicamentosActuales(String medicamentosActuales) {
        this.medicamentosActuales = medicamentosActuales;
    }

    public Boolean getEmbarazada() {
        return embarazada;
    }

    public void setEmbarazada(Boolean embarazada) {
        this.embarazada = embarazada;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getUltimaVisita() {
        return ultimaVisita;
    }

    public void setUltimaVisita(Date ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}