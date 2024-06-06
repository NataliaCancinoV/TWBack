package mx.uv;

public class Reseñas {

    String idReseña;
    String nombreUsuario;
    String idPastel;
    String contenido;
    int estrellas;

    public String getIdReseña() {
        return idReseña;
    }

    public void setIdReseña(String idReseña) {
        this.idReseña = idReseña;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getIdPastel() {
        return idPastel;
    }

    public void setIdPastel(String idPastel) {
        this.idPastel = idPastel;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public String toString() {
        return "Reseñas [idReseña=" + idReseña + ", nombreUsuario=" + nombreUsuario + ", idPastel=" + idPastel + ", contenido=" + contenido + ", estrellas=" + estrellas + "]";
    }

    public Reseñas(){

    }

    public Reseñas(String idReseña, String nombreUsuario, String idPastel, String contenido, int estrellas) {
        this.idReseña = idReseña;
        this.nombreUsuario = nombreUsuario;
        this.idPastel = idPastel;
        this.contenido = contenido;
        this.estrellas = estrellas;
    }  
}

