package pruebas.museosqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;


public class Lista extends AppCompatActivity {

    ListView lv ;
    ArrayList<String> lista;
    ArrayAdapter adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        lv = (ListView)findViewById(R.id.listaObras);
        BD db = new BD(getApplicationContext(),null,null,1);
        lista = db.llenar_lista();
        /*
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, lista) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(lista.get(1).toString());
                text2.setText(lista.get(0).toString());
                return view;
            }
        };
        adaptador.notifyDataSetChanged();
        */
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        lv.setAdapter(adaptador);


    }

}
