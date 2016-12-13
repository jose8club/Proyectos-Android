package jose.museonfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class ActualizarNfc extends ActionBarActivity {

    NfcAdapter nfcAdapter_act;
    EditText nfc_nombre_act;
    EditText nfc_autor_act;
    EditText nfc_tipo_act;
    EditText nfc_estilo_act;
    Button btn_act;

    public static final String INTENTO_NFC = "Intento de obtener NFC";
    public static final String ACTUALIZACION_NFC = "Se actualizo el mensaje del tag NFC";
    public static final String ELIMINACION_NFC = "Se elimino el mensaje del tag NFC";
    public static final String ERROR_DETECTED = "No se puede encontrar NFC tag";
    public static final String ERROR_ENABLE = "No esta activada la funcionalidad NFC";
    public static final String ERROR_NFC = "Este dispositivo no soporta NFC";
    public static final String ERROR_WRITABLE = "Tag no es editable";
    public static final String ERROR_TAG = "Tag no es formateable al Ndef";
    public static final String MENSAJE = "Creado por José Luis Acuña";
    public static final String WRITE_OVERLOAD="El mensaje es demasiado largo para este tag";
    public static final String WRITE_SUCESS="Texto Escrito al NFC tag exitoso";
    public static final String WRITE_TAG="Tag formateado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nfc);

        nfcAdapter_act = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter_act==null){
            //detecta si el dispositivo tiene o no NFC
            Toast.makeText(this, ERROR_NFC, Toast.LENGTH_LONG).show();
            finish();
        }
        if(!nfcAdapter_act.isEnabled()){
            //detecta si el dispositivo tiene activado su NFC
            Toast.makeText(this,ERROR_ENABLE, Toast.LENGTH_LONG).show();
            finish();
        }
        nfc_nombre_act = (EditText)findViewById(R.id.nfc_nombre_act);
        nfc_autor_act = (EditText)findViewById(R.id.nfc_autor_act);
        nfc_tipo_act = (EditText)findViewById(R.id.nfc_tipo_act);
        nfc_estilo_act = (EditText)findViewById(R.id.nfc_estilo_act);
        btn_act = (Button)findViewById(R.id.btn_act);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        String nnombre = intento.getExtras().getString("nombre");
        String nautor = intento.getExtras().getString("autor");
        String ntipo = intento.getExtras().getString("tipo");
        String nestilo = intento.getExtras().getString("estilo");
        nfc_nombre_act.setText(nnombre);
        nfc_autor_act.setText(nautor);
        nfc_tipo_act.setText(ntipo);
        nfc_estilo_act.setText(nestilo);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!nfcAdapter_act.isEnabled()){
            //detecta si el dispositivo tiene activado su NFC
            Toast.makeText(this,ERROR_ENABLE, Toast.LENGTH_LONG).show();
            finish();
        }

        enableForegroundDispatchSystem();


    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    private void enableForegroundDispatchSystem() {
        // definicion de la actividad a retomar
        Intent intent = new Intent(this, ActualizarNfc.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        // recupera la actividad intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // empareja intent con pendingintent
        IntentFilter[] intentFilters = new IntentFilter[]{};
        // retoma la actividad del nfcadapter
        nfcAdapter_act.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        // deshabilita la actividad del nfc adapter
        nfcAdapter_act.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this,INTENTO_NFC, Toast.LENGTH_LONG).show();
            final Intent intentl = intent;
            btn_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tag tag = intentl.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    // se crea el mensaje
                    NdefMessage ndefMessage = createNdefMessage(nfc_nombre_act.getText()+";"+
                            nfc_autor_act.getText()+";"+nfc_tipo_act.getText()+";"+nfc_estilo_act.getText()+"");
                    // se escribe el mensaje creado en el tag
                    writeNdefMessage(tag, ndefMessage);

                    if(nfc_nombre_act.getText().toString().trim().equals("") &&
                            nfc_autor_act.getText().toString().trim().equals("") &&
                            nfc_tipo_act.getText().toString().trim().equals("") &&
                            nfc_estilo_act.getText().toString().trim().equals("")){
                        Toast.makeText(getApplicationContext(),ELIMINACION_NFC, Toast.LENGTH_LONG).show();
                        Intent busqueda = new Intent(getApplicationContext(), Lista.class);
                        startActivity(busqueda);
                    }else{
                        Toast.makeText(getApplicationContext(),ACTUALIZACION_NFC, Toast.LENGTH_LONG).show();
                        Intent busqueda = new Intent(getApplicationContext(), Lista.class);
                        startActivity(busqueda);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actualizar_nfc, menu);
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

    /******************************************************************************
     **********************************Escribir hacia el NFC Tag*******************
     ******************************************************************************/

    private NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if(tag==null){
                Toast.makeText(this,ERROR_DETECTED, Toast.LENGTH_LONG).show();
                return;
            }
            // Obtiene una instancia NDef para el tag
            Ndef ndef = Ndef.get(tag);

            if(ndef==null){
                //formatea el tag y formatea el mensaje en formato ndef
                formatTag(tag, ndefMessage);
            }else {
                // habilita I/O
                ndef.connect();

                //verificar si el tag es editable
                if(!ndef.isWritable()){
                    Toast.makeText(this, ERROR_WRITABLE, Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }
                // Verifica si el mensaje es demasiado largo para la capacidad del tag
                if(ndef.getMaxSize()<ndefMessage.toByteArray().length){
                    Toast.makeText(this, WRITE_OVERLOAD, Toast.LENGTH_LONG ).show();
                    Toast.makeText(this, "Capacidad maxima: "+ ndef.getMaxSize() + " bytes", Toast.LENGTH_LONG ).show();
                    Toast.makeText(this, "Peso del mensaje: "+ ndefMessage.toByteArray().length + " bytes", Toast.LENGTH_LONG ).show();
                }else{
                    // Escribe el mensaje
                    ndef.writeNdefMessage(ndefMessage);
                    Toast.makeText(this, WRITE_SUCESS, Toast.LENGTH_LONG ).show();
                    Toast.makeText(this, "Capacidad maxima: "+ ndef.getMaxSize() + " bytes", Toast.LENGTH_LONG ).show();
                    Toast.makeText(this, "Peso del mensaje: "+ ndefMessage.toByteArray().length + " bytes", Toast.LENGTH_LONG ).show();
                }
                ndef.close();
            }
        }catch (Exception e){
            Log.e("writeNdefMessage", e.getMessage());
        }
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormateable = NdefFormatable.get(tag);

            if(ndefFormateable==null){
                Toast.makeText(this, ERROR_TAG, Toast.LENGTH_SHORT).show();
                return;
            }

            ndefFormateable.connect();
            ndefFormateable.format(ndefMessage);
            ndefFormateable.close();
            Toast.makeText(this, WRITE_TAG, Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Log.e("formatTag", e.getMessage());
        }
    }

    /******************************************************************************
     *******************************Fin de escribir hacia el NFC Tag***************
     ******************************************************************************/
}
