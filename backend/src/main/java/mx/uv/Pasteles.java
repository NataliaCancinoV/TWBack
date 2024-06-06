package mx.uv;

public class Pasteles {

    String idPedido;
    String idUsuario;
    String idPastel;
    String idNombre;
    String idPrecio;
    String idTamaño;
    String status;
    String inscripcion;
    String tipoRelleno;

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdPastel() {
        return idPastel;
    }

    public void setIdPastel(String idPastel) {
        this.idPastel = idPastel;
    }

    public String getIdNombre() {
        return idNombre;
    }
    
    public void setIdNombre(String idNombre) {
        this.idNombre = idNombre;
    }

    public String getIdPrecio() {
        return idPrecio;
    }

    public void setIdPrecio(String idPrecio) {
        this.idPrecio = idPrecio;
    }

    public String getIdTamaño() {
        return idTamaño;
    }

    public void setIdTamaño(String idTamaño) {
        this.idTamaño = idTamaño;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(String inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getTipoRelleno() {
        return tipoRelleno;
    }

    public void setTipoRelleno(String tipoRelleno) {
        this.tipoRelleno = tipoRelleno;
    }

    public String toString() {
        return "Pasteles [idPedido=" + idPedido + ", idUsuario=" + idUsuario + ", idPastel=" + idPastel + ", idNombre=" + idNombre + ", idPrecio=" + idPrecio
                + ", idTamaño=" + idTamaño + ", status=" + status + ", inscripcion=" + inscripcion + ", tipoRelleno=" + tipoRelleno + "]";
    }

    public Pasteles(){

    }

    public Pasteles(String idPedido, String idUsuario, String idPastel, String idNombre, String idPrecio, String idTamaño,
            String status, String inscripcion, String tipoRelleno) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.idPastel = idPastel;
        this.idNombre = idNombre;
        this.idPrecio = idPrecio;
        this.idTamaño = idTamaño;
        this.status = status;
        this.inscripcion = inscripcion;
        this.tipoRelleno = tipoRelleno;
    }  

    
}


