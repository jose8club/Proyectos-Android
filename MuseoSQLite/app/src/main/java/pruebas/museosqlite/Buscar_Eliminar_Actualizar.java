package pruebas.museosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Buscar_Eliminar_Actualizar extends AppCompatActivity {

    EditText nNombre;
    TextView serie, nombre;
    Button Bbuscar,BEliminar,BActualizar,BLista;
    Spinner spinner;
    ArrayList<String> lista;
    ArrayAdapter<String> spinnerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_eliminar_actualizar);

        serie=(TextView)findViewById(R.id.serie);
        nombre=(TextView)findViewById(R.id.nombre);

        Bbuscar=(Button)findViewById(R.id.Bbuscar);
        BEliminar=(Button)findViewById(R.id.BEliminar);

        nNombre=(EditText)findViewById(R.id.NNombre);

        BActualizar=(Button)findViewById(R.id.BActualizar);
        BLista=(Button)findViewById(R.id.blista);
        spinner=(Spinner)findViewById(R.id.spinnerObras);

        //llenar spinner
        BD db = new BD(getApplicationContext(),null,null,1);
        lista = db.llenar_spinner();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        Bbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD db = new BD(getApplicationContext(),null,null,1);
                String buscar = spinner.getSelectedItem().toString();
                //String buscar = Ebuscar.getText().toString();
                String[] datos;
                datos=db.buscar_registros(buscar);
                serie.setText(datos[0]);
                nombre.setText(datos[1]);
                Toast.makeText(getApplicationContext(),datos[2],Toast.LENGTH_SHORT).show();
                //Ebuscar.setText("");
            }
        });
        BEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD db = new BD(getApplicationContext(),null,null,1);
                //String Serie = serie.getText().toString();
                String Serie = spinner.getSelectedItem().toString();
                String mensaje =db.eliminar(Serie);
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                //Ebuscar.setText("");
                serie.setText("");
                nombre.setText("");
                spinnerArrayAdapter.remove((String)spinner.getSelectedItem());
                spinnerArrayAdapter.notifyDataSetChanged();
            }
        });
        BActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD db = new BD(getApplicationContext(),null,null,1);
                //String Buscar = serie.getText().toString();
                //String Serie = nSerie.getText().toString();
                String Buscar = spinner.getSelectedItem().toString();
                String Nombre = nNombre.getText().toString();
                String Mensaje =db.actualizar(Buscar, Nombre);
                //String Mensaje =db.actualizar(Buscar, Serie, Nombre);
                Toast.makeText(getApplicationContext(),Mensaje,Toast.LENGTH_SHORT).show();
                serie.setText("");
                nombre.setText("");
                nNombre.setText("");
            }
        });
        BLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serie.setText("");
                nombre.setText("");
                nNombre.setText("");
                Intent intento = new Intent(getApplicationContext(),Lista.class);
                startActivity(intento);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buscar_eliminar_actualizar, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
