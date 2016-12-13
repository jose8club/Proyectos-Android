package jose.imagensqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Alicia on 28/11/2016.
 */
public class Adaptador extends BaseAdapter {

    private static ArrayList<Obras> searchArrayList;

    private LayoutInflater mInflater;

    public Adaptador(Context context, ArrayList<Obras> results) {
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
            holder.txtId = (TextView) convertView.findViewById(R.id.serie_fila);
            holder.txtNombre = (TextView) convertView.findViewById(R.id.nombre_fila);
            holder.txtAutor = (TextView) convertView.findViewById(R.id.autor_fila);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtId.setText(searchArrayList.get(position).getId());
        holder.txtNombre.setText(searchArrayList.get(position).getNombre());
        holder.txtAutor.setText(searchArrayList.get(position).getAutor());


        return convertView;
    }
    static class ViewHolder {
        TextView txtId;
        TextView txtNombre;
        TextView txtAutor;
    }
}