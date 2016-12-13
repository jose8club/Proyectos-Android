package pruebas.sqliteprueba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //Variables de textos
    EditText edtIdentificacion;
    EditText edtNombre;
    //EditText edtAutor;
    //EditText edtTecnica;
    ListView list;

    Controlador controlador;

    TextView serie_lista;
    TextView serie_nombre;
    //TextView serie_autor;
    //TextView serie_tecnica;

    //Mensajes
    public static final String INSERT_EXITO = "Se ingreso exitosamente la nueva obra de arte";
    public static final String INSERT_FALTO = "Faltan datos que deben ser ingresados";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // se mapea las variables de los textos con su lugar en el layout
        edtIdentificacion = (EditText)findViewById(R.id.edtIdentificacion);
        edtNombre = (EditText)findViewById(R.id.edtNombre);
        //edtAutor = (EditText)findViewById(R.id.edtAutor);
        //edtTecnica = (EditText)findViewById(R.id.edtTecnica);
        list = (ListView)findViewById(R.id.listArte);

        //Visualizar tabla
        Cursor cursor = controlador.leerDatos();
        String[] desde = new String[] {
                "Serie",
                "Nombre",
        };
        int[] to = new int[] {
                R.id.miembro_serie,
                R.id.miembro_nombre
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                MainActivity.this, R.layout.formato_lista, cursor, desde, to);

        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);

        // accion cuando hacemos click en item para poder modificarlo o eliminarlo
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                serie_lista = (TextView) findViewById(R.id.miembro_serie);
                serie_nombre = (TextView) findViewById(R.id.miembro_nombre);
                //serie_autor = (TextView) findViewById(R.id.miembro_autor);
                //serie_tecnica = (TextView) findViewById(R.id.miembro_tecnica);

                String miembroSerie = serie_lista.getText().toString();
                String miembroNombre = serie_nombre.getText().toString();
                //String miembroAutor = serie_autor.getText().toString();
                //String miembroTecnica = serie_tecnica.getText().toString();

                Intent modificar = new Intent(getApplicationContext(), Modificar.class);
                modificar.putExtra("miembroSerie", miembroSerie);
                modificar.putExtra("miembroNombre", miembroNombre);
                //modificar.putExtra("miembroAutor", miembroAutor);
                //modificar.putExtra("miembroTecnica", miembroTecnica);
                startActivity(modificar);
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
        //De acuerdo con el icono seleccionado se debe realizar una accion
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:

                //aqui se trabaja con los ingresos a la obra desde los edittext
                String serie = edtIdentificacion.getText().toString();
                String nombre = edtNombre.getText().toString();
                //String autor = edtAutor.getText().toString();
                //String tecnica = edtTecnica.getText().toString();

                //Se valida el ingreso de todos los campos sin excepcion
                if(serie.length()>0 && nombre.length()>0){
                    //Se abre la base de datos de los clientes
                    UsuarioSQLiteHelper user = new UsuarioSQLiteHelper(this,"DbObras",null,1);
                    SQLiteDatabase bd = user.getWritableDatabase();
                    bd.execSQL("INSERT INTO Obra (Serie,Nombre) VALUES ('"
                            + serie + "','"
                            + nombre + "')");
                    bd.close();
                    Toast.makeText(this,INSERT_EXITO,Toast.LENGTH_LONG).show();
                    edtIdentificacion.setText("");
                    edtNombre.setText("");
                    //edtAutor.setText("");
                    //edtTecnica.setText("");

                }else{
                    Toast.makeText(this,INSERT_FALTO,Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.action_Visualizar:

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
