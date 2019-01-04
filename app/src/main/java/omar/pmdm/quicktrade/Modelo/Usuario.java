package omar.pmdm.quicktrade.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    String nombreUsuario;
    String nombre;
    String apellidos;
    String direccion;
    String userID;

    public Usuario() {

    }

    public Usuario(String nombreUsuario, String nombre, String apellidos, String direccion, String userID) {
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.userID = userID;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    protected Usuario(Parcel in) {
        nombreUsuario = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        direccion = in.readString();
        userID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreUsuario);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(direccion);
        dest.writeString(userID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}