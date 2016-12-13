package jose.imagensqlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Actualizar extends ActionBarActivity {

    /**
     * El id de la obra a actualzar o eliminar
     */
    TextView id;
    /**
     * Edit text de los campos que se desea actualizar
     */
    EditText act_nombre, act_autor, act_resumen;
    /**
     * Imagen que debe verse es el objetivo primario de este programa
     * al igual que el string que contendrá dicha imagen
     */
    private ImageView imagen;
    String URI;
    /**
     * Botones que actualizan y eliminan respectivamente
     */
    Button btn_act, btn_elim;
    /**
     * Instancia de la base de datos sqlite
     */
    BD db;
    /**
     * Mensaje de autoria
     */
    public static final String MENSAJE = "Creado por José Luis Acuña";

    /**
     * Arreglo de todos los retornos de datos por fila
     */
    String [] fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        id = (TextView)findViewById(R.id.act_serie);
        act_nombre = (EditText)findViewById(R.id.act_nombre);
        act_autor = (EditText)findViewById(R.id.act_autir);
        act_resumen = (EditText)findViewById(R.id.act_resumen);
        imagen = (ImageView)findViewById(R.id.im_obra);
        btn_act = (Button)findViewById(R.id.btn_act);
        btn_elim = (Button)findViewById(R.id.btn_el);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        String idobra = intento.getExtras().getString("idobra");
        id.setText(idobra);
        //Buscar todos los datos del sistema acorde al id de la obra
        db = new BD(getApplicationContext(),null,null,1);
        fila = db.buscar(id.getText().toString());
        //Se rellena todos los campos respectivos
        act_nombre.setText(fila[1]);
        act_autor.setText(fila[2]);
        act_resumen.setText(fila[4]);
        //se obtiene los datos para obtener la imagen
        URI = fila[12].toString();
        // Se uttiliza la clase al final para mostrar la imagen
        new descargarImagen(imagen).execute(URI);

        /**
         * Acceder al boton de actualizar obra de arte
         */
        btn_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idobra = id.getText().toString();
                String nombre = act_nombre.getText().toString();
                String autor = act_autor.getText().toString();
                String fecha_creacion = fila[3].toString();
                String resumen = act_resumen.getText().toString();
                String tipo_obra = fila[5].toString();
                String estilo_obra = fila[6].toString();
                String tecnica_obra = fila[7].toString();
                String fecha_ingreso = fila[8].toString();
                String nacionalidad = fila[9].toString();
                String dimensiones = fila[10].toString();
                String peso = fila[11].toString();
                String imagen = fila[12].toString();

                // Procede a actualizar la base de datos
                String Mensaje = db.actualizar(idobra, nombre, autor, fecha_creacion,
                        resumen, tipo_obra, estilo_obra, tecnica_obra, fecha_ingreso,
                        nacionalidad, dimensiones, peso, imagen);

                // Lo muestra en pantalla
                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_SHORT).show();

                // Retorna a la pantalla principal
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });

        /**
         * Acceder a eliminar obra de arte
         */
        btn_elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se usa el ID de la obra
                String idobra = id.getText().toString();
                // Se procede a eliminar la obra
                String mensaje = db.eliminar(idobra);
                // Se muestra en pantalla su eliminacion
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                // Retorna a la pantalla principal
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });

        /**
         * Dar al usuario la opcion de ver el link de la imagen de la obra de arte
         */
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se muestra en pantalla su eliminacion
                Toast.makeText(getApplicationContext(), URI, Toast.LENGTH_SHORT).show();

            }
        });
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

    /**
     * Clase creada para poder mostrar mediante asyntask la imagen almacenada en la
     * base de datos SQLite
     */
    private class descargarImagen extends AsyncTask<String, Void, Bitmap> {
        ImageView imagen;

        public descargarImagen(ImageView imagen) {
            this.imagen = imagen;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imagen.setImageBitmap(result);
        }
    }
}
