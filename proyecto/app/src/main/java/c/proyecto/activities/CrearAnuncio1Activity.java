package c.proyecto.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import c.proyecto.R;
import c.proyecto.models.Anuncio;
import c.proyecto.utils.Imagenes;

public class CrearAnuncio1Activity extends AppCompatActivity {

    private static final String INTENT_ANUNCIO = "intent anuncio1";
    private static final int RC_ABRIR_GALERIA = 233;
    private Anuncio mAnuncio;
    private ImageView imgSiguiente;
    private ImageView imgPrincipal;
    private ImageView img1, img2, img3, img4, img5;
    private ImageView imgSeleccionada;
    private String mPathOriginal;


    public static void start(Context context, @Nullable Anuncio anuncio){
        Intent intent = new Intent(context, CrearAnuncio1Activity.class);
        intent.putExtra(INTENT_ANUNCIO, anuncio);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio1);
        mAnuncio = getIntent().getParcelableExtra(INTENT_ANUNCIO);
        initViews();
    }

    private void initViews() {
        imgPrincipal = (ImageView) findViewById(R.id.imgPrincipal);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        showImageDialogList(imgPrincipal);
        showImageDialogList(img1);
        showImageDialogList(img2);
        showImageDialogList(img3);
        showImageDialogList(img4);
        showImageDialogList(img5);


        imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearAnuncio2Activity.start(CrearAnuncio1Activity.this, mAnuncio);
            }
        });
    }

    private void showImageDialogList(final ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CrearAnuncio1Activity.this);
                dialog.setTitle("Seleccione una de las opciones");
                dialog.setItems(R.array.chooseImageListItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se guarda cual fue el ultimo ImageView seleccionado
                        imgSeleccionada = img;
                        switch (which) {
                            //Galería
                            case 0:
                                openGallery();
                                break;
                            //Cámara
                            case 1:

                                break;
                        }
                    }
                });
                dialog.create().show();
            }
        });
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, RC_ABRIR_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            switch (requestCode){
                case RC_ABRIR_GALERIA:
                    // Se obtiene el path real a partir de la uri retornada por la galería.
                    Uri uriGaleria = data.getData();
                    mPathOriginal = Imagenes.getRealPathFromGallery(this, uriGaleria);
                    new HiloEscalador().execute(imgSeleccionada.getWidth(), imgSeleccionada.getHeight());
                    break;
            }

    }

    class HiloEscalador extends AsyncTask<Integer, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Integer... params) {
            return Imagenes.escalar(params[0], params[1], mPathOriginal);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgSeleccionada.setImageBitmap(bitmap);
        }
    }
}
