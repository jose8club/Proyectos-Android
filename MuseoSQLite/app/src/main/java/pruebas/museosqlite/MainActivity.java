package pruebas.museosqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    EditText ingSerie,ingNombre;
    Button guardar,buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ingSerie = (EditText)findViewById(R.id.serie);
        ingNombre = (EditText)findViewById(R.id.nombre);
        guardar = (Button)findViewById(R.id.guardar);
        buscar = (Button)findViewById(R.id.buscar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD db= new BD(getApplicationContext(),null,null,1);
                String serie = ingSerie.getText().toString();
                String nombre = ingNombre.getText().toString();
                String mensaje =db.guardar(serie, nombre);
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                ingSerie.setText("");
                ingNombre.setText("");
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingSerie.setText("");
                ingNombre.setText("");
                Intent busqueda = new Intent(getApplicationContext(), Buscar_Eliminar_Actualizar.class);
                startActivity(busqueda);
            }
        });
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
