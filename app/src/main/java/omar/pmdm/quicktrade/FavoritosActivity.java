package omar.pmdm.quicktrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import omar.pmdm.quicktrade.Modelo.Producto;

public class FavoritosActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser currentUser;

    private RecyclerView recyclerViewProductos;

    private ImageView btnFavoritos;

    private LinearLayoutManager linearLayoutManager;
    private AdaptadorRecyclerProductos adaptadorRecyclerProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerViewProductos = findViewById(R.id.recyclerProductos);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewProductos.setLayoutManager(linearLayoutManager);

        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnFavoritos.setOnClickListener(this);

        listarProductos();

    }

    private void listarProductos() {
        final ArrayList<Producto> listadoProductos = new ArrayList<Producto>();
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios").child(currentUser.getUid()).child("favoritos");
        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listadoProductos.clear();
                for(final DataSnapshot i: dataSnapshot.getChildren()) {
                    String idProducto = i.child("idProducto").getValue(String.class);
                    DatabaseReference bbddAux = FirebaseDatabase.getInstance().getReference("productos");
                    Query qAux = bbddAux.orderByChild("idProducto").equalTo(idProducto);
                    qAux.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot a: dataSnapshot.getChildren()) {
                                Producto p = a.getValue(Producto.class);
                                Log.d("Omar", "Producto encontrado: " + p.getNombre());
                                listadoProductos.add(p);
                                adaptadorRecyclerProductos = new AdaptadorRecyclerProductos(listadoProductos);
                                recyclerViewProductos.setAdapter(adaptadorRecyclerProductos);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Log.d("Omar", "Adaptamos el Recycler");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnFavoritos:
                finish();
                break;
        }
    }
}
