package omar.pmdm.quicktrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    private EditText usuario;
    private EditText password;
    private ArrayList<Usuario> listaUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText)findViewById(R.id.txtUsuario);
        password = (EditText)findViewById(R.id.txtPassword);
        listaUsuarios = new ArrayList<Usuario>();
        guardarUsuario(1, "Manel", "Viel", "mviel@florida-uni.es", "1234", "654321098");

    }

    private void guardarUsuario(int idUser, String nombre, String apellidos, String email, String password, String telefono) {
        Usuario u1 = new Usuario(idUser, nombre, apellidos, email, password, telefono);
        listaUsuarios.add(u1);
    }

    public void comprobarUsuario(View v) {
        // Comprobamos que ninguno de los campos sea una cadena vacia, en caso de serlo devolvemos false;
        if((usuario.getText().equals("")) || (password.getText().equals(""))) {
            Log.d("Depuración", "Una de las cadenas está vacía");
        }
        Usuario u1;
        Iterator<Usuario> Iter = listaUsuarios.iterator();
        while(Iter.hasNext()) {
            u1 = (Usuario) Iter.next();
            Log.d("Depuración", "Usuario introducido: " + usuario.getText());
            Log.d("Depuración", "Usuario a comparar: " + u1.getEmail());
            Log.d("Depuración", "Contraseña introducida: " + password.getText());
            Log.d("Depuración", "Contraseña a comparar: " + u1.getPassword());
            if(usuario.getText().equals(u1.getEmail())) {
                if(password.getText().equals(u1.getPassword())) {
                    Log.d("Depuración", "El usuario y la clave son correctas");
                }
            }
        }
        Log.d("Depuración", "No existe el usuario o la contraseña es incorrecta");
    }
}
