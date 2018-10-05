package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        // Inicializamos los botones y campos de texto que vamos a necesitar de la vista
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        usuario = (EditText)findViewById(R.id.txtUsuario);
        password = (EditText)findViewById(R.id.txtPassword);
        listaUsuarios = new ArrayList<Usuario>();

        // Listener del botón "btnLogin"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comprobarUsuario(v);
            }
        });

        // Listener del botón "btnRegistrar"
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RegistroActivity.class);
                i.putExtra("listaUsuarios", listaUsuarios);
                startActivityForResult(i, 0);
            }
        });

        guardarUsuario(1, "Manel", "Viel", "mviel@florida-uni.es", "1234", "654321098");
    }

    private void guardarUsuario(int idUser, String nombre, String apellidos, String email, String password, String telefono) {
        Usuario u1 = new Usuario(idUser, nombre, apellidos, email, password, telefono);
        listaUsuarios.add(u1);
    }

    private void comprobarUsuario(View v) {
        // Comprobamos que ninguno de los campos sea una cadena vacia, en caso de serlo devolvemos false;
        if((usuario.getText().toString().compareTo("") == 0) || (password.getText().toString().compareTo("") == 0)) {
            // Alguna cadena vacía
        } else {
            Usuario u1;
            Iterator<Usuario> Iter = listaUsuarios.iterator();
            while (Iter.hasNext()) {
                u1 = (Usuario) Iter.next();
                if (usuario.getText().toString().compareTo(u1.getEmail()) == 0) {
                    if (password.getText().toString().compareTo(u1.getPassword()) == 0) {
                        // Coincide el usuario y la clave con el que estamos comparando ahora mismo;
                        Intent i = new Intent(v.getContext(), MainActivity.class);
                        startActivity(i);
                    }
                }
            }
            // No ha coincidido ningun usuario (el usuario no existe o la clave es incorrecta);
        }
    }
}
