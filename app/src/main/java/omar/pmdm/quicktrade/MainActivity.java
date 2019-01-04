package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import omar.pmdm.quicktrade.Modelo.Usuario;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference database;
    FirebaseUser currentUser;
    Usuario usuarioActual;

    TextView txtBienvenida;
    Button btnEditarUsuario;

    final static int EditarUsuarioActivity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBienvenida = findViewById(R.id.txtBienvenida);
        btnEditarUsuario = findViewById(R.id.btnEditarUsuario);
        btnEditarUsuario.setOnClickListener(this);
        recuperarUsuario();

    }

    private void recuperarUsuario() {
        currentUser = getIntent().getParcelableExtra("Usuario");

        database = FirebaseDatabase.getInstance().getReference("usuarios");
        Query q = database.orderByChild("userID").equalTo(currentUser.getUid());

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    usuarioActual = i.getValue(Usuario.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditarUsuario:
                Intent i = new Intent(getApplicationContext(), EditarUsuarioActivity.class);
                i.putExtra("Usuario", usuarioActual);
                startActivityForResult(i, EditarUsuarioActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        switch (requestCode) {
            case EditarUsuarioActivity:
                switch (resultCode) {
                    case RESULT_OK:
                        usuarioActual = i.getParcelableExtra("Usuario");
                        Toast.makeText(getApplicationContext(), "Usuario editado correctamente.", Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "No se han hecho cambios.", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

}
