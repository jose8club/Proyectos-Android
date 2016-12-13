package jose.multimedia_web;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EliminarDos extends ActionBarActivity {

    /**
     * Botones a utilizar
     */
    Button btn_eliminar, btn_retornar;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String ELIMINAR = IP + "/obras/php/eliminar_obra.php";
    String i;

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    /**
     * ID a eliminar y Salida JSON
     */
    TextView id_elim, el_json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_dos);

        btn_retornar = (Button)findViewById(R.id.btn_retdiez);
        btn_eliminar = (Button)findViewById(R.id.btn_borrado);
        el_json = (TextView)findViewById(R.id.el_json);
        id_elim = (TextView)findViewById(R.id.id_eliminar);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        i = intento.getExtras().getString("idobra");
        id_elim.setText(i);

        /**
         * Se retorna a la pantalla principal
         */
        btn_retornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });

        /**
         * Se elimina definitivamente del sistema
         */
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexion = new WebService();
                conexion.execute(ELIMINAR,"1",id_elim.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eliminar_dos, menu);
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
            if (params[1] == "1") {    // delete

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
                    // Envio los parámetros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();//para procesar la peticion
                    writer.close();//para cerrar la peticion

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

                        if (resultJSON.equals("1")) {      // Si el estado es 1 entonces es porque la operación fue un exito
                            devuelve = "Sentencia de borrado ejecutada correctamente";

                        } else if (resultJSON.equals("2")) { // Si el estado es 2 entonces es porque la operación fue un fracaso
                            devuelve = "No hay Obras de Arte que borrar";
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
            el_json.setText(s);
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
