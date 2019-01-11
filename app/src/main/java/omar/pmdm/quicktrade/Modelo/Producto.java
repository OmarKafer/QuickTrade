package omar.pmdm.quicktrade.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable {

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

    protected Producto(Parcel in) {
        idProducto = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        categoria = in.readString();
        precio = in.readDouble();
        nombreUsuario = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idProducto);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(categoria);
        dest.writeDouble(precio);
        dest.writeString(nombreUsuario);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Producto> CREATOR = new Parcelable.Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };
}