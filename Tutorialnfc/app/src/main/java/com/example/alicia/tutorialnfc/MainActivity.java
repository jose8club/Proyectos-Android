package com.example.alicia.tutorialnfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MainActivity extends Activity {

    //Variables Globales
    public static final String ERROR_DETECTED="Etiqueta NFC NO Detectada";
    public static final String WRITE_SUCESS="Texto Escrito al NFC tag exitoso";
    public static final String WRITE_ERROR_IO="Error durante la escritura debido a error de I/O";
    public static final String WRITE_ERROR_FORMAT="Error durante la escritura debido a error de Formato";
    public static final String WRITE_OVERLOAD="El mensaje es demasiado largo para este tag";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;

    TextView tvNFCContent; //Contenido del tag
    TextView message;      //Mensaje a escribir
    Button btnWrite;       //Boton write

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //interfaz grafica
        context=this;

        tvNFCContent=(TextView)findViewById(R.id.nfc_contents);
        message=(TextView)findViewById(R.id.edit_message);
        btnWrite=(Button)findViewById(R.id.button);

        btnWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if(myTag ==null) {
                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        write(message.getText().toString(), myTag);
                    }
                } catch (IOException e) {
                    Toast.makeText(context, WRITE_ERROR_IO, Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, WRITE_ERROR_FORMAT, Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });

        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter==null){
            //detecta si el dispositivo tiene o no NFC
            Toast.makeText(this,"Este dispositivo no soporta NFC", Toast.LENGTH_LONG).show();
            finish();
        }

        readFromIntent(getIntent()); // Metodo que lee NFC
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};

    }

    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs=intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs=null;
            if(rawMsgs!=null){
                msgs=new NdefMessage[rawMsgs.length];
                for (int i=0; i<rawMsgs.length; i++){
                    msgs[i]=(NdefMessage)rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if(msgs==null || msgs.length==0)return;
        String text="";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding; // Get the Text Encoding
        if ((payload[0] & 128) == 0) textEncoding = "UTF-8";
        else textEncoding = "UTF-16";
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Obtener el texto
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        tvNFCContent.setText("Contenido NFC: " + "\n" + text);
    }

    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Obtiene una instancia Ndf para el tag.
        Ndef ndef = Ndef.get(tag);
        // Permite I/O
        ndef.connect();
        // Verifica si el mensaje es demasiado largo para la capacidad del tag
        if(ndef.getMaxSize()<message.toByteArray().length){
            Toast.makeText(context, WRITE_OVERLOAD, Toast.LENGTH_LONG ).show();
            Toast.makeText(context, "Capacidad maxima: "+ ndef.getMaxSize() + " bytes", Toast.LENGTH_LONG ).show();
            Toast.makeText(context, "Peso del mensaje: "+ message.toByteArray().length + " bytes", Toast.LENGTH_LONG ).show();
        }else{
            // Escribe el mensaje
            ndef.writeNdefMessage(message);
            Toast.makeText(context, WRITE_SUCESS, Toast.LENGTH_LONG ).show();
            Toast.makeText(context, "Capacidad maxima: "+ ndef.getMaxSize() + " bytes", Toast.LENGTH_LONG ).show();
            Toast.makeText(context, "Peso del mensaje: "+ message.toByteArray().length + " bytes", Toast.LENGTH_LONG ).show();
        }
        // Cierra la conexión
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException{
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // Establece estatus del byte (ver NDEF spec para actual bits)
        payload[0] = (byte) langLength;

        // copia langbytes y textbytes en el payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }
    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
