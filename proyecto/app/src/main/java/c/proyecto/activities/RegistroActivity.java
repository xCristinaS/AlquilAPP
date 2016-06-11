package c.proyecto.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.api.ImgurUploader;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.RegistroActivityOps;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.RegistroPresenter;
import c.proyecto.utils.Imagenes;
import c.proyecto.utils.UtilMethods;

public class RegistroActivity extends AppCompatActivity implements RegistroActivityOps {

    private static final int RC_CAPTURAR_FOTO = 1;
    private static final int RC_ABRIR_GALERIA = 2;
    private TextView lblCancelar;
    private EditText txtUser, txtPass, txtRepeatPass, txtNombre, txtApellidos;
    private Button btnRegistrarse;
    private RegistroPresenter presentador;
    private ImageView imgAvatar;
    private boolean mPermisoEscrituraAceptado;
    private String mPathOriginal;
    private File mFileUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        presentador = RegistroPresenter.getPresentador(this);
        presentador.setUsersManager(new UsersFirebaseManager(presentador));
        mPermisoEscrituraAceptado = true;
        initViews();
    }

    private void initViews() {
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtRepeatPass = (EditText) findViewById(R.id.txtRepeatPass);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        lblCancelar = (TextView) findViewById(R.id.lblCancelar);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);

        showImageDialogList(imgAvatar);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!areFieldsEmpty()){
                    btnRegistrarse.setEnabled(false);
                    existUser();
                }
            }

        });
        lblCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean areFieldsEmpty() {
        boolean empty = false;

        if(txtUser.getText().toString().isEmpty()){
            txtUser.setError("Rellene este campo");
            empty = true;
        }
        if(txtPass.getText().toString().isEmpty()){
            txtPass.setError("Rellene este campo");
            empty = true;
        }
        if(txtRepeatPass.getText().toString().isEmpty()){
            txtRepeatPass.setError("Rellene este campo");
            empty = true;
        }
        if(txtNombre.getText().toString().isEmpty()){
            txtNombre.setError("Rellene este campo");
            empty = true;
        }
        if(txtApellidos.getText().toString().isEmpty()){
            txtApellidos.setError("Rellene este campo");
            empty = true;
        }

        return empty;
    }
    private void existUser(){
        presentador.checkUser(txtUser.getText().toString());
    }

    private boolean comprobarPass(){
        //Comprueba que la contraseña y la contraseña repetida sean iguales
        return !txtPass.getText().toString().isEmpty() && txtPass.getText().toString().equals(txtRepeatPass.getText().toString());
    }

    @Override
    public void createUser(boolean exist) {
        if(!exist){
            if(comprobarPass()){
                //Guarda el usuario registrado
                getSharedPreferences(Constantes.NOMBRE_PREFERENCIAS, MODE_PRIVATE).edit().putString(Constantes.KEY_USER, txtUser.getText().toString()).apply();
                presentador.register(txtUser.getText().toString(), txtPass.getText().toString(), txtNombre.getText().toString(), txtApellidos.getText().toString());
            }else{
                txtRepeatPass.setError("Las contraseñas no son iguales");
                btnRegistrarse.setEnabled(true);
            }
        }else{
            txtUser.setError("Este usuario ya existe");
            btnRegistrarse.setEnabled(true);
        }
    }

    @Override
    public void userHasBeenRegistered(Object o) {
        if (o instanceof Usuario) {
            ArrayList<MyPresenter> presenters = new ArrayList<>();
            presenters.add(presentador);
            if (mFileUserPhoto != null)
                new ImgurUploader(mFileUserPhoto, (Usuario)o, presenters).upload();
            MainActivity.start(RegistroActivity.this, (Usuario)o);
        } else
            Toast.makeText(this, (String) o, Toast.LENGTH_SHORT).show();
    }

    private void showImageDialogList(ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pedirá los permisos de escritura y lectura en ejecución (API >23)
                if (Build.VERSION.SDK_INT >= 23)
                    //Si no se han aceptado los permisos no mostrará el diálogo y cancelará el método
                    if (!UtilMethods.isStoragePermissionGranted(RegistroActivity.this) || !mPermisoEscrituraAceptado )
                        return;

                AlertDialog.Builder dialog = new AlertDialog.Builder(RegistroActivity.this);
                dialog.setTitle("Seleccione una de las opciones");
                dialog.setItems(R.array.chooseImageWithoutRemoveListItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Se guarda cual fue el ultimo ImageView seleccionado
                        switch (which) {
                            //Galería
                            case 0:
                                openGallery();
                                break;
                            //Cámara
                            case 1:
                                if (Imagenes.hayCamara(RegistroActivity.this))
                                    takePhoto();
                                else
                                    Toast.makeText(RegistroActivity.this, "Este dispositivo no dispone de cámara", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                dialog.create().show();

            }

        });
    }

    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Si hay alguna actividad que sepa realizar la acción
        if (i.resolveActivity(getPackageManager()) != null) {

            File photoFile = Imagenes.crearArchivoFoto(this, "cameraPhotoRegister.jpeg", false);
            if (photoFile != null) {
                //Se guarda el path del archivo para cuando se haya hecho la captura y se necesite referencia a ella.
                mPathOriginal = photoFile.getAbsolutePath();
                //Se añade como extra del intent la URI donde debe guardarse
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(i, RC_CAPTURAR_FOTO);
            }
        }
    }


    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, RC_ABRIR_GALERIA);
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
                    new HiloEscalador().execute(imgAvatar.getWidth(), imgAvatar.getHeight());
                    break;
                case RC_CAPTURAR_FOTO:
                    new HiloEscalador().execute(imgAvatar.getWidth(), imgAvatar.getHeight());
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
            imgAvatar.setImageBitmap(bitmap);
            //Guarda la imagen capturada o seleccionada en un array de bitmap para cuando
            //termine de editar o crear el anuncio las suba a prest_internet y no antes.
            mFileUserPhoto = Imagenes.crearArchivoFoto(RegistroActivity.this, "foto_register.jpeg", false);
            Imagenes.guardarBitmapEnArchivo(bitmap, mFileUserPhoto);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case UtilMethods.TAG_WRITE_STORAGE_PERMISION:
                //Si acepta los permisos se volverá a llamar al onClick de imgFoto
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mPermisoEscrituraAceptado = true;
                    imgAvatar.callOnClick();
                }
                else
                    mPermisoEscrituraAceptado = false;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        presentador.liberarMemoria();
        super.onDestroy();
    }
}
