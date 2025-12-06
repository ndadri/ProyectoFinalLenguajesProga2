package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Consulta {
    private int consultaId;
    private Integer citaId;
    private int pacienteId;
    private int odontologoId;
    private Timestamp fechaConsulta;

    // Motivo y diagnóstico
    private String motivoConsulta;
    private String sintomas;
    private String diagnostico;

    // Tratamiento
    private String tratamiento;
    private String dientesTratados;
    private String procedimientos;

    // Observaciones
    private String observaciones;
    private String pronostico;
    private Date proximaCita;
    private boolean requiereSeguimiento;

    private Timestamp fechaCreacion;

    // Campos adicionales para mostrar
    private String pacienteNombre;
    private String pacienteCedula;
    private String odontologoNombre;

    // Constructor vacío
    public Consulta() {
    }

    // Constructor básico
    public Consulta(int pacienteId, int odontologoId, String motivoConsulta, String diagnostico, String tratamiento) {
        this.pacienteId = pacienteId;
        this.odontologoId = odontologoId;
        this.motivoConsulta = motivoConsulta;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    // Getters y Setters
    public int getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(int consultaId) {
        this.consultaId = consultaId;
    }

    public Integer getCitaId() {
        return citaId;
    }

    public void setCitaId(Integer citaId) {
        this.citaId = citaId;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public int getOdontologoId() {
        return odontologoId;
    }

    public void setOdontologoId(int odontologoId) {
        this.odontologoId = odontologoId;
    }

    public Timestamp getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Timestamp fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDientesTratados() {
        return dientesTratados;
    }

    public void setDientesTratados(String dientesTratados) {
        this.dientesTratados = dientesTratados;
    }

    public String getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(String procedimientos) {
        this.procedimientos = procedimientos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPronostico() {
        return pronostico;
    }

    public void setPronostico(String pronostico) {
        this.pronostico = pronostico;
    }

    public Date getProximaCita() {
        return proximaCita;
    }

    public void setProximaCita(Date proximaCita) {
        this.proximaCita = proximaCita;
    }

    public boolean isRequiereSeguimiento() {
        return requiereSeguimiento;
    }

    public void setRequiereSeguimiento(boolean requiereSeguimiento) {
        this.requiereSeguimiento = requiereSeguimiento;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getPacienteCedula() {
        return pacienteCedula;
    }

    public void setPacienteCedula(String pacienteCedula) {
        this.pacienteCedula = pacienteCedula;
    }

    public String getOdontologoNombre() {
        return odontologoNombre;
    }

    public void setOdontologoNombre(String odontologoNombre) {
        this.odontologoNombre = odontologoNombre;
    }
}