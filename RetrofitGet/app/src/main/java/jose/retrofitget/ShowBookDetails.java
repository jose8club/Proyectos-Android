package jose.retrofitget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowBookDetails extends AppCompatActivity {

    //Defining views
    private TextView textViewObraId;
    private TextView textViewObraNombre;
    private TextView textViewObraAutor;
    private TextView textViewObraTipo;
    private TextView textViewObraEstilo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_details);

        //Initializing Views
        textViewObraId = (TextView) findViewById(R.id.textViewObraId);
        textViewObraNombre = (TextView) findViewById(R.id.textViewObraNombre);
        textViewObraAutor = (TextView) findViewById(R.id.textViewObraAutor);
        textViewObraTipo = (TextView) findViewById(R.id.textViewObraTipo);
        textViewObraEstilo = (TextView) findViewById(R.id.textViewObraEstilo);

        //Getting intent
        Intent intent = getIntent();

        //Displaying values by fetching from intent
        textViewObraId.setText(String.valueOf(intent.getIntExtra(MainActivity.ID, 0)));
        textViewObraNombre.setText(intent.getStringExtra(MainActivity.NOMBRE));
        textViewObraAutor.setText(intent.getStringExtra(MainActivity.AUTOR));
        textViewObraTipo.setText(intent.getStringExtra(MainActivity.TIPO));
        textViewObraEstilo.setText(intent.getStringExtra(MainActivity.ESTILO));
    }
}