package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import omar.pmdm.quicktrade.Modelo.Producto;
import omar.pmdm.quicktrade.Modelo.Usuario;

public class AnyadirProductoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNombre;
    private EditText txtDescripion;
    private Spinner spinnerCategoria;
    private EditText txtPrecio;
    private Button btnGuardar;
    private Button btnCancelar;

    DatabaseReference database;
    Usuario usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_producto);

        txtNombre = findViewById(R.id.txtNombre);
        txtDescripion = findViewById(R.id.txtDescripcion);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        txtPrecio = findViewById(R.id.txtPrecio);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("productos");

        usuarioActual = getIntent().getParcelableExtra("Usuario");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                if(anyadirProducto()){
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;

            case R.id.btnCancelar:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

    }

    private boolean anyadirProducto() {
        if(comprobarCampos()) {
            String idProducto = database.push().getKey();
            Producto p = new Producto(idProducto, txtNombre.getText().toString(), txtDescripion.getText().toString(), spinnerCategoria.getSelectedItem().toString(), Double.parseDouble(txtPrecio.getText().toString()), usuarioActual.getUserID());
            database.child(idProducto).setValue(p);
            return true;
        } else {
            return false;
        }
    }

    private boolean comprobarCampos() {
        if(txtNombre.getText().toString().compareToIgnoreCase("") == 0
                || txtDescripion.getText().toString().compareToIgnoreCase("") == 0
                || txtPrecio.getText().toString().compareToIgnoreCase("") == 0) {
            return false;
        } else {
            return true;
        }
    }
}
