package anthony.app.cosechapp;

public class Listausu {
    private String nombreusu,rolusu,id_usuario;
    private int image;


    public Listausu(String nombreusu, String rolusu, int image, String id_usuario){
        this.nombreusu=nombreusu;
        this.rolusu=rolusu;
        this.image=image;
        this.id_usuario=id_usuario;
    }
    public String getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
    public String getNombreusu() {
        return nombreusu;
    }
    public void setNombreusu(String nombreusu) {
        this.nombreusu = nombreusu;
    }
    public String getRolusu() {
        return rolusu;
    }
    public void setRolusu(String rolusu) {
        this.rolusu = rolusu;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }








}
