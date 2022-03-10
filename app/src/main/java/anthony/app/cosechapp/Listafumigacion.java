package anthony.app.cosechapp;

public class Listafumigacion {
    private String fumigacion ,hora,fecha,id;



    public Listafumigacion(String fumigacion, String hora, String fecha,String id) {
        this.fumigacion = fumigacion;
        this.hora = hora;
        this.fecha = fecha;
        this.id = id;
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
