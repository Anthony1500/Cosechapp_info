package anthony.app.cosechapp;

public class Listausu {
    private String nombreusu,rolusu;
    private int image;


    public Listausu(String nombreusu, String rolusu, int image){
        this.nombreusu=nombreusu;
        this.rolusu=rolusu;
        this.image=image;
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
