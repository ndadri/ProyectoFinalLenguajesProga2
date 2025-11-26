package models;

public class Cita {
    private int id;
    private int pacienteId;
    private String fecha;
    private String motivo;

    public Cita() {}

    public Cita(int id, int pacienteId, String fecha, String motivo) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
