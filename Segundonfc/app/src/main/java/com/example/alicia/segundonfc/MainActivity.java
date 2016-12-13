package com.example.alicia.segundonfc;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite;
    EditText txtTagContent;
    public static final String INTENTO_NFC = "Intento de obtener NFC";
    public static final String ERROR_DETECTED = "No se puede encontrar NFC tag";
    public static final String ERROR_ENABLE = "No esta activada la funcionalidad NFC";
    public static final String ERROR_NFC = "Este dispositivo no soporta NFC";
    public static final String ERROR_MSGS = "Mensajes de NFC no encontrados";
    public static final String ERROR_LECT = "No se puede leer NFC";
    public static final String ERROR_WRITABLE = "Tag no es editable";
    public static final String ERROR_TAG = "Tag no es formateable al Ndef";
    public static final String WRITE_OVERLOAD="El mensaje es demasiado largo para este tag";
    public static final String WRITE_SUCESS="Texto Escrito al NFC tag exitoso";
    public static final String WRITE_TAG="Tag formateado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter==null){
            //detecta si el dispositivo tiene o no NFC
            Toast.makeText(this,ERROR_NFC, Toast.LENGTH_LONG).show();
            finish();
        }
        if(!nfcAdapter.isEnabled()){
            //detecta si el dispositivo tiene activado su NFC
            Toast.makeText(this,ERROR_ENABLE, Toast.LENGTH_LONG).show();
            finish();
        }
        tglReadWrite = (ToggleButton)findViewById(R.id.tglReadWrite);
        txtTagContent = (EditText)findViewById(R.id.txtTagContent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!nfcAdapter.isEnabled()){
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
        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        // recupera la actividad intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // empareja intent con pendingintent
        IntentFilter[] intentFilters = new IntentFilter[]{};
        // retoma la actividad del nfcadapter
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        // deshabilita la actividad del nfc adapter
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this,INTENTO_NFC, Toast.LENGTH_LONG).show();
            // obtener el numero de serie del tag
            byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String serie = new String();
            for (int i = 0; i < tagId.length; i++) {
                String x = Integer.toHexString(((int) tagId[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                serie += x + ' ';
            }
            //Si el toogelbutton es para leer
            if(tglReadWrite.isChecked()){
                Parcelable [] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                // si se encuentra el mensaje
                if(parcelables != null && parcelables.length > 0){
                    readTextFromMessage((NdefMessage) parcelables[0], serie);
                    Toast.makeText(this, "Número de serie: " + serie,  Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,ERROR_MSGS, Toast.LENGTH_LONG).show();
                }
            }else{
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                // se crea el mensaje
                NdefMessage ndefMessage = createNdefMessage(txtTagContent.getText()+"");
                // se escribe el mensaje creado en el tag
                writeNdefMessage(tag, ndefMessage);
            }
        }
    }

    /******************************************************************************
     **********************************Leer desde el NFC Tag***********************
     ******************************************************************************/

    private void readTextFromMessage(NdefMessage ndefMessage, String serie) {

        NdefRecord [] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            //txtTagContent.setText(tagContent + "\n" + "\n" + "Número de Serie: " + serie);
            txtTagContent.setText(tagContent);
        }else {
            Toast.makeText(this,ERROR_LECT, Toast.LENGTH_LONG).show();
        }
    }

    private String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding;
            if ((payload[0] & 128) == 0) textEncoding = "UTF-8";
            else textEncoding = "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    /******************************************************************************
     *******************************Fin de Leer desde el NFC Tag*******************
     ******************************************************************************/

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
            return true;
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

    public void tglReadWriteOnClick(View view){
        txtTagContent.setText("");
    }

}