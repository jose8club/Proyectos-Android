package jose.servicioswebv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    public static final String MENSAJE = "Creado por José Luis Acuña con la colaboración de Internet";
    Button consultar;
    Button consultarporid;
    Button insertar;
    Button actualizar;
    Button borrar;
    EditText identificador;
    EditText nombre;
    EditText autor;
    EditText fcreacion;
    EditText fingreso;
    EditText estilo;
    TextView resultado;
    ImageView imageView;
    //VideoView videoView;

    // IP de la Url creada en el hosting
    String IP = "http://jose8android.esy.es";
    // Rutas de los Web Services en el back-end
    String GET = IP + "/obras/obtener_obras.php";
    String GET_BY_ID = IP + "/obras/obtener_obras_por_id.php";
    String UPDATE = IP + "/obras/actualizar_obra.php";
    String DELETE = IP + "/obras/borrar_obra.php";
    String INSERT = IP + "/obras/insertar_obra.php";
    String IMAGENES = IP + "/obras/imagenes/";

    ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlaces con elementos visuales del XML

        consultar = (Button) findViewById(R.id.consultar);
        consultarporid = (Button) findViewById(R.id.consultarid);
        insertar = (Button) findViewById(R.id.insertar);
        actualizar = (Button) findViewById(R.id.actualizar);
        borrar = (Button) findViewById(R.id.borrar);
        identificador = (EditText) findViewById(R.id.eid);
        nombre = (EditText) findViewById(R.id.enombre);
        autor = (EditText) findViewById(R.id.eautor);
        fcreacion = (EditText) findViewById(R.id.efcreacion);
        fingreso = (EditText) findViewById(R.id.efingreso);
        estilo = (EditText) findViewById(R.id.eestilo);
        resultado = (TextView) findViewById(R.id.resultado);
        imageView = (ImageView)findViewById(R.id.imageView);
        //videoView = (VideoView)findViewById(R.id.videoView);

        // Listener de los botones

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiloconexion = new ObtenerWebService();
                hiloconexion.execute(GET,"1"); // Parámetros que recibe doInBackground
            }
        });
        consultarporid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiloconexion = new ObtenerWebService();
                String cadenallamada = GET_BY_ID + "?idobra=" + identificador.getText().toString();
                hiloconexion.execute(cadenallamada,"2");   // Parámetros que recibe doInBackground
                //identificador.setText("");
            }
        });
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiloconexion = new ObtenerWebService();
                hiloconexion.execute(INSERT,"3",
                        nombre.getText().toString(),
                        autor.getText().toString(),
                        fcreacion.getText().toString(),
                        fingreso.getText().toString(),
                        estilo.getText().toString());   // Parámetros que recibe doInBackground
                /*nombre.setText("");
                autor.setText("");
                fcreacion.setText("");
                fingreso.setText("");
                estilo.setText("");*/
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiloconexion = new ObtenerWebService();
                hiloconexion.execute(UPDATE,"4",identificador.getText().toString(),
                        nombre.getText().toString(),
                        autor.getText().toString(),
                        fcreacion.getText().toString(),
                        fingreso.getText().toString(),
                        estilo.getText().toString()); // Parámetros que recibe doInBackground
                /*identificador.setText("");
                nombre.setText("");
                autor.setText("");
                fcreacion.setText("");
                fingreso.setText("");
                estilo.setText("");*/
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiloconexion = new ObtenerWebService();
                hiloconexion.execute(DELETE,"5",identificador.getText().toString());   // Parámetros que recibe doInBackground
                //identificador.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class ObtenerWebService extends AsyncTask<String, Void, String> {

        Bitmap bitmap; //bitmap para la imagen
        //Bitmap bitmap2; //bitmap para el video
        //Uri video;

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve = "";
            //cada una de las opciones es individual
            if (params[1] == "1") {    // Consulta de todos los alumnos

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
                                        obrasJSON.getJSONObject(i).getString("fcreacion") + " " +
                                        obrasJSON.getJSONObject(i).getString("fingreso") + " " +
                                        obrasJSON.getJSONObject(i).getString("estilo") + "\n";

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


            } else if (params[1] == "2") {    // consulta por id

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
                                    respuestaJSON.getJSONObject("obra").getString("fcreacion") + " " +
                                    respuestaJSON.getJSONObject("obra").getString("fingreso") + " " +
                                    respuestaJSON.getJSONObject("obra").getString("estilo");

                            if(respuestaJSON.getJSONObject("obra").getString("rutaimagen").equals("noimagen")){
                                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_fallo);
                            }else{
                                URL urlimagen = new URL(IMAGENES + respuestaJSON.getJSONObject("obra").getString("rutaimagen"));
                                HttpURLConnection conimagen = (HttpURLConnection) urlimagen.openConnection();
                                conimagen.connect();
                                bitmap = BitmapFactory.decodeStream(conimagen.getInputStream());
                            }

                            /** Aun no se puede desarrollar la parte de video por no poder soportar video

                            if(respuestaJSON.getJSONObject("obra").getString("rutavideo").equals("novideo")){
                                videoView.setVisibility(View.INVISIBLE);
                            }else{
                                String uri = IMAGENES + respuestaJSON.getJSONObject("obra").getString("rutavideo");
                                Uri urlvideo = Uri.parse(uri);
                                video = urlvideo;
                            }
                             *
                             */

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


            } else if (params[1] == "3") {    // insert

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
                    jsonParam.put("nombre", params[2]);
                    jsonParam.put("autor", params[3]);
                    jsonParam.put("fcreacion", params[4]);
                    jsonParam.put("fingreso", params[5]);
                    jsonParam.put("estilo", params[6]);
                    // Envio los parámetros post.
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

                        if (resultJSON.equals("1")) {      // Si el estado es 1 entonces es porque la operación fue un exito
                            devuelve = "Obra de Arte insertada correctamente";

                        } else if (resultJSON.equals("2")) { // Si el estado es 2 entonces es porque la operación fue un fracaso
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

            } else if (params[1] == "4") {    // update

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
                    jsonParam.put("idobra",params[2]);
                    jsonParam.put("nombre", params[3]);
                    jsonParam.put("autor", params[4]);
                    jsonParam.put("fcreacion", params[5]);
                    jsonParam.put("fingreso", params[6]);
                    jsonParam.put("estilo", params[7]);
                    // Envio los parámetros post.
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

                        if (resultJSON.equals("1")) {      // Si el estado es 1 entonces es porque la operación fue un exito
                            devuelve = "Obra de Arte actualizada correctamente";

                        } else if (resultJSON.equals("2")) { // Si el estado es 2 entonces es porque la operación fue un fracaso
                            devuelve = "La Obra de Arte no pudo actualizarse";
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

            } else if (params[1] == "5") {    // delete

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
            resultado.setText(s);
            //ver video no puede aplicarse debido a que no soporta el contenido
            //videoView.setVideoURI(video);
            //videoView.requestFocus();
            //videoView.start();
            //ver imagen
            imageView.setImageBitmap(bitmap);

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
