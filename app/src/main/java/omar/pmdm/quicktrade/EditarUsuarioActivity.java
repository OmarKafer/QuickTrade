package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import omar.pmdm.quicktrade.Modelo.Usuario;

public class EditarUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtDireccion;
    private EditText txtNombreUsuario;
    private Button btnGuardar;
    private Button btnCancelar;

    private Usuario usuarioActual;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        database = FirebaseDatabase.getInstance().getReference("usuarios");
        usuarioActual = getIntent().getParcelableExtra("Usuario");

        txtNombre = findViewById(R.id.txtNombre);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtDireccion = findViewById(R.id.txtDireccion);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        txtNombreUsuario.setText(usuarioActual.getNombreUsuario());
        txtNombre.setText(usuarioActual.getNombre());
        txtApellidos.setText(usuarioActual.getApellidos());
        txtDireccion.setText(usuarioActual.getDireccion());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                actualizarUsuarioActual();
                Query q = database.orderByChild("userID").equalTo(usuarioActual.getUserID());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        database.child(usuarioActual.getUserID()).setValue(usuarioActual);
                        Intent i = new Intent();
                        i.putExtra("Usuario", usuarioActual);
                        setResult(RESULT_OK, i);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
                break;
            case R.id.btnCancelar:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void actualizarUsuarioActual() {
        usuarioActual.setNombre(txtNombre.getText().toString());
        usuarioActual.setApellidos(txtApellidos.getText().toString());
        usuarioActual.setDireccion(txtDireccion.getText().toString());
    }
}
