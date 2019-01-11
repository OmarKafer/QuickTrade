package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdService;

import omar.pmdm.quicktrade.Modelo.Producto;
import omar.pmdm.quicktrade.Modelo.Usuario;

public class DetalleProductoActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private Usuario usuarioActual;
    private Producto productoActual;

    private DatabaseReference database;

    private TextView txtNombre, txtDescripcion, txtPrecio;
    private TextView lblNombre, lblDescripcion, lblPrecio, lblCategoria;
    private Spinner spinnerCategoria;
    private Button btnGuardar, btnSalir, btnEliminar;
    private CheckBox cbFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        usuarioActual = getIntent().getParcelableExtra("Usuario");
        productoActual = getIntent().getParcelableExtra("Producto");

        database = FirebaseDatabase.getInstance().getReference("productos");

        txtNombre = findViewById(R.id.txtNombre);
        txtNombre.addTextChangedListener(this);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtDescripcion.addTextChangedListener(this);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtPrecio.addTextChangedListener(this);

        lblNombre = findViewById(R.id.lblNombre);
        lblDescripcion = findViewById(R.id.lblDescripcion);
        lblPrecio = findViewById(R.id.lblPrecio);
        lblCategoria = findViewById(R.id.lblCategoria);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);
        btnSalir = findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(this);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(this);
        cbFavorito = findViewById(R.id.cbFavorito);


        cargarCampos();
        cargarLabelProducto();
        comprobarFavorito();

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lblCategoria.setText(spinnerCategoria.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void comprobarFavorito() {
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios").child(usuarioActual.getUserID()).child("favoritos");
        Query q = bbdd.orderByChild("idProducto").equalTo(productoActual.getIdProducto());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    cbFavorito.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarCampos() {
        txtNombre.setText(productoActual.getNombre());
        txtDescripcion.setText(productoActual.getDescripcion());
        txtPrecio.setText(productoActual.getPrecio()+"");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
        int pos = adapter.getPosition(productoActual.getCategoria());
        spinnerCategoria.setSelection(pos);

        if (usuarioActual.getNombreUsuario().compareToIgnoreCase(productoActual.getnombreUsuario()) != 0) {
            txtNombre.setEnabled(false);
            txtDescripcion.setEnabled(false);
            txtPrecio.setEnabled(false);
            spinnerCategoria.setEnabled(false);
            btnGuardar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }
    }

    private void cargarLabelProducto() {
        lblNombre.setText(txtNombre.getText().toString());
        lblDescripcion.setText(txtDescripcion.getText().toString());
        lblPrecio.setText(txtPrecio.getText().toString() + " €");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        cargarLabelProducto();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("Usuario", FirebaseAuth.getInstance().getCurrentUser());
        switch (view.getId()) {
            case R.id.btnGuardar:
                if(guardarProducto()) {
                    estadoFavorito();
                    setResult(RESULT_OK);
                    Toast.makeText(getApplicationContext(), "Producto editado con exito !", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR: No se puede modificar el producto", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSalir:
                estadoFavorito();
                setResult(RESULT_CANCELED);
                startActivity(i);
                break;
            case R.id.btnEliminar:
                eliminarProducto();
                Toast.makeText(getApplicationContext(), "Producto eliminado con exito !", Toast.LENGTH_SHORT).show();
                startActivity(i);
                break;
        }
    }

    private void eliminarProducto() {
        database.child(productoActual.getIdProducto()).removeValue();
    }

    private boolean guardarProducto() {
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios").child(usuarioActual.getUserID());

        productoActual.setNombre(txtNombre.getText().toString());
        productoActual.setDescripcion(txtDescripcion.getText().toString());
        productoActual.setPrecio(Double.parseDouble(txtPrecio.getText().toString()));
        productoActual.setCategoria(spinnerCategoria.getSelectedItem().toString());
        database.child(productoActual.getIdProducto()).setValue(productoActual);
        return true;
    }

    private void estadoFavorito() {
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios").child(usuarioActual.getUserID());
        if(cbFavorito.isChecked()) {
            bbdd.child("favoritos").child(productoActual.getIdProducto()).child("idProducto").setValue(productoActual.getIdProducto());
        } else {
            bbdd.child("favoritos").child(productoActual.getIdProducto()).removeValue();
        }
    }
}