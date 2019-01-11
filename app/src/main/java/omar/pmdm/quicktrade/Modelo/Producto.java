package omar.pmdm.quicktrade.Modelo;

public class Producto {

    private String idProducto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private double precio;
    private String nombreUsuario;

    public Producto() {

    }

    public Producto(String idProducto, String nombre, String descripcion, String categoria, double precio, String nombreUsuario) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.nombreUsuario = nombreUsuario;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getnombreUsuario() {
        return nombreUsuario;
    }

    public void setIdUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
