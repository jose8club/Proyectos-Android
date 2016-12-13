package jose.retrofitget;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

//Class having OnItemClickListener to handle the clicks on list
public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    //Root URL of our web service
    public static final String ROOT_URL = "http://localhost:8080";
    public static final String MENSAJE = "Creado por José Luis Acuña";

    //Strings to bind with intent will be used to send data to other activity
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String AUTOR = "autor";
    public static final String TIPO = "tipo";
    public static final String ESTILO = "estilo";

    //List view to show data
    private ListView listView;

    //List of type books this list will store type Obra which is our data model
    private List<Obra> obras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the listview
        listView = (ListView) findViewById(R.id.listViewBooks);

        //Calling the method that will fetch data
        getObras();

        //Setting onItemClickListener to listview
        listView.setOnItemClickListener(this);
    }

    private void getObras(){
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Buscando Obras","Por favor espere...",false,false);

        //Creating a rest adapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();

        //Creating an object of our api interface
        ObraAPI api = adapter.create(ObraAPI.class);

        //Defining the method
        api.getObra(new Callback<List<Obra>>() {
            @Override
            public void success(List<Obra> list, Response response) {
                //Dismissing the loading progressbar
                loading.dismiss();

                //Storing the data in our list
                obras = list;

                //Calling a method to show the list
                showList();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Fracaso en la busqueda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Our method to show list
    private void showList(){
        //String array to store all the book names
        String[] items = new String[obras.size()];

        //Traversing through the whole list to get all the names
        for(int i=0; i<obras.size(); i++){
            //Storing names to string array
            items[i] = obras.get(i).getNombre();
        }

        //Creating an array adapter for list view
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list,items);

        //Setting adapter to listview
        listView.setAdapter(adapter);
    }


    //This method will execute on listitem click
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Creating an intent
        Intent intent = new Intent(this, ShowBookDetails.class);

        //Getting the requested book from the list
        Obra obra = obras.get(position);

        //Adding book details to intent
        intent.putExtra(ID,obra.getId());
        intent.putExtra(NOMBRE,obra.getNombre());
        intent.putExtra(AUTOR,obra.getAutor());
        intent.putExtra(TIPO,obra.getTipo());
        intent.putExtra(ESTILO,obra.getEstilo());

        //Starting another activity to show book details
        startActivity(intent);
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
            Toast.makeText(this,MENSAJE,Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

}