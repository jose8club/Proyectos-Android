package pruebas.sqliteprueba;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class Modificar extends AppCompatActivity implements OnClickListener {

    EditText serie, nombre;
    Button btnActualizar, btnEliminar;
    Controlador dbcon;
    String serieMod, nombreMod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        dbcon = new Controlador(this);
        dbcon.abrirBaseDeDatos();

        serie = (EditText)findViewById(R.id.edtModIdentificacion);
        nombre = (EditText)findViewById(R.id.edtModNombre);

        btnActualizar = (Button)findViewById(R.id.btnActualizar);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);

        Intent i = getIntent();
        serieMod = i.getStringExtra("miembroSerie");
        nombreMod = i.getStringExtra("miembroSerie");
        serie.setText(serieMod);
        nombre.setText(nombreMod);

        btnActualizar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActualizar:
                String memName_upd = nombre.getText().toString();
                dbcon.actualizarDatos(serieMod, memName_upd);
                this.returnHome();
                break;

            case R.id.btnEliminar:
                dbcon.deleteData(serieMod);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modificar, menu);
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
