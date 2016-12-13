package jose.museonfc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Lista extends ActionBarActivity {

    ListView lv ;
    ArrayList<String> series;
    ArrayList<String> nombres;
    ArrayList<ListaObras> ListaObras;
    Button btn_regresar;
    public static final String MENSAJE = "Creado por José Luis Acuña";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        lv = (ListView)findViewById(R.id.lista_obras);
        btn_regresar = (Button)findViewById(R.id.btn_regresar);
        BD db = new BD(getApplicationContext(),null,null,1);
        series = db.llenar_lista_serie();
        nombres = db.llenar_lista_nombre();
        ListaObras = GetListaObra(series, nombres);
        //adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        //lv.setAdapter(adaptador);
        lv.setAdapter(new AdaptadorLista(this, ListaObras));


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Obtiene el valor de la casilla elegida
                //String item = adapterView.getItemAtPosition(i).toString();
                TextView serie = (TextView) view.findViewById(R.id.serie_fila);
                Toast.makeText(getApplicationContext(), serie.getText().toString(), Toast.LENGTH_SHORT).show();
                String numero = serie.getText().toString();
                Intent z = new Intent(getApplicationContext(), Actualizar.class);
                z.putExtra("nserie", numero);
                startActivity(z);

            }
        });

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(getApplicationContext(), ingreso.class);
                startActivity(z);
            }
        });

    }

    private ArrayList<ListaObras> GetListaObra(ArrayList<String> series, ArrayList<String> nombres) {
        ArrayList<ListaObras> obras = new ArrayList<ListaObras>();
        int largo = series.size();
        ListaObras res;
        for (int i=0; i<largo; i++){
            res = new ListaObras();
            res.setSerie(series.get(i).toString());
            res.setNombre(nombres.get(i).toString());
            obras.add(res);
        }
        return obras;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, MENSAJE, Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lv.setAdapter(new AdaptadorLista(this, ListaObras));
    }
}
