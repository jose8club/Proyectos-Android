package jose.multimedia_web;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class Editar extends ActionBarActivity {

    /**
     * Retornar al la pantalla prinicpal
     */
    Button btn_retornar, btn_editar;

    /**
     * Textos para actualizar
     */
    EditText act_nombre, act_autor, act_audio, act_video;
    TextView act_id;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String ID = IP + "/obras/php/obt_obra_por_id.php";
    String i;

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        btn_retornar = (Button)findViewById(R.id.btn_retcuatro);
        btn_editar = (Button)findViewById(R.id.btn_editar);
        act_id = (TextView)findViewById(R.id.act_id);
        act_nombre = (EditText)findViewById(R.id.act_nombre);
        act_autor = (EditText)findViewById(R.id.act_autir);
        act_audio = (EditText)findViewById(R.id.act_audio);
        act_video = (EditText)findViewById(R.id.act_video);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        i = intento.getExtras().getString("idobra");
        act_id.setText(i);

        conexion = new WebService();
        String llamada = ID + "?idobra=" + act_id.getText().toString();
        conexion.execute(llamada, "1"); // Parámetros que recibe doInBackground

        btn_retornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar, menu);
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


                            //id_nombre.setText(respuestaJSON.getJSONObject("obra").getString("nombre").toString());
                            //id_autor.setText(respuestaJSON.getJSONObject("obra").getString("autor").toString());
                            //au = respuestaJSON.getJSONObject("obra").getString("audio").toString();
                            //vi = respuestaJSON.getJSONObject("obra").getString("video").toString();
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
            act_nombre.setText(arreglo[1]);
            act_autor.setText(arreglo[2]);
            act_audio.setText(arreglo[3]);
            act_video.setText(arreglo[4]);
            btn_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent u = new Intent(getApplicationContext(), ActualizarDos.class);
                    u.putExtra("idobra", act_id.getText().toString());
                    u.putExtra("nombre", act_nombre.getText().toString());
                    u.putExtra("autor", act_autor.getText().toString());
                    u.putExtra("audio", act_audio.getText().toString());
                    u.putExtra("video", act_video.getText().toString());
                    startActivity(u);
                }
            });
            //Ejecucion(s);
            //id_nombre.setText(s);

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
