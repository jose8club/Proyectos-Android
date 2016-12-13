package jose.multimedia_web;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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


public class Ver extends ActionBarActivity {

    /**
     * ListView creado para poder observar las obras
     */
    ListView lv;

    /**
     * Boton para retornar a la pantalla principal
     */
    Button btn_retornar;

    /**
     * Son los componentes de la lista que se mostraran al usuario
     * id de la obra, nombre y el autor
     */
    ArrayList<String> id;
    ArrayList<String> nombres;
    ArrayList<String> autores;

    /**
     * Es la lista a ingresar de la obra
     */
    ArrayList<Obras> ListaObras;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String OBTENER = IP + "/obras/php/obt_obra.php";

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);
        btn_retornar = (Button)findViewById(R.id.btn_volver);
        lv = (ListView)findViewById(R.id.lista_obras);
        id = new ArrayList<>();
        nombres = new ArrayList<>();
        autores = new ArrayList<>();
        conexion = new WebService();
        conexion.execute(OBTENER, "1"); // Parámetros que recibe doInBackground

        /**
         * Retorna a la pantalla principal
         */
        btn_retornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(u);
            }
        });
    }

    /**
     * Este metodo se creo para llenar la lista con la serie siendo invisible, el nombre y autor de la obra
     * @param id
     * @param nombres
     * @param autores
     * @return obras
     */
    private ArrayList<Obras> GetListaObra(ArrayList<String> id, ArrayList<String> nombres, ArrayList<String> autores) {
        ArrayList<Obras> obras = new ArrayList<Obras>();
        int largo = id.size();
        Obras res;
        for (int i=0; i<largo; i++){
            res = new Obras();
            res.setId(id.get(i).toString());
            res.setNombre(nombres.get(i).toString());
            res.setAutor(autores.get(i).toString());
            obras.add(res);
        }
        return obras;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver, menu);
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
            if (params[1] == "1") {    // Ingreso de los alumnos

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
                                devuelve = devuelve + obrasJSON.getJSONObject(i).getString("idobra") + " " +
                                        obrasJSON.getJSONObject(i).getString("nombre") + " " +
                                        obrasJSON.getJSONObject(i).getString("autor") + " " +
                                        obrasJSON.getJSONObject(i).getString("audio") + " " +
                                        obrasJSON.getJSONObject(i).getString("video") + "\n";
                                id.add(obrasJSON.getJSONObject(i).getString("idobra").toString());
                                nombres.add(obrasJSON.getJSONObject(i).getString("nombre").toString());
                                autores.add(obrasJSON.getJSONObject(i).getString("autor").toString());
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
            ListaObras = GetListaObra(id, nombres, autores);
            lv.setAdapter(new Adaptador(getApplicationContext(), ListaObras));

            /**
             * Actualizar
             */
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView v = (TextView) view.findViewById(R.id.serie_fila);
                    //Toast.makeText(getApplicationContext(), "IdObra: " + v.getText(), Toast.LENGTH_LONG).show();
                    Intent u = new Intent(getApplicationContext(), Editar.class);
                    u.putExtra("idobra",v.getText().toString());
                    startActivity(u);
                }
            });

            /**
             * Eliminar
             */
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView v = (TextView) view.findViewById(R.id.serie_fila);
                    //Toast.makeText(getApplicationContext(), "IdObra: " + v.getText(), Toast.LENGTH_LONG).show();
                    Intent u = new Intent(getApplicationContext(), EliminarDos.class);
                    u.putExtra("idobra", v.getText().toString());
                    startActivity(u);
                    return true;
                }
            });
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
