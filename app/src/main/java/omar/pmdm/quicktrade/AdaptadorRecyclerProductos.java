package omar.pmdm.quicktrade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import omar.pmdm.quicktrade.Modelo.Producto;

public class AdaptadorRecyclerProductos extends RecyclerView.Adapter<AdaptadorRecyclerProductos.MiViewHolder> {
    ArrayList<Producto> listadoProductos;

    public AdaptadorRecyclerProductos (ArrayList<Producto> listadoProductos) {
        this.listadoProductos = listadoProductos;
    }

    public static class MiViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre, txtDescripcion, txtPrecio, txtCategoria;


        public MiViewHolder (View view) {
            super(view);
            txtNombre = (TextView) view.findViewById(R.id.txtNombre);
            txtDescripcion =(TextView) view.findViewById(R.id.txtDescripcion);
            txtPrecio = (TextView) view.findViewById(R.id.txtPrecio);
            txtCategoria =(TextView) view.findViewById(R.id.txtCategoria);
        }

    }

    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_recycler, parent, false);
        return new MiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        if(!listadoProductos.isEmpty()) {
            holder.txtNombre.setText(listadoProductos.get(position).getNombre());
            holder.txtDescripcion.setText(listadoProductos.get(position).getDescripcion());
            holder.txtPrecio.setText(listadoProductos.get(position).getPrecio() + " €");
            holder.txtCategoria.setText(listadoProductos.get(position).getCategoria());
        }
    }

    @Override
    public int getItemCount() {
        return listadoProductos.size();
    }

    public Producto getPosProducto (int pos) {
        return listadoProductos.get(pos);
    }
}
