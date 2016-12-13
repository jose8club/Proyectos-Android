package jose.multimedia_web;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class IdDos extends ActionBarActivity {

    /**
     * Edit text del nombre y autor de la obra
     */
    TextView id_serie, id_nombre, id_autor;

    /**
     * String del audio y video
     */
    String i, au, vi;

    /**
     * boton para retornar al sistema
     */
    Button btn_ret;
    Button btn_id;

    /**
     * botones de audio y video
     */
    Button audio,video,stop;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String ID = IP + "/obras/php/obt_obra_por_id.php";
    String AUDIO = IP + "/obras/audio/";

    /**
     * El mediaplayer para el audio
     */
    MediaPlayer player;

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_dos);
        id_serie = (TextView)findViewById(R.id.id_serie);
        id_nombre = (TextView)findViewById(R.id.id_nombre);
        id_autor = (TextView)findViewById(R.id.id_autor);
        btn_id = (Button)findViewById(R.id.btn_id);
        btn_ret = (Button)findViewById(R.id.btn_rettres);
        audio = (Button)findViewById(R.id.btn_audio);
        video = (Button)findViewById(R.id.btn_video);
        stop = (Button)findViewById(R.id.btn_stop);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        i = intento.getExtras().getString("idobra");
        id_serie.setText(i);

        /**
         * actitud del boton de busqueda
         */
        btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexion = new WebService();
                String llamada = ID + "?idobra=" + id_serie.getText().toString();
                conexion.execute(llamada, "1"); // Parámetros que recibe doInBackground
            }
        });

        /**
         * Lo que muestra el audio
         */
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), au, Toast.LENGTH_LONG).show();
                try {
                    //detener();
                    player = new MediaPlayer();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(AUDIO + au);
                    //player.prepare();
                    //player.start();

                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    player.prepareAsync();
                    //player.start();
                    stop.setVisibility(View.VISIBLE);

                    //player.prepare();
                    //player.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Detener audio
         */
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    player.reset();
                    player.prepare();
                    detener();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Lo que muestra el video
         */
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), vi, Toast.LENGTH_LONG).show();
                Intent u = new Intent(getApplicationContext(), Video.class);
                u.putExtra("video",vi);
                startActivity(u);
            }
        });

        /**
         * boton que regresa a pantalla principal
         */
        btn_ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setVisibility(View.INVISIBLE);
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });

    }

    /**
     * Este metodo es para detener el audio
     */
    private void detener() {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_id_dos, menu);
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

    /**
     * Clase dedicada a cumplir el servicio web
     */
    public class WebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener informacion
            String devuelve = "";
            if (params[1] == "1") {    // consulta por id
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        // Si el estado es 1 entonces es porque la operación fue un exito
                        if (resultJSON.equals("1")){
                            devuelve = devuelve + respuestaJSON.getJSONObject("obra").getString("idobra") + " " +
                                    respuestaJSON.getJSONObject("obra").getString("nombre") + " " +
                                    respuestaJSON.getJSONObject("obra").getString("autor") + " " +
                                    respuestaJSON.getJSONObject("obra").getString("audio") + " " +
                                    respuestaJSON.getJSONObject("obra").getString("video");
                        }
                        else if (resultJSON.equals("2")){ // Si el estado es 2 entonces es porque la operación fue un fracaso
                            devuelve = "No hay obra que devolver";
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;
            }
            return null;
        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            String [] arreglo = s.split(" ");
            id_nombre.setText(arreglo[1]);
            id_autor.setText(arreglo[2]);
            au = arreglo[3];
            vi = arreglo[4];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
