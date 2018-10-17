package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        final Button btnGuardar = (Button) findViewById(R.id.btnGuardar);

        // Listener del botón "btnCancelar"
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(i, 0);
            }
        });

        // Listener del botón "btnGuardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Usuario u1 = guardarUsuario();
                if (u1 != null) {
                    Intent i = new Intent();
                    i.putExtra("Usuario", u1);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Log.d("Depuracion", "Error al guardar el usuario");
                }
            }
        });
    }

    private Usuario guardarUsuario() {
        if(comprobarCampos()) {
            EditText idUsuario = (EditText) findViewById(R.id.txtId);
            EditText nombre = (EditText) findViewById(R.id.txtNombre);
            EditText apellidos = (EditText) findViewById(R.id.txtApellidos);
            EditText email = (EditText) findViewById(R.id.txtEmail);
            EditText password = (EditText) findViewById(R.id.txtPassword);
            EditText telefono = (EditText) findViewById(R.id.txtTelefono);
            Usuario u1 = new Usuario(Integer.parseInt(idUsuario.getText().toString()), nombre.getText().toString(), apellidos.getText().toString(), email.getText().toString(), password.getText().toString(), telefono.getText().toString());
            return u1;
        } else {
            return null;
        }
    }

    private boolean comprobarCampos() {
        EditText idUsuario = (EditText) findViewById(R.id.txtId);
        EditText nombre = (EditText) findViewById(R.id.txtNombre);
        EditText apellidos = (EditText) findViewById(R.id.txtApellidos);
        EditText email = (EditText) findViewById(R.id.txtEmail);
        EditText password = (EditText) findViewById(R.id.txtPassword);
        EditText telefono = (EditText) findViewById(R.id.txtTelefono);
        RadioButton hombre = (RadioButton) findViewById(R.id.rbHombre);
        RadioButton mujer = (RadioButton) findViewById(R.id.rbMujer);
        if(idUsuario.getText().toString().compareTo("") == 0 ||
                nombre.getText().toString().compareTo("") == 0 ||
                apellidos.getText().toString().compareTo("") == 0 ||
                email.getText().toString().compareTo("") == 0 ||
                password.getText().toString().compareTo("") == 0 ||
                telefono.getText().toString().compareTo("") == 0 ||
                (!hombre.isChecked() && !mujer.isChecked())) {
            return false;
        } else {
            return true;
        }
    }
}
