package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import omar.pmdm.quicktrade.Modelo.Producto;
import omar.pmdm.quicktrade.Modelo.Usuario;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference database;
    private FirebaseUser currentUser;
    private Usuario usuarioActual;

    private Button btnEditarUsuario;
    private Button btnAnyadirProducto;
    private Spinner spinnerCategoria, spinnerUsuario;
    private RecyclerView recyclerViewProductos;

    private LinearLayoutManager linearLayoutManager;
    private AdaptadorRecyclerProductos adaptadorRecyclerProductos;

    final static int EditarUsuarioActivity = 0;
    final static int AnyadirProductoActivity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEditarUsuario = findViewById(R.id.btnEditarUsuario);
        btnEditarUsuario.setOnClickListener(this);
        btnAnyadirProducto = findViewById(R.id.btnAnyadirProducto);
        btnAnyadirProducto.setOnClickListener(this);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerCategoria.setOnItemSelectedListener(this);
        spinnerUsuario = findViewById(R.id.spinnerUsuario);
        spinnerUsuario.setOnItemSelectedListener(this);
        recyclerViewProductos = findViewById(R.id.recyclerProductos);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewProductos.setLayoutManager(linearLayoutManager);

        recuperarUsuario();
        cargarSpinners();

    }

    private void cargarSpinners() {
        ArrayList<String> categorias = new ArrayList<String>();
        categorias.add("Todas");
        categorias.add("Tecnología");
        categorias.add("Coches");
        categorias.add("Hogar");

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);

        final ArrayList<String> usuarios = new ArrayList<String>();
        usuarios.add("Todos");

        ArrayAdapter<String> adapterUsuario = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, usuarios);
        adapterUsuario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsuario.setAdapter(adapterUsuario);

        database = FirebaseDatabase.getInstance().getReference("usuarios");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    Usuario u = i.getValue(Usuario.class);
                    usuarios.add(u.getNombreUsuario());
                }
                ArrayAdapter<String> adapterAux = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, usuarios);
                adapterAux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUsuario.setAdapter(adapterAux);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (spinnerCategoria.getSelectedItem().toString().compareTo("Todas") == 0) {
            if(spinnerUsuario.getSelectedItem().toString().compareTo("Todos") == 0) {
                listarProductos(false, false, null, null);
            } else {
                listarProductos(false, true, null, spinnerUsuario.getSelectedItem().toString());
            }
        } else {
            if(spinnerUsuario.getSelectedItem().toString().compareTo("Todos") == 0) {
                listarProductos(true, false, spinnerCategoria.getSelectedItem().toString(), null);
            } else {
                listarProductos(true, true, spinnerCategoria.getSelectedItem().toString(), spinnerUsuario.getSelectedItem().toString());
            }
        }
    }

    private void listarProductos(boolean filtrarCategoria, boolean filtrarUsuario, final String categoria, final String usuario) {
        final ArrayList<Producto> listadoProductos = new ArrayList<Producto>();
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("productos");
        Query q = bbdd.orderByChild("nombre");
        if (filtrarCategoria && filtrarUsuario) {
            // Filtrar por los dos
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listadoProductos.clear();
                    for(final DataSnapshot i: dataSnapshot.getChildren()) {
                        Producto p = i.getValue(Producto.class);
                        if ((p.getCategoria().compareToIgnoreCase(categoria) == 0) && (p.getnombreUsuario().compareToIgnoreCase(usuario) == 0)) {
                            listadoProductos.add(p);
                        }
                    }
                    if(usuario.compareToIgnoreCase(usuarioActual.getNombreUsuario()) == 0) {
                        adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos, true);
                        recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                    } else {
                        adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos, false);
                        recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Log.d("Omar", "Filtrar por los dos");
        } else {
            if (filtrarCategoria) {
                // Filtrar solo por Categoría
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listadoProductos.clear();
                        for(final DataSnapshot i: dataSnapshot.getChildren()) {
                            Producto p = i.getValue(Producto.class);
                            if (p.getCategoria().compareToIgnoreCase(categoria) == 0) {
                                listadoProductos.add(p);
                            }
                        }
                        adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos, false);
                        recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d("Omar", "Filtrar solo por Categoría");
            } else if (filtrarUsuario) {
                // Filtrar solo por Usuario
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listadoProductos.clear();
                        for(final DataSnapshot i: dataSnapshot.getChildren()) {
                            Producto p = i.getValue(Producto.class);
                            Log.d("Omar", "Owner encontrado: " + p.getnombreUsuario() + " Usuario buscado: " + usuario);
                            if (p.getnombreUsuario().compareToIgnoreCase(usuario) == 0) {
                                listadoProductos.add(p);
                            }
                        }
                        if(usuario.compareToIgnoreCase(usuarioActual.getNombreUsuario()) == 0) {
                            adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos, true);
                            recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                        } else {
                            adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos, false);
                            recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d("Omar", "Filtrar solo por Usuario");
            } else {
                // Sin filtros
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listadoProductos.clear();
                        for (final DataSnapshot i : dataSnapshot.getChildren()) {
                            Producto p = i.getValue(Producto.class);
                            listadoProductos.add(p);
                        }
                        adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos, false);
                        recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d("Omar", "Sin filtros");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.btnEditarUsuario:
                i = new Intent(getApplicationContext(), EditarUsuarioActivity.class);
                i.putExtra("Usuario", usuarioActual);
                startActivityForResult(i, EditarUsuarioActivity);
                break;

            case R.id.btnAnyadirProducto:
                i = new Intent(getApplicationContext(), AnyadirProductoActivity.class);
                i.putExtra("Usuario", usuarioActual);
                startActivityForResult(i, AnyadirProductoActivity);
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
            case AnyadirProductoActivity:
                switch (resultCode) {
                    case RESULT_OK:
                        Toast.makeText(getApplicationContext(), "Producto añadido con éxito", Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "No se ha añadido ningún producto", Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    }
}
