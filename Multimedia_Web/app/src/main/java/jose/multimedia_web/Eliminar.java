package jose.multimedia_web;

import android.content.Intent;
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
import java.util.ArrayList;


public class Eliminar extends ActionBarActivity {

    /**
     * botones de accion
     */
    Button btn_ret;
    Button btn_eliminar;

    /**
     * Spinner con los ide obra de la obra a mostrar
     */
    Spinner sp_id;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayList<String> lista;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String SPINNER = IP + "/obras/php/obt_id.php";

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        btn_ret = (Button)findViewById(R.id.btn_retcinco);
        btn_eliminar = (Button)findViewById(R.id.btn_eliminar);
        sp_id = (Spinner)findViewById(R.id.sp_id);
        /**
         * Se comienza a buscar la lista de idobras y ponerlas en los spinner
         */
        buscar_lista();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_id.setAdapter(spinnerArrayAdapter);

        btn_ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), EliminarDos.class);
                u.putExtra("idobra",sp_id.getSelectedItem().toString());
                startActivity(u);
            }
        });
    }

    /**
     * metodo para llenar el spinner
     */
    private void buscar_lista() {
        lista = new ArrayList<>();
        lista.add("Escoja una obra a cambiar");
        conexion = new WebService();
        conexion.execute(SPINNER, "1"); // Parámetros que recibe doInBackground
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eliminar, menu);
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
            if (params[1] == "1") {
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión con la cadena de conexion
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");
                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

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

                        if (resultJSON.equals("1")) {      // hay obras de arte que mostrar
                            JSONArray obrasJSON = respuestaJSON.getJSONArray("obras");   // obra es el nombre del campo en el JSON
                            for (int i = 0; i < obrasJSON.length(); i++) {
                                devuelve = devuelve + obrasJSON.getJSONObject(i).getString("idobra") + "\n";
                                lista.add(obrasJSON.getJSONObject(i).getString("idobra").toString());
                            }


                        } else if (resultJSON.equals("2")) {
                            devuelve = "No hay obras";
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
            //id_nombre.setText(s.substring(14,70).toString());
            //id_autor.setText(s.substring(72,150).toString());
            //au = s.substring(151,160).toString();
            //vi = s.substring(161,180).toString();
            //Ejecucion(s);
            //id_nombre.setText(s);
            //id_uno.setText(s);
            //el_json.setText(s);
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            //el_json.setText(s);
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
