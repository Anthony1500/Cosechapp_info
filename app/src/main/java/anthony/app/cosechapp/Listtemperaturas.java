package anthony.app.cosechapp;

public class Listtemperaturas {

    private String temperaturaaire,humedadaire,humedad,fecha,hora;


    public Listtemperaturas(String temperaturaaire, String humedadaire, String humedad, String fecha, String hora) {
        this.temperaturaaire = temperaturaaire;
        this.humedadaire = humedadaire;
        this.humedad = humedad;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getTemperaturaaire() {
        return temperaturaaire;
    }

    public void setTemperaturaaire(String temperaturaaire) {
        this.temperaturaaire = temperaturaaire;
    }

    public String getHumedadaire() {
        return humedadaire;
    }

    public void setHumedadaire(String humedadaire) {
        this.humedadaire = humedadaire;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
