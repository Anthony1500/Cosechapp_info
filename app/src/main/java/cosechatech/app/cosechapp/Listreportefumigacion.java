package cosechatech.app.cosechapp;

public class Listreportefumigacion {
    private String fecha,hora,invernadero,tratamiento,encargado;


    public Listreportefumigacion(String fecha, String hora, String invernadero, String tratamiento, String encargado) {
        this.fecha = fecha;
        this.hora = hora;
        this.invernadero = invernadero;
        this.tratamiento = tratamiento;
        this.encargado = encargado;
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

    public String getInvernadero() {
        return invernadero;
    }

    public void setInvernadero(String invernadero) {
        this.invernadero = invernadero;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }
}
