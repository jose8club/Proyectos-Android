package com.jose.iwish.ui.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jose.iwish.R;
import com.jose.iwish.tools.Constantes;
import com.jose.iwish.ui.fragmentos.MainActivityFragment;

/**
 * Actividad principal que contiene un fragmento con una lista.
 * Recuerda que la nueva librería de soporte reemplazó la clase
 * {@link android.support.v7.app.ActionBarActivity} por
 * {@link AppCompatActivity} para el uso de la action bar
 * en versiones antiguas.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creación del fragmento principal
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment(), "MainActivityFragment")
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.CODIGO_DETALLE || requestCode == 3) {
            if (resultCode == RESULT_OK || resultCode == 203) {
                MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().
                        findFragmentByTag("MainFragment");
                fragment.cargarAdaptador();
            }
        }
    }

}
