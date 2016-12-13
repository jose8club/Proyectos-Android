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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Crear extends ActionBarActivity {

    /**
     * Botones
     */
    Button btn_ing,btn_retornar;

    /**
     * Edit Text
     */
    EditText act_serie, act_nombre, act_autor, act_audio, act_video;

    /**
     * Este muesta un mensaje de respuesta JSON
     */
    TextView ing_json;
    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String INSERT = IP + "/obras/php/ingresar_obra.php";

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    /**
     *  Mensaje de creacion
     */
    public static final String MENSAJE = "Creado por José Luis Acuña";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        btn_ing = (Button)findViewById(R.id.btn_ing);
        btn_retornar = (Button)findViewById(R.id.btn_retuno);
        act_serie = (EditText)findViewById(R.id.act_serie);
        act_nombre = (EditText)findViewById(R.id.act_nombre);
        act_autor = (EditText)findViewById(R.id.act_autir);
        act_audio = (EditText)findViewById(R.id.act_audio);
        act_video = (EditText)findViewById(R.id.act_video);
        ing_json = (TextView)findViewById(R.id.ing_json);

        btn_ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexion = new WebService();
                conexion.execute(INSERT,"1",
                        act_serie.getText().toString(),
                        act_nombre.getText().toString(),
                        act_autor.getText().toString(),
                        act_audio.getText().toString(),
                        act_video.getText().toString());   // Par�metros que recibe doInBackground
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_crear, menu);
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
     * Clase dedicada a cumplir el servicio web
     */
    public class WebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener informacion
            String devuelve = "";
            if (params[1] == "1") {    // Ingreso de los alumnos

                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el Objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idobra", params[2]);
                    jsonParam.put("nombre", params[3]);
                    jsonParam.put("autor", params[4]);
                    jsonParam.put("audio", params[5]);
                    jsonParam.put("video", params[6]);
                    // Envio los par�metros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();

                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            result.append(line);
                            //response+=line;
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON.equals("1")) {      // Si el estado es 1 entonces es porque la operaci�n fue un exito
                            devuelve = "Obra de Arte insertada correctamente";

                        } else if (resultJSON.equals("2")) { // Si el estado es 2 entonces es porque la operaci�n fue un fracaso
                            devuelve = "La Obra de Arte no pudo insertarse";
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
            ing_json.setText(s);
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
