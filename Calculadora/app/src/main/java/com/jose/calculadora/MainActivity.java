package com.jose.calculadora;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    /**
     * Variables Globales
     */
    RadioGroup contenedorRadios;
    TextView operador, resultado;
    public static final String MENSAJE = "Creado por José Luis Acuña y Jaime Revelo";
    EditText numA, numB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Obtener el contenedor de los radio button y el resultado
         */
        contenedorRadios = (RadioGroup)findViewById(R.id.radioOperaciones);
        contenedorRadios.check(R.id.radioSuma);
        resultado = (TextView)findViewById(R.id.textoResultado);

        /**
         *
         */
        contenedorRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                operador = (TextView)findViewById(R.id.textoOperador);

                switch (contenedorRadios.getCheckedRadioButtonId()){
                    case R.id.radioSuma:
                        operador.setText("+");
                        break;
                    case R.id.radioResta:
                        operador.setText("-");
                        break;
                    case R.id.radioMultipicacion:
                        operador.setText("*");
                        break;
                    case R.id.radioDivision:
                        operador.setText("/");
                        break;
                }
            }
        });

    }

    public void onClickOperar(View v){

        /**
         * Variables Locales
         */
        int a,b;
        double res = 0;


        /**
         * Obtener los campos de edicion
         */
        numA = (EditText)findViewById(R.id.editNumeroA);
        numB = (EditText)findViewById(R.id.editNumeroB);

        /**
         * Convertir los numeros en enteros
         */
        a = Integer.parseInt(numA.getText().toString());
        b = Integer.parseInt(numB.getText().toString());

        /**
         * Calculo del resultado
         */
        switch (contenedorRadios.getCheckedRadioButtonId()){
            case R.id.radioSuma:
                res = a+b;
                break;
            case R.id.radioResta:
                res = a-b;
                break;
            case R.id.radioMultipicacion:
                res = a*b;
                break;
            case R.id.radioDivision:
                if(b==0){
                    res = 0;
                }else{
                    res = a/b;
                }
                break;
        }

        /**
         * Enseñar el resultado
         */
        resultado.setText(String.valueOf(res));
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

    /**
    @Override
    protected void onPause() {
        super.onPause();
        contenedorRadios.check(R.id.radioSuma);
        operador.setText("+");
        numA.setText("");
        numB.setText("");
        resultado.setText("0");
    }
    */

    /**
    @Override
    protected void onResume() {
        super.onResume();
        contenedorRadios.check(R.id.radioSuma);
        operador.setText("+");
        numA.setText("");
        numB.setText("");
        resultado.setText("0");
    }
    */
}
