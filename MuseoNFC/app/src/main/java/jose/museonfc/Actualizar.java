package jose.museonfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class Actualizar extends ActionBarActivity {

    TextView serie;
    EditText nombre,autor,tipo,estilo,fecha_creacion,nacionalidad,fecha_ingreso;
    Button btn_actualizar, btn_eliminar;
    public static final String MENSAJE = "Creado por José Luis Acuña";
    BD db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        serie = (TextView)findViewById(R.id.act_serie);
        nombre = (EditText)findViewById(R.id.act_nombre);
        autor = (EditText)findViewById(R.id.act_autir);
        tipo = (EditText)findViewById(R.id.act_tipo);
        estilo = (EditText)findViewById(R.id.act_estilo);
        fecha_creacion = (EditText)findViewById(R.id.act_creacion);
        nacionalidad = (EditText)findViewById(R.id.act_nacion);
        fecha_ingreso = (EditText)findViewById(R.id.act_ingreso);
        btn_actualizar = (Button)findViewById(R.id.btn_actualizar);
        btn_eliminar = (Button)findViewById(R.id.btn_eliminar);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        String nserie = intento.getExtras().getString("nserie");
        serie.setText(nserie);

        //Buscar todos los datos del sistema:
        db = new BD(getApplicationContext(),null,null,1);
        String [] fila = db.buscar_registros(serie.getText().toString());
        nombre.setText(fila[1]);
        autor.setText(fila[2]);
        tipo.setText(fila[3]);
        estilo.setText(fila[4]);
        fecha_creacion.setText(fila[5]);
        nacionalidad.setText(fila[6]);
        fecha_ingreso.setText(fila[7]);
        Toast.makeText(getApplicationContext(),fila[8],Toast.LENGTH_SHORT).show();

        //if(getIntent().hasExtra(NfcAdapter.EXTRA_TAG)) {
            // actualizar los datos en el sistema y del tag nfc
            //final Intent intentl = intent;
            btn_actualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //BD ab = new BD(getApplicationContext(),null,null,1);
                    String Buscar = serie.getText().toString();
                    String Nombre = nombre.getText().toString();
                    String Autor = autor.getText().toString();
                    String Tipo = tipo.getText().toString();
                    String Estilo = estilo.getText().toString();
                    String Creacion = fecha_creacion.getText().toString();
                    String Nacion = nacionalidad.getText().toString();
                    String Ingreso = fecha_ingreso.getText().toString();

                    //Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    // se crea el mensaje en el tag nfc en el caso de actualizar
                    //NdefMessage ndefMessage = createNdefMessage(nombre.getText()+";"+
                    //        autor.getText()+";"+tipo.getText()+";"+ estilo.getText()+"");
                    // se escribe el mensaje creado en el tag
                    //writeNdefMessage(tag, ndefMessage);

                    String Mensaje = db.actualizar(Buscar, Nombre, Autor, Tipo, Estilo, Creacion, Nacion, Ingreso);

                    Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_SHORT).show();
                    Intent z = new Intent(getApplicationContext(), ActualizarNfc.class);
                    z.putExtra("nombre",Nombre);
                    z.putExtra("autor",Autor);
                    z.putExtra("tipo",Tipo);
                    z.putExtra("estilo",Estilo);
                    startActivity(z);

                }
            });

            // eliminar los datos de una obra del bd y del tag nfc
            btn_eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Serie = serie.getText().toString();

                    //Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    //se crea el mensaje en blanco en el caso de eliminar
                    //NdefMessage ndefMessage = createNdefMessage("");
                    // se escribe el mensaje creado en el tag
                    //writeNdefMessage(tag, ndefMessage);

                    String mensaje = db.eliminar(Serie);

                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    Intent z = new Intent(getApplicationContext(), ActualizarNfc.class);
                    z.putExtra("nombre","");
                    z.putExtra("autor","");
                    z.putExtra("tipo","");
                    z.putExtra("estilo","");
                    startActivity(z);
                }
            });

        //}
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actualizar, menu);
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

}
