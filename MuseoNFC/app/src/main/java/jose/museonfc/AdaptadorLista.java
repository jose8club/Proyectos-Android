package jose.museonfc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alicia on 26/09/2016.
 */
public class AdaptadorLista extends BaseAdapter {
    private static ArrayList<ListaObras> searchArrayList;

    private LayoutInflater mInflater;

    public AdaptadorLista(Context context, ArrayList<ListaObras> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_fila, null);
            holder = new ViewHolder();
            holder.txtSerie = (TextView) convertView.findViewById(R.id.serie_fila);
            holder.txtNombre = (TextView) convertView.findViewById(R.id.nombre_fila);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSerie.setText(searchArrayList.get(position).getSerie());
        holder.txtNombre.setText(searchArrayList.get(position).getNombre());


        return convertView;
    }
    static class ViewHolder {
        TextView txtSerie;
        TextView txtNombre;

    }
}
