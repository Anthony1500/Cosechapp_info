package cosechatech.app.cosechapp;

public class Listafumigacion {
    private String fumigacion ,hora,fecha,id,encargado,invernadero,tratamiento;



    public Listafumigacion(String fumigacion, String hora, String fecha,String id,String encargado, String invernadero, String tratamiento) {
        this.fumigacion = fumigacion;
        this.hora = hora;
        this.fecha = fecha;
        this.id = id;
        this.encargado= encargado;
        this.invernadero=invernadero;
        this.tratamiento=tratamiento;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
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

    public String getFumigacion() {
        return fumigacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFumigacion(String fumigacion) {
        this.fumigacion = fumigacion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
