package jose.imagensqlite;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Ingreso extends ActionBarActivity {

    /**
     * Edit text creados para ser ingresados al sistema
     */
    EditText edt_idobra, edt_nombre, edt_autor,
    edt_fecha_creacion, edt_resumen, edt_tipo,
    edt_estilo, edt_tecnica, edt_fecha_ingreso,
    edt_nacionalidad, edt_dim, edt_peso, edt_link;

    /**
     * Boton creado para poder guardar en la base de datos sqlite
     * la obra de arte y para poder retornar a la pantalla principal
     */
    Button btn_guardar, btn_reg;

    /**
     * Mensaje de autoria
     */
    public static final String MENSAJE = "Creado por Jose Luis Acunia";

    /**
     * Este mensaje se creo para poder determinar si se ingresaron los cuatro primeros valores
     */
    public static final String ERROR_UNO="Deben ingresar todos los valores mas importantes";

    /**
     * Mensaje de ubicacion en texto de la imagen a depositar
     */
    public static final String IP = "http://jose8android.esy.es/obras/imagenes/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);
        edt_idobra = (EditText)findViewById(R.id.act_serie);
        edt_nombre = (EditText)findViewById(R.id.act_nombre);
        edt_autor = (EditText)findViewById(R.id.act_autir);
        edt_fecha_creacion = (EditText)findViewById(R.id.act_creacion);
        edt_resumen = (EditText)findViewById(R.id.act_resumen);
        edt_tipo = (EditText)findViewById(R.id.act_tipo);
        edt_estilo = (EditText)findViewById(R.id.act_estilo);
        edt_tecnica = (EditText)findViewById(R.id.act_tecnica);
        edt_fecha_ingreso = (EditText)findViewById(R.id.act_ingreso);
        edt_nacionalidad = (EditText)findViewById(R.id.act_nacion);
        edt_dim = (EditText)findViewById(R.id.act_dim);
        edt_peso = (EditText)findViewById(R.id.act_peso);
        edt_link = (EditText)findViewById(R.id.act_imagen);
        btn_guardar = (Button)findViewById(R.id.btn_ing);
        edt_link.setText(IP);

        /**
         * Accion del boton de guardar para almacenar una obra de arte
         * en el sistema
         */
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_tipo.getText().toString() != "Estatua")
                {
                    edt_peso.setText("Irrelevante"); //por el hecho de que se definio de que si la obra era una estatua
                                                     //entonces su peso seria irrelevante
                }
                if(edt_idobra.getText().toString().trim().equals("")||
                        edt_nombre.getText().toString().trim().equals("")||
                        edt_autor.getText().toString().trim().equals("")||
                        edt_resumen.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), ERROR_UNO, Toast.LENGTH_LONG).show();
                }else{
                    BD db = new BD(getApplicationContext(),null,null,1);
                    String id = edt_idobra.getText().toString();
                    String nombre = edt_nombre.getText().toString();
                    String autor = edt_autor.getText().toString();
                    String fecha_creacion = edt_fecha_creacion.getText().toString();
                    String resumen = edt_resumen.getText().toString();
                    String tipo = edt_tipo.getText().toString();
                    String estilo = edt_estilo.getText().toString();
                    String tecnica = edt_tecnica.getText().toString();
                    String fecha_ingreso = edt_fecha_ingreso.getText().toString();
                    String nacionalidad = edt_nacionalidad.getText().toString();
                    String dimension = edt_dim.getText().toString();
                    String peso = edt_peso.getText().toString();
                    String imagen = edt_link.getText().toString();
                    String mensaje =db.guardar(id, nombre, autor, fecha_creacion, resumen,  tipo,
                                                estilo, tecnica, fecha_ingreso, nacionalidad,
                                                dimension, peso, imagen);
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                    // Retorna a la pantalla principal
                    Intent u = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(u);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingreso, menu);
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
    }
}
