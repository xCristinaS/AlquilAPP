package c.proyecto.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.pojo.ImagePojo;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;

import c.proyecto.utils.Imagenes;
import c.proyecto.utils.UtilMethods;

public class CrearAnuncio1Activity extends AppCompatActivity {

    private static final String EXTRA_ANUNCIO = "intent anuncio1";
    private static final String EXTRA_USUARIO = "extra user";
    public static final String EXTRA_ANUNCIO_RESULT = "anuncio1Result";

    private static final int RC_CLOSE = 15;
    private static final int RC_ABRIR_GALERIA = 233;
    private static final int RC_CAPTURAR_FOTO = 455;
    public static final int RC_EDITAR_ANUNCIO = 231;

    private TextView lblSiguiente;
    private ImageView imgSiguiente, imgPrincipal, img1, img2, img3, img4, img5, imgSeleccionada;
    private ProgressBar prbPrincipal, prb1, prb2, prb3, prb4, prb5;
    private String mPathOriginal;
    private File[] mImagenesAnuncio;
    private Anuncio mAnuncio;
    private Usuario user;
    private boolean imagesModified;
    private LinkedList<ImagePojo> imagePojoToLoadList;
    private boolean mPermisoEscrituraAceptado;


    public static void start(Context context, Usuario user) {
        Intent intent = new Intent(context, CrearAnuncio1Activity.class);
        intent.putExtra(EXTRA_USUARIO, user);
        context.startActivity(intent);
    }

    public static void startForResult(Activity activity, Anuncio anuncio, Usuario user, int requestCode) {
        Intent intent = new Intent(activity, CrearAnuncio1Activity.class);
        intent.putExtra(EXTRA_USUARIO, user);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio1);
        mAnuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        user = getIntent().getParcelableExtra(EXTRA_USUARIO);
        mImagenesAnuncio = new File[Constantes.NUMERO_IMAGENES_ANUNCIO];
        mPermisoEscrituraAceptado = true;
        initViews();
        recuperarImagenes();
    }

    private void initViews() {
        lblSiguiente = (TextView) findViewById(R.id.lblSiguiente);
        imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgPrincipal = (ImageView) findViewById(R.id.imgPrincipal);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        prbPrincipal = (ProgressBar) findViewById(R.id.prbPrincipal);
        prb1 = (ProgressBar) findViewById(R.id.prb1);
        prb2 = (ProgressBar) findViewById(R.id.prb2);
        prb3 = (ProgressBar) findViewById(R.id.prb3);
        prb4 = (ProgressBar) findViewById(R.id.prb4);
        prb5 = (ProgressBar) findViewById(R.id.prb5);

        showImageDialogList(imgPrincipal);
        showImageDialogList(img1);
        showImageDialogList(img2);
        showImageDialogList(img3);
        showImageDialogList(img4);
        showImageDialogList(img5);

        View.OnClickListener siguienteOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImagenesAnuncio[0] != null) {
                    imgSiguiente.setEnabled(false);
                    lblSiguiente.setEnabled(false);

                    if (imagesModified)
                        CrearAnuncio2Activity.startForResult(CrearAnuncio1Activity.this, mAnuncio, user, RC_CLOSE, mImagenesAnuncio[0], mImagenesAnuncio[1], mImagenesAnuncio[2], mImagenesAnuncio[3], mImagenesAnuncio[4], mImagenesAnuncio[5]);
                    else
                        CrearAnuncio2Activity.startForResult(CrearAnuncio1Activity.this, mAnuncio, user, RC_CLOSE);
                } else
                    Toast.makeText(CrearAnuncio1Activity.this, "Debe cargar una foto para continuar", Toast.LENGTH_SHORT).show();
            }
        };


        imgSiguiente.setOnClickListener(siguienteOnClick);
        lblSiguiente.setOnClickListener(siguienteOnClick);

    }

    private void recuperarImagenes() {
        int contador = 1;
        ImageView[] imgViews = {imgPrincipal, img1, img2, img3, img4, img5};
        ProgressBar[] prbs = {prbPrincipal, prb1, prb2, prb3, prb4, prb5};
        imagePojoToLoadList = new LinkedList<>();

        if (mAnuncio != null)
            if (mAnuncio.getImagenes().size() > 0) {
                for (String keyImg : mAnuncio.getImagenes().keySet()) {
                    ProgressBar prbSeleccionado;
                    ImagePojo imgPojo;
                    if (keyImg.equals(Constantes.FOTO_PRINCIPAL)) {
                        prbSeleccionado = prbs[0];
                        imgPojo = new ImagePojo(imgViews[0], prbSeleccionado, "foto_piso" + 0 + ".jpg", mAnuncio.getImagenes().get(keyImg), 0);
                        imagePojoToLoadList.addFirst(imgPojo);
                    } else {
                        prbSeleccionado = prbs[contador];
                        imgPojo = new ImagePojo(imgViews[contador], prbSeleccionado, "foto_piso" + contador + ".jpg", mAnuncio.getImagenes().get(keyImg), contador);
                        contador++;
                        imagePojoToLoadList.add(imgPojo);
                    }
                    //Se activa la progresBar para indicar que se está cargando la foto
                    prbSeleccionado.setVisibility(View.VISIBLE);
                }
            }
        loadImages1by1();
    }

    private void loadImages1by1() {
        if (imagePojoToLoadList.size() > 0)
            new ImageDownloader().execute(imagePojoToLoadList.getFirst());
    }

    class ImageDownloader extends AsyncTask<ImagePojo, Void, ImagePojo> {

        @Override
        protected ImagePojo doInBackground(ImagePojo... params) {
            try {
                //Se obtiene el bitmap de la URL
                Bitmap bm = Picasso.with(CrearAnuncio1Activity.this).load(params[0].getUrl()).get();
                //Se crea la imagen y se obtiene el descriptor
                File file = guardarBitmapEnArray(bm, getImageViewId(params[0].getImgView()));
                //Se escala la imagen creada.
                bm = Imagenes.escalar(getImageViewWidth(params[0].getImgView()), getImageViewHeight(params[0].getImgView()), file.getAbsolutePath());
                //Se guarda la imagen escalada en su antiguo descriptor sobrescribiendolo.
                file = guardarBitmapEnArray(bm, getImageViewId(params[0].getImgView()));
                //Guarda el descriptor en el objeto que se pasará al postExecute.
                params[0].setFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(ImagePojo imagePojo) {
            super.onPostExecute(imagePojo);
            Picasso.with(CrearAnuncio1Activity.this).load(imagePojo.getFile()).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(imagePojo.getImgView());
            imagePojo.getPrb().setVisibility(View.GONE);
            imagePojoToLoadList.removeFirst();
            loadImages1by1();
        }
    }

    private int getImageViewId(ImageView img) {
        return img.getId();
    }

    private int getImageViewHeight(ImageView img) {
        Point sizes = new Point();
        getWindowManager().getDefaultDisplay().getSize(sizes);

        return (int) (((PercentRelativeLayout.LayoutParams)img.getLayoutParams()).getPercentLayoutInfo().heightPercent * sizes.y);
    }

    private int getImageViewWidth(ImageView img) {
        Point sizes = new Point();
        getWindowManager().getDefaultDisplay().getSize(sizes);

        return (int) (((PercentRelativeLayout.LayoutParams)img.getLayoutParams()).getPercentLayoutInfo().widthPercent * sizes.x);
    }


    private void showImageDialogList(final ImageView img) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idArrayOpciones = R.array.chooseImageWithoutRemoveListItem;
                final ImageView[] imgViews = {imgPrincipal, img1, img2, img3, img4, img5};

                imgSeleccionada = img;

                //Pedirá los permisos de escritura y lectura en ejecución (API >23)
                if (Build.VERSION.SDK_INT >= 23)
                    //Si no se han aceptado los permisos no mostrará el diálogo y saldrá del método
                    if (!UtilMethods.isStoragePermissionGranted(CrearAnuncio1Activity.this) || !mPermisoEscrituraAceptado )
                        return;


                //Comprobar si contiene alguna imagen el imageView para mostrar o no la opción de eliminar.
                for (int i = 0; i < imgViews.length; i++)
                    if (imgViews[i].getId() == img.getId())
                        //Si existe alguna imagen en ese ImageView
                        if (mImagenesAnuncio[i] != null)
                            idArrayOpciones = R.array.chooseImageWithRemoveListItem;


                AlertDialog.Builder dialog = new AlertDialog.Builder(CrearAnuncio1Activity.this);
                dialog.setTitle("Seleccione una de las opciones");
                dialog.setItems(idArrayOpciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se guarda cual fue el último ImageView seleccionado
                        switch (which) {
                            //Galería
                            case 0:
                                openGallery();
                                break;
                            //Cámara
                            case 1:
                                if (Imagenes.hayCamara(CrearAnuncio1Activity.this))
                                    takePhoto();
                                else
                                    Toast.makeText(CrearAnuncio1Activity.this, "Este dispositivo no dispone de cámara", Toast.LENGTH_SHORT).show();
                                break;
                            //Eliminar imagen
                            case 2:
                                for (int i = 0; i < imgViews.length; i++)
                                    if (imgViews[i].getId() == imgSeleccionada.getId()) {
                                        //Eliminar imagen del array
                                        mImagenesAnuncio[i] = null;
                                        //Poner imagen por defecto
                                        imgSeleccionada.setImageDrawable(getResources().getDrawable(R.drawable.sin_imagen));
                                    }
                                imagesModified = true;
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

    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Si hay alguna actividad que sepa realizar la acción
        if (i.resolveActivity(getPackageManager()) != null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            File photoFile = Imagenes.crearArchivoFoto(this, "cameraPhoto.jpeg", false);
            if (photoFile != null) {
                //Se guarda el path del archivo para cuando se haya hecho la captura y se necesite referencia a ella.
                mPathOriginal = photoFile.getAbsolutePath();
                //Se añade como extra del intent la URI donde debe guardarse
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(i, RC_CAPTURAR_FOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case RC_ABRIR_GALERIA:
                    // Se obtiene el path real a partir de la URI retornada por la galería.
                    Uri uriGaleria = data.getData();
                    mPathOriginal = Imagenes.getRealPathFromGallery(this, uriGaleria);
                    new HiloEscalador().execute(imgSeleccionada.getWidth(), imgSeleccionada.getHeight());
                    imagesModified = true;
                    break;
                case RC_CAPTURAR_FOTO:
                    new HiloEscalador().execute(imgSeleccionada.getWidth(), imgSeleccionada.getHeight());
                    imagesModified = true;
                    break;
                case RC_CLOSE:
                    setResult(RESULT_OK, new Intent().putExtra(EXTRA_ANUNCIO_RESULT, data.getParcelableExtra(CrearAnuncio2Activity.EXTRA_ANUNCIO_RESULT)));
                    finish();
                    break;
            }

    }

    class HiloEscalador extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... params) {
            return Imagenes.escalar(params[0], params[1], mPathOriginal);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgSeleccionada.setImageBitmap(bitmap);
            //Guarda la imagen capturada o seleccionada en un array de bitmap para cuando
            //termine de editar o crear el anuncio las suba a prest_internet y no antes.
            guardarBitmapEnArray(bitmap, imgSeleccionada.getId());
        }
    }

    @Nullable
    private File guardarBitmapEnArray(Bitmap bitmap, int idImageView) {
        File f = Imagenes.crearArchivoFoto(this, "foto_piso" + idImageView + ".jpeg", false);
        switch (idImageView) {
            case R.id.imgPrincipal:
                mImagenesAnuncio[0] = f;
                Imagenes.guardarBitmapEnArchivo(bitmap, mImagenesAnuncio[0]);
                return mImagenesAnuncio[0];
            case R.id.img1:
                mImagenesAnuncio[1] = f;
                Imagenes.guardarBitmapEnArchivo(bitmap, mImagenesAnuncio[1]);
                return mImagenesAnuncio[1];
            case R.id.img2:
                mImagenesAnuncio[2] = f;
                Imagenes.guardarBitmapEnArchivo(bitmap, mImagenesAnuncio[2]);
                return mImagenesAnuncio[2];
            case R.id.img3:
                mImagenesAnuncio[3] = f;
                Imagenes.guardarBitmapEnArchivo(bitmap, mImagenesAnuncio[3]);
                return mImagenesAnuncio[3];
            case R.id.img4:
                mImagenesAnuncio[4] = f;
                Imagenes.guardarBitmapEnArchivo(bitmap, mImagenesAnuncio[4]);
                return mImagenesAnuncio[4];
            case R.id.img5:
                mImagenesAnuncio[5] = f;
                Imagenes.guardarBitmapEnArchivo(bitmap, mImagenesAnuncio[5]);
                return mImagenesAnuncio[5];
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case UtilMethods.TAG_WRITE_STORAGE_PERMISION:
                //Si acepta los permisos se volverá a llamar al onClick de imgFoto
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mPermisoEscrituraAceptado = true;
                    imgSeleccionada.callOnClick();
                }
                else
                    mPermisoEscrituraAceptado = false;
                break;
        }


    }

    @Override
    protected void onResume() {
        if(imgSiguiente != null && lblSiguiente != null){
            imgSiguiente.setEnabled(true);
            lblSiguiente.setEnabled(true);
        }

        super.onResume();
    }
}
