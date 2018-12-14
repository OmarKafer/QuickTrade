package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    final static int RegistroActivity = 0;


    @Override
    public void onStart() {
        super.onStart();
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Inicializamos los botones y campos de texto que vamos a necesitar de la vista
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        email = (EditText)findViewById(R.id.txtUsuario);
        password = (EditText)findViewById(R.id.txtPassword);

        // Listener del botón "btnLogin"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginUsuario();
            }
        });

        // Listener del botón "btnRegistrar"
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivityForResult(i, RegistroActivity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {

    }

    private void loginUsuario() {
        // Comprobamos que ninguno de los campos sea una cadena vacia, en caso de serlo devolvemos false;
        if((email.getText().toString().compareTo("") == 0) || (password.getText().toString().compareTo("") == 0)) {
            // Alguna cadena vacía
            Toast.makeText(getApplicationContext(), "ERROR: Revisa los campos.", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR: Email o contraseña incorrectas.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
