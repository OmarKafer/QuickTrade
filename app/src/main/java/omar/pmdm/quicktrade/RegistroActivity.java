package omar.pmdm.quicktrade;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import omar.pmdm.quicktrade.Modelo.Usuario;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference database;

    EditText nombreUsuario;
    EditText nombre;
    EditText apellidos;
    EditText email;
    EditText password;
    EditText direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        final Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        nombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
        nombre = (EditText) findViewById(R.id.txtNombre);
        apellidos = (EditText) findViewById(R.id.txtApellidos);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        direccion = (EditText) findViewById(R.id.txtDireccion);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        // Listener del botón "btnCancelar"
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED); // Manda un 0.
                finish();
            }
        });

        // Listener del botón "btnGuardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registrarUsuario(nombreUsuario.getText().toString());
            }
        });
    }

    boolean res;
    private void registrarUsuario(final String nombreUsuario) {
        res = false;
        Query q = database.orderByChild("nombreUsuario");

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    Usuario u = i.getValue(Usuario.class);
                    if (u.getNombreUsuario().compareToIgnoreCase(nombreUsuario) == 0) {
                        res = true;
                    }
                }
                if(comprobarCampos() && !res) {
                    introducirUsuario();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR: Comprueba los campos, o el nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void introducirUsuario() {

        EditText email = (EditText) findViewById(R.id.txtEmail);
        EditText password = (EditText) findViewById(R.id.txtPassword);


        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Usuario creado con éxito.", Toast.LENGTH_SHORT).show();
                            introducirDatos(mAuth.getCurrentUser());
                            mAuth.getInstance().signOut();
                            setResult(RESULT_OK); // Manda un -1
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + task.getException(), Toast.LENGTH_LONG).show();
                            setResult(RESULT_FIRST_USER); // Manda un 1
                            finish();
                        }
                    }
                });
    }

    private boolean comprobarCampos() {
        if(nombre.getText().toString().compareTo("") == 0 ||
                nombreUsuario.getText().toString().compareTo("") == 0 ||
                apellidos.getText().toString().compareTo("") == 0 ||
                email.getText().toString().compareTo("") == 0 ||
                password.getText().toString().compareTo("") == 0 ||
                direccion.getText().toString().compareTo("") == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void introducirDatos(FirebaseUser user) {
        Usuario usuario = new Usuario(nombreUsuario.getText().toString(), nombre.getText().toString(), apellidos.getText().toString(), direccion.getText().toString(), user.getUid());
        database.child(user.getUid()).setValue(usuario);
    }

}
