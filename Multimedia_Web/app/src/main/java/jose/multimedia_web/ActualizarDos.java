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


public class ActualizarDos extends ActionBarActivity {

    /**
     * String que sera los actualizados
     */
    String idobra, nombre, autor, audio, video;
    TextView act_id, act_nombre, act_autor, act_audio, act_video, act_json;;

    /**
     * botones a usar
     */
    Button btn_rec, btn_ret;

    /**
     *    IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String ACTUALIZAR = IP + "/obras/php/actualizar_obra.php";

    /**
     * Se obtiene el servicio web de crear una obra de arte
     */
    WebService conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_dos);
        act_id = (TextView)findViewById(R.id.act_id);
        act_nombre = (TextView)findViewById(R.id.act_nombre);
        act_autor = (TextView)findViewById(R.id.act_autir);
        act_audio = (TextView)findViewById(R.id.act_audio);
        act_video = (TextView)findViewById(R.id.act_video);
        act_json = (TextView)findViewById(R.id.act_json);
        btn_rec = (Button)findViewById(R.id.btn_rec);
        btn_ret = (Button)findViewById(R.id.btn_retonce);

        /**
         * Se obtienen del activity anterior
         */
        Intent intento = getIntent();
        idobra = intento.getExtras().getString("idobra");
        act_id.setText(idobra);
        nombre = intento.getExtras().getString("nombre");
        act_nombre.setText(nombre);
        autor = intento.getExtras().getString("autor");
        act_autor.setText(autor);
        audio = intento.getExtras().getString("audio");
        act_audio.setText(audio);
        video = intento.getExtras().getString("video");
        act_video.setText(video);

        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexion = new WebService();
                conexion.execute(ACTUALIZAR, "1", act_id.getText().toString(),
                        act_nombre.getText().toString(),
                        act_autor.getText().toString(),
                        act_audio.getText().toString(),
                        act_video.getText().toString()); // Parámetros que recibe doInBackground);
            }
        });

        btn_ret.setOnClickListener(new View.OnClickListener() {
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
        getMenuInflater().inflate(R.menu.menu_actualizar_dos, menu);
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
            if (params[1] == "1") {    // update

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
                    jsonParam.put("audio", params[5]);
                    jsonParam.put("video", params[6]);
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
            act_json.setText(s);
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
