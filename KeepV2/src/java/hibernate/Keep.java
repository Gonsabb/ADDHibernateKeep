package hibernate;
// Generated 02-mar-2016 9:28:45 by Hibernate Tools 4.3.1

/**
 * Keep generated by hbm2java
 */
public class Keep implements java.io.Serializable {

    private Integer id;
    private Usuario usuario;
    private Integer idAndroid;
    private String contenido;
    private String ruta;
    private String estado;

    public Keep() {
    }

    public Keep(Usuario usuario, String estado) {
        this.usuario = usuario;
        this.estado = estado;
    }

    public Keep(Usuario usuario, String idAndroid, String contenido, String ruta, String estado) {
        this(usuario, Integer.parseInt(idAndroid), contenido, ruta, estado);
    }

    public Keep(Usuario usuario, Integer idAndroid, String contenido, String ruta, String estado) {
        this.usuario = usuario;
        this.idAndroid = idAndroid;
        this.contenido = contenido;
        this.ruta = ruta;
        this.estado = estado;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getIdAndroid() {
        return this.idAndroid;
    }

    public void setIdAndroid(Integer idAndroid) {
        this.idAndroid = idAndroid;
    }

    public String getContenido() {
        return this.contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getRuta() {
        return this.ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUserName() {
        return usuario.getLogin();
    }

}
