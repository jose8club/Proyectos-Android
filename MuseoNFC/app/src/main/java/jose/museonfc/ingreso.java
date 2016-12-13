package jose.museonfc;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ingreso extends ActionBarActivity {

    ArrayList<String> lista;
    ArrayAdapter<String> spinnerArrayAdapter;
    Button btn_fecha1;
    Button btn_fecha2;
    Button btn_guardar;
    Button btn_inicio;
    Button btn_lista;
    TextView txt_fecha_creacion;
    TextView txt_fecha_ingreso;
    EditText txt_nacionalidad;
    Spinner spinner;
    public static final String ERROR_UNO="Deben ingresar todos los valores";
    public static final String MENSAJE = "Creado por José Luis Acuña";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        spinner = (Spinner)findViewById(R.id.spinnerObras);
        txt_nacionalidad = (EditText)findViewById(R.id.txt_nacionalidad);
        txt_fecha_creacion = (TextView)findViewById(R.id.txt_fecha_creacion);
        txt_fecha_ingreso = (TextView)findViewById(R.id.txt_fecha_ingreso);
        btn_fecha1 = (Button)findViewById(R.id.btn_fecha1);
        btn_fecha2 = (Button)findViewById(R.id.btn_fecha2);
        btn_guardar = (Button)findViewById(R.id.btn_guardar);
        btn_lista = (Button)findViewById(R.id.btn_lista);
        btn_inicio = (Button)findViewById(R.id.btn_inicio);

        //llenar spinner
        BD db = new BD(getApplicationContext(),null,null,1);
        lista = db.llenar_spinner();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        btn_fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragmentDos();
                newFragment.show(getSupportFragmentManager(), "datePickerDos");
            }
        });

        btn_fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_fecha_creacion.getText().toString().trim().equals("")||
                        txt_fecha_ingreso.getText().toString().trim().equals("")||
                        txt_nacionalidad.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), ERROR_UNO, Toast.LENGTH_LONG).show();
                }else{
                    BD db = new BD(getApplicationContext(),null,null,1);
                    String Buscar = spinner.getSelectedItem().toString();
                    String fechauno = txt_fecha_creacion.getText().toString();
                    String nacionalidad = txt_nacionalidad.getText().toString();
                    String fechados = txt_fecha_ingreso.getText().toString();
                    String Mensaje =db.actualizar_vacios(Buscar, fechauno, nacionalidad, fechados);
                    //String Mensaje =db.actualizar(Buscar, Serie, Nombre);
                    Toast.makeText(getApplicationContext(),Mensaje,Toast.LENGTH_SHORT).show();
                    txt_fecha_creacion.setText("");
                    txt_nacionalidad.setText("");
                    txt_fecha_ingreso.setText("");
                }
            }
        });

        btn_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_nacionalidad.setText("");
                txt_fecha_creacion.setText("");
                txt_fecha_ingreso.setText("");
                Intent busqueda = new Intent(getApplicationContext(), Lista.class);
                startActivity(busqueda);
            }
        });

        btn_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(z);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingreso, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        spinner.setAdapter(spinnerArrayAdapter);
    }
}
