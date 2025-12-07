package models;

public class Diente {
    private int dienteId;
    private int odontogramaId;
    private String numeroDiente;
    private String estado;
    private String observaciones;

    // Constructor vacío
    public Diente() {
    }

    // Constructor con parámetros
    public Diente(int odontogramaId, String numeroDiente, String estado) {
        this.odontogramaId = odontogramaId;
        this.numeroDiente = numeroDiente;
        this.estado = estado;
    }

    // Getters y Setters
    public int getDienteId() {
        return dienteId;
    }

    public void setDienteId(int dienteId) {
        this.dienteId = dienteId;
    }

    public int getOdontogramaId() {
        return odontogramaId;
    }

    public void setOdontogramaId(int odontogramaId) {
        this.odontogramaId = odontogramaId;
    }

    public String getNumeroDiente() {
        return numeroDiente;
    }

    public void setNumeroDiente(String numeroDiente) {
        this.numeroDiente = numeroDiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Métodos auxiliares
    public boolean esSano() {
        return "Sano".equals(estado);
    }

    public boolean tieneCaries() {
        return "Caries".equals(estado);
    }

    public boolean estaObturado() {
        return "Obturado".equals(estado);
    }

    public boolean tieneEndodoncia() {
        return "Endodoncia".equals(estado);
    }

    public boolean tieneCorona() {
        return "Corona".equals(estado);
    }

    public boolean estaExtraido() {
        return "Extraccion".equals(estado);
    }

    public boolean estaAusente() {
        return "Ausente".equals(estado);
    }

    public boolean esImplante() {
        return "Implante".equals(estado);
    }

    public String getColorEstado() {
        if (estado == null) return "#10B981"; // Verde (sano)

        switch (estado) {
            case "Sano": return "#10B981";
            case "Caries": return "#EF4444";
            case "Obturado": return "#3B82F6";
            case "Endodoncia": return "#8B5CF6";
            case "Corona": return "#F59E0B";
            case "Extraccion": return "#6B7280";
            case "Ausente": return "#E5E7EB";
            case "Implante": return "#14B8A6";
            default: return "#10B981";
        }
    }
}