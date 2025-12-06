package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private int citaId;
    private int pacienteId;
    private int odontologoId;
    private Timestamp fechaHora;
    private int duracionMinutos;
    private String motivo;
    private String tipoCita;
    private String estado;
    private String consultorio;
    private String observaciones;
    private boolean recordatorioEnviado;
    private Timestamp fechaCreacion;

    // Campos adicionales para mostrar
    private String pacienteNombre;
    private String odontologoNombre;
    private String pacienteTelefono;
    private String pacienteCedula;

    // Constructores
    public Cita() {
    }

    public Cita(int pacienteId, int odontologoId, Timestamp fechaHora) {
        this.pacienteId = pacienteId;
        this.odontologoId = odontologoId;
        this.fechaHora = fechaHora;
        this.duracionMinutos = 30;
        this.tipoCita = "Primera_Vez";
        this.estado = "Programada";
    }

    // Getters y Setters
    public int getCitaId() {
        return citaId;
    }

    public void setCitaId(int citaId) {
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

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isRecordatorioEnviado() {
        return recordatorioEnviado;
    }

    public void setRecordatorioEnviado(boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
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

    public String getOdontologoNombre() {
        return odontologoNombre;
    }

    public void setOdontologoNombre(String odontologoNombre) {
        this.odontologoNombre = odontologoNombre;
    }

    public String getPacienteTelefono() {
        return pacienteTelefono;
    }

    public void setPacienteTelefono(String pacienteTelefono) {
        this.pacienteTelefono = pacienteTelefono;
    }

    public String getPacienteCedula() {
        return pacienteCedula;
    }

    public void setPacienteCedula(String pacienteCedula) {
        this.pacienteCedula = pacienteCedula;
    }

    // Métodos auxiliares
    public String getFechaFormateada() {
        if (fechaHora != null) {
            LocalDateTime ldt = fechaHora.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return ldt.format(formatter);
        }
        return "";
    }

    public String getHoraFormateada() {
        if (fechaHora != null) {
            LocalDateTime ldt = fechaHora.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return ldt.format(formatter);
        }
        return "";
    }

    public String getEstadoColor() {
        switch (estado) {
            case "Confirmada":
                return "success";
            case "Programada":
                return "info";
            case "En_Curso":
                return "warning";
            case "Completada":
                return "success";
            case "Cancelada":
                return "error";
            case "No_Asistio":
                return "error";
            default:
                return "info";
        }
    }
}