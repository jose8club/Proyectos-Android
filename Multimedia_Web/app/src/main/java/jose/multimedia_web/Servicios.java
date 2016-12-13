package jose.multimedia_web;

import android.os.AsyncTask;

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

/**
 * Created by Alicia on 09/12/2016.
 */
public class Servicios {

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String ID = IP + "/obras/php/obt_id.php";
    String NOMBRES = IP + "/obras/php/obt_nombre.php";
    String AUTORES = IP + "/obras/php/obt_autor.php";

    /**
     * Los arraylist que retornaran como variables globales
     */
    ArrayList<String> id;
    ArrayList<String> nombre;
    ArrayList<String> autor;

    /**
     *    Se obtienen las idobras
     */
    public ArrayList<String> obt_id() {
        id = new ArrayList<>();
        id.add("Escoja una obra a visualizar");
        conexion = new WebService();
        conexion.execute(ID, "1"); // Parámetros que recibe doInBackground
        return id;
    }

    /**
     *    Se obtienen los nombres
     */
    public ArrayList<String> obt_nombre() {
        nombre = new ArrayList<>();
        conexion = new WebService();
        conexion.execute(NOMBRES, "2"); // Parámetros que recibe doInBackground
        return nombre;
    }

    /**
     *    Se obtienen los autores
     */
    public ArrayList<String> obt_autor() {
        autor = new ArrayList<>();
        conexion = new WebService();
        conexion.execute(AUTORES, "3"); // Parámetros que recibe doInBackground
        return autor;
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
                                devuelve = devuelve + obrasJSON.getJSONObject(i).getString("idobra") + "\n";
                                id.add(obrasJSON.getJSONObject(i).getString("idobra").toString());
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

            }else if (params[1] == "2") {    // Ingreso de los alumnos

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
                                devuelve = devuelve + obrasJSON.getJSONObject(i).getString("nombre") + "\n";
                                nombre.add(obrasJSON.getJSONObject(i).getString("nombre").toString());
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

            }else if (params[1] == "3") {    // Ingreso de los alumnos

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
                                devuelve = devuelve + obrasJSON.getJSONObject(i).getString("autor") + "\n";
                                autor.add(obrasJSON.getJSONObject(i).getString("autor").toString());
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
