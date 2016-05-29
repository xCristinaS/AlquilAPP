package c.proyecto.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import c.proyecto.R;
import c.proyecto.api.ImgurUploader;
import c.proyecto.dialog_fragments.CaracteristicasUsuarioDialogFragment;
import c.proyecto.dialog_fragments.DescripcionDialogFragment;
import c.proyecto.dialog_fragments.NacionalidadesDialogFragment;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.ProfilePresenter;
import c.proyecto.utils.Imagenes;
import c.proyecto.utils.UtilMethods;

public class EditProfileActivity extends AppCompatActivity implements NacionalidadesDialogFragment.IonNacionalidadClicked {


    private static final int RC_ABRIR_GALERIA = 274;
    private static final int RC_CAPTURAR_FOTO = 433;

    private static final String ARG_USUARIO = "args_user";
    private static final String TAG_DIALOG_HABITOS = "DialogHabitos";
    private static final String TAG_DIALOG_DESCRIPCION = "DialogDescripcion";
    private static final String TAG_DIALOG_NACIONALIDADES = "DialogNacionalidades";

    private EditText txtNombre, txtApellidos, txtFechaNac, txtNacionalidad, txtProfesion, txtComentDesc;
    private ImageView imgFoto, imgCaracteristicas, imgGenero;
    private Button btnConfirmar;
    private Usuario mUser;
    private ProfilePresenter mPresenter;
    private String[] mNationalities;
    private String mPathOriginal;
    private File mFileUserPhoto;
    private boolean mPermisoEscrituraAceptado;

    public static void start(Activity a, Usuario u) {
        Intent intent = new Intent(a, EditProfileActivity.class);
        intent.putExtra(ARG_USUARIO, u);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mNationalities = new String[248];
        getNationalitiesFromFile();
        mPresenter = ProfilePresenter.getPresentador(this);
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));
        mUser = getIntent().getParcelableExtra(ARG_USUARIO);
        initViews();
        recuperarUser();
    }

    private void initViews() {
        imgFoto = (ImageView) findViewById(R.id.slider);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtFechaNac = (EditText) findViewById(R.id.txtFechaNac);
        txtNacionalidad = (EditText) findViewById(R.id.txtNacionalidad);
        txtProfesion = (EditText) findViewById(R.id.txtProfesion);
        txtComentDesc = (EditText) findViewById(R.id.txtComentDesc);
        imgCaracteristicas = (ImageView) findViewById(R.id.imgHabitos);
        imgGenero = (ImageView) findViewById(R.id.imgGenero);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        mPermisoEscrituraAceptado = true;

        showImageDialogList(imgFoto);


        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyPresenter> presenters = new ArrayList<>();
                presenters.add(mPresenter);
                introducirDatosEnUser();
                if (mFileUserPhoto != null)
                    new ImgurUploader(mFileUserPhoto, mUser, presenters);
                mPresenter.updateUserProfile(mUser);
                finish();
            }
        });

        txtFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        imgCaracteristicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaracteristicasDialog();
            }
        });

        imgGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescripcionDialog();
            }
        });

        txtNacionalidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNationalitiesDialog();
            }
        });
    }

    private void introducirDatosEnUser() {
        if (!TextUtils.isEmpty(txtNombre.getText()))
            mUser.setNombre(txtNombre.getText().toString());
        if (!TextUtils.isEmpty(txtApellidos.getText()))
            mUser.setApellidos(txtApellidos.getText().toString());
        if (!TextUtils.isEmpty(txtFechaNac.getText())) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                mUser.setFecha_nacimiento(format.parse(txtFechaNac.getText().toString()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mUser.setComentario_desc(txtComentDesc.getText().toString());
        if (!TextUtils.isEmpty(txtProfesion.getText()))
            mUser.setProfesion(txtProfesion.getText().toString());
        if (!TextUtils.isEmpty(txtNacionalidad.getText()))
            mUser.setNacionalidad(txtNacionalidad.getText().toString());
    }

    private void recuperarUser() {
        Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgFoto);
        txtNombre.setText(mUser.getNombre());
        txtApellidos.setText(mUser.getApellidos());
        if (mUser.getNacionalidad() != null)
            txtNacionalidad.setText(mUser.getNacionalidad());
        if (mUser.getProfesion() != null)
            txtProfesion.setText(mUser.getProfesion());
        if (mUser.getComentario_desc() != null)
            txtComentDesc.setText(mUser.getComentario_desc());
        if (mUser.getFecha_nacimiento() > 0)
            txtFechaNac.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(mUser.getFecha_nacimiento())));
    }

    private void showCaracteristicasDialog() {
        CaracteristicasUsuarioDialogFragment.newInstance(mUser, true).show(getSupportFragmentManager(), TAG_DIALOG_HABITOS);
    }

    private void showDescripcionDialog() {
        DescripcionDialogFragment.newInstance(mUser).show(getSupportFragmentManager(), TAG_DIALOG_DESCRIPCION);
    }

    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        int year, mont, day;
        String[] fechaRecuperada;
        //Si el txt está vacio se mostrará por defecto la fecha actual.
        if (txtFechaNac.getText().toString().isEmpty()) {
            year = currentDate.get(Calendar.YEAR);
            mont = currentDate.get(Calendar.MONTH);
            day = currentDate.get(Calendar.DAY_OF_MONTH);
        } else {
            //Por el contrario, si ya contiene una fecha, mostrará esa por defecto para elegir a partir de esa.
            fechaRecuperada = txtFechaNac.getText().toString().split("/");
            year = Integer.valueOf(fechaRecuperada[2]);
            mont = Integer.valueOf(fechaRecuperada[1]) - 1;
            day = Integer.valueOf(fechaRecuperada[0]);
        }

        DatePickerDialog datePicker = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(selectedyear, selectedmonth, selectedday);
                Date date = cal.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtFechaNac.setText(String.format("%s", format.format(date)));
            }
        }, year, mont, day);

        datePicker.setTitle("Seleccione su fecha");
        datePicker.show();
    }

    private void getNationalitiesFromFile() {
        BufferedReader lector;
        String line, pais;
        int cont = 0;
        try {
            InputStream input = this.getResources().openRawResource(R.raw.countries);
            lector = new BufferedReader(new InputStreamReader(input));
            while ((line = lector.readLine()) != null) {
                pais = line.split(":")[1];
                mNationalities[cont] = pais;
                cont++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNationalitiesDialog() {
        NacionalidadesDialogFragment.newInstance(mNationalities).show(getSupportFragmentManager(), TAG_DIALOG_NACIONALIDADES);
    }

    @Override
    public void onNacionalidadClicked(String nacionalidad) {
        txtNacionalidad.setText(nacionalidad);
    }

    private void showImageDialogList(final ImageView img) {


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pedirá los permisos de escritura y lectura en ejecución (API >23)
                if (Build.VERSION.SDK_INT >= 23)
                    //Si no se han aceptado los permisos no mostrará el diálogo y cancelará el método
                    if (!UtilMethods.isStoragePermissionGranted(EditProfileActivity.this) || !mPermisoEscrituraAceptado )
                        return;

                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this);
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
                                        if (Imagenes.hayCamara(EditProfileActivity.this))
                                            takePhoto();
                                        else
                                            Toast.makeText(EditProfileActivity.this, "Este dispositivo no dispone de cámara", Toast.LENGTH_SHORT).show();
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
                    new HiloEscalador().execute(imgFoto.getWidth(), imgFoto.getHeight());
                    break;
                case RC_CAPTURAR_FOTO:
                    new HiloEscalador().execute(imgFoto.getWidth(), imgFoto.getHeight());
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
            imgFoto.setImageBitmap(bitmap);
            //Guarda la imagen capturada o seleccionada en un array de bitmap para cuando
            //termine de editar o crear el anuncio las suba a prest_internet y no antes.
            mFileUserPhoto = Imagenes.crearArchivoFoto(EditProfileActivity.this, "foto_user.jpeg", false);
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
                    imgFoto.callOnClick();
                }
                else
                    mPermisoEscrituraAceptado = false;
                break;
        }


    }
}
