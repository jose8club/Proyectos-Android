package jose.imagensqlite;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    /**
     * ListView creado para poder observar las obras
     */
    ListView lv;
    /**
     * Son los componentes de la lista que se mostraran al usuario
     * id de la obra, nombre y el autor
     */
    ArrayList<String> id;
    ArrayList<String> nombres;
    ArrayList<String> autores;
    /**
     * Es la lista a ingresar de la obra
     */
    ArrayList<Obras> ListaObras;
    /**
     * Boton para ingresar a la activity del ingreso
     */
    Button btn_ingresar;
    /**
     * Mensaje de autoria
     */
    public static final String MENSAJE = "Creado por José Luis Acuña";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.lista_obras);
        btn_ingresar = (Button)findViewById(R.id.btn_crear);
        BD db = new BD(getApplicationContext(),null,null,1);
        id = db.lista_id();
        nombres = db.lista_nombre();
        autores = db.lista_autor();
        ListaObras = GetListaObra(id, nombres, autores);
        lv.setAdapter(new Adaptador(this, ListaObras));

        /**
         * Poder ingresar una nueva obra de arte
         */
        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(getApplicationContext(), Ingreso.class);
                startActivity(z);
            }
        });

        /**
         * Al seleccionar un item de la lista poder acceder a este y poder hacer modificaciones
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el valor de la casilla elegida
                TextView serie = (TextView) view.findViewById(R.id.serie_fila);
                Toast.makeText(getApplicationContext(), serie.getText().toString(), Toast.LENGTH_SHORT).show();
                String numero = serie.getText().toString();
                Intent z = new Intent(getApplicationContext(), Actualizar.class);
                z.putExtra("idobra", numero);
                startActivity(z);
            }
        });
    }

    /**
     * Este metodo se creo para llenar la lista con la serie siendo invisible, el nombre y autor de la obra
     * @param id
     * @param nombres
     * @param autores
     * @return
     */
    private ArrayList<Obras> GetListaObra(ArrayList<String> id, ArrayList<String> nombres, ArrayList<String> autores) {
        ArrayList<Obras> obras = new ArrayList<Obras>();
        int largo = id.size();
        Obras res;
        for (int i=0; i<largo; i++){
            res = new Obras();
            res.setId(id.get(i).toString());
            res.setNombre(nombres.get(i).toString());
            res.setAutor(autores.get(i).toString());
            obras.add(res);
        }
        return obras;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        lv.setAdapter(new Adaptador(this, ListaObras));
    }
}
