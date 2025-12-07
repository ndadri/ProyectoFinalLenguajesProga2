package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Odontograma {
    private int odontogramaId;
    private int pacienteId;
    private Date fechaCreacion;
    private String observaciones;

    // Campos adicionales (JOIN)
    private String pacienteNombre;
    private String pacienteCedula;

    // Constructor vacío
    public Odontograma() {
    }

    // Getters y Setters
    public int getOdontogramaId() {
        return odontogramaId;
    }

    public void setOdontogramaId(int odontogramaId) {
        this.odontogramaId = odontogramaId;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    // Métodos auxiliares
    public String getFechaFormateada() {
        if (fechaCreacion == null) return "";
        return fechaCreacion.toString();
    }
}