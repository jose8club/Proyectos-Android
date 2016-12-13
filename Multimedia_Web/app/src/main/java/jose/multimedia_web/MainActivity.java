package jose.multimedia_web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;


public class MainActivity extends ActionBarActivity {

    /**
     * botones para acceder a las nuevas pantallas
     */
    Button btn_crear, btn_ver, btn_id;

    /**
     * Ver la imagen que se esta viendo por delante por decoracion
     * y la URI que lo representa
     */
    private ImageView imagen;
    public final static String URI = "/storage/emulated/0/Documents/Celular/development.png";

    /**
     *  Mensaje de creacion
     */
    public static final String MENSAJE = "Creado por José Luis Acuña";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_crear = (Button)findViewById(R.id.btn_volver);
        btn_ver = (Button)findViewById(R.id.btn_ver);
        btn_id = (Button)findViewById(R.id.btn_id);
        imagen = (ImageView)findViewById(R.id.image_delantera);

        /**
         * Accede a la creacion de una obra
         */
        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), Crear.class);
                startActivity(u);
            }
        });

        /**
         * Accede a ver a cada obra en una lista
         */
        btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), Ver.class);
                startActivity(u);
            }
        });

        /**
         * Se ve completa la obra con video y audio
         */
        btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(getApplicationContext(), Id.class);
                startActivity(u);
            }
        });

        // Se carga la imagen
        carga_imagen(URI);
        //carga_fondo(URI);
        /**
         * Se ve al presionar la imagen el lugar de almacenamiento del dispositivo
         */
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), URI, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Intento de cargar la imagen desde drawable
     * @param uri
     */
    /*
    private void carga_fondo(String uri) {
        try {
            Uri path = Uri.parse(uri);
            Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(path));
            imagen.setImageBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    */

    /**
     * Este metodo busca cargar la imagen en el sistema desde el telefono
     * @param uri
     */
    private void carga_imagen(String uri) {
        // Se buscar cargar la imagen
        File img = new File(uri);
        if(img.exists())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
            imagen.setImageBitmap(bitmap);
        }
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
}
