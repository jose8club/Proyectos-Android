package jose.multimedia_web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class Video extends Activity {

    /**
     * Dialog de carga
     */
    ProgressDialog dialog;

    /**
     * Video a reproducir
     */
    VideoView video_obra;

    /**
     * link del video
     */
    String vid;

    /**
     *  IP de la Url creada en el hosting
     */
    String IP = "http://jose8android.esy.es";
    String OBRA = IP + "/obras/video/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video_obra = (VideoView) findViewById(R.id.video_obra);

        // se obtiene el intent que paso a esta etapa y lo cambia
        Intent intento = getIntent();
        vid = intento.getExtras().getString("video");
        //Toast.makeText(getApplicationContext(), (OBRA + vid), Toast.LENGTH_LONG).show();

        // Crear process dialog para la carga
        dialog = new ProgressDialog(Video.this);
        // Poner titulo
        dialog.setTitle("Video de Obra de Arte");
        // Poner mensaje
        dialog.setMessage("Cargando...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        // Mostrar Barra de progreso
        dialog.show();

        try {
            // Incializar MediaController
            MediaController media = new MediaController(Video.this);
            media.setAnchorView(video_obra);
            // Uso del URL del video
            Uri video = Uri.parse(OBRA + vid);
            video_obra.setMediaController(media);
            video_obra.setVideoURI(video);

        } catch (Exception e) {
            e.printStackTrace();
        }

        video_obra.requestFocus();
        video_obra.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // cerrar el dialog e inciar el video
            public void onPrepared(MediaPlayer mp) {
                dialog.dismiss();
                video_obra.start();
            }
        });

        /**
         * Se reproduce el video de esta manera
         */
        /*
        Uri path = Uri.parse(OBRA + vid);
        video_obra.setVideoURI(path);
        video_obra.setMediaController(new MediaController(this));
        video_obra.start();
        */
    }

}
