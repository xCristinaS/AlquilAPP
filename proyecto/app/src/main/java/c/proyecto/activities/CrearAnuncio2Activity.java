package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.api.ImgurUploader;
import c.proyecto.dialog_fragments.SeleccionPrestacionesDialogFragment;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MyLatLng;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.Prestacion;
import c.proyecto.mvp_presenters.CrearEditarAnuncioPresenter;

public class CrearAnuncio2Activity extends AppCompatActivity implements PrestacionesAdapter.IPrestacionAdapter, SeleccionPrestacionesDialogFragment.ICallBackOnDismiss {

    private static final String TAG_DIALOG_PRESTACIONES = "TAG_PRESTACIONES";
    public static final String EXTRA_ANUNCIO_RESULT = "resultAnuncio2";
    private static final String EXTRA_ANUNCIO = "intentAnuncio2";
    private static final String EXTRA_USUARIO = "extra_user";
    private static final String EXTRA_IMAGE_0 = "img0";
    private static final String EXTRA_IMAGE_1 = "img1";
    private static final String EXTRA_IMAGE_2 = "img2";
    private static final String EXTRA_IMAGE_3 = "img3";
    private static final String EXTRA_IMAGE_4 = "img4";
    private static final String EXTRA_IMAGE_5 = "img5";



    private EditText txtTituloAnuncio, txtNum, txtPoblacion, txtProvincia, txtCamas, txtToilets, txtTamano, txtDescripcion, txtPrecio, txtDireccion;
    private ImageView imgCasa, imgHabitacion, imgPiso;
    private RecyclerView rvPrestaciones, rvHuespedes;

    private PrestacionesAdapter mPrestacionesAdapter;
    private CrearEditarAnuncioPresenter mPresenter;
    private ArrayList<File> imagenesAnuncio;
    private Anuncio mAnuncio;
    private Usuario user;


    public static void startForResult(Activity a, Anuncio anuncio, Usuario user, int requestCode, File img0, File img1, File img2, File img3, File img4, File img5) {
        Intent intent = new Intent(a, CrearAnuncio2Activity.class);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        intent.putExtra(EXTRA_USUARIO, user);
        intent.putExtra(EXTRA_IMAGE_0, img0);
        intent.putExtra(EXTRA_IMAGE_1, img1);
        intent.putExtra(EXTRA_IMAGE_2, img2);
        intent.putExtra(EXTRA_IMAGE_3, img3);
        intent.putExtra(EXTRA_IMAGE_4, img4);
        intent.putExtra(EXTRA_IMAGE_5, img5);
        a.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio2);

        imagenesAnuncio = new ArrayList<>();
        File img0 = (File) getIntent().getExtras().get(EXTRA_IMAGE_0);
        if (img0 != null)
            imagenesAnuncio.add(img0);

        File img1 = (File) getIntent().getExtras().get(EXTRA_IMAGE_1);
        if (img1 != null)
            imagenesAnuncio.add(img1);

        File img2 = (File) getIntent().getExtras().get(EXTRA_IMAGE_2);
        if (img2 != null)
            imagenesAnuncio.add(img2);

        File img3 = (File) getIntent().getExtras().get(EXTRA_IMAGE_3);
        if (img3 != null)
            imagenesAnuncio.add(img3);

        File img4 = (File) getIntent().getExtras().get(EXTRA_IMAGE_4);
        if (img4 != null)
            imagenesAnuncio.add(img4);

        File img5 = (File) getIntent().getExtras().get(EXTRA_IMAGE_5);
        if (img5 != null)
            imagenesAnuncio.add(img5);

        mAnuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        user = getIntent().getParcelableExtra(EXTRA_USUARIO);
        mPresenter = CrearEditarAnuncioPresenter.getPresentador(this);
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, user));

        //Si se entra creando
        if (mAnuncio == null) {
            mAnuncio = new Anuncio();
            mAnuncio.setPrestaciones(new ArrayList<Prestacion>());
            initViews();
        } else {
            initViews();
            recuperarAnuncio();
        }
    }

    private void initViews() {
        txtTituloAnuncio = (EditText) findViewById(R.id.txtTituloAnuncio);
        imgCasa = (ImageView) findViewById(R.id.imgCasa);
        imgHabitacion = (ImageView) findViewById(R.id.imgHabitacion);
        imgPiso = (ImageView) findViewById(R.id.imgPiso);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtNum = (EditText) findViewById(R.id.txtNum);
        txtPoblacion = (EditText) findViewById(R.id.txtPoblacion);
        txtProvincia = (EditText) findViewById(R.id.txtProvincia);
        rvPrestaciones = (RecyclerView) findViewById(R.id.rvPrestaciones);
        txtCamas = (EditText) findViewById(R.id.txtCamas);
        txtToilets = (EditText) findViewById(R.id.txtToilets);
        txtTamano = (EditText) findViewById(R.id.txtTamano);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);
        rvHuespedes = (RecyclerView) findViewById(R.id.rvHuespedes);
        txtDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalizacionActivity.startForResult(CrearAnuncio2Activity.this, mAnuncio);
            }
        });

        confImgTipoVivienda();
        confRecyclerPrestaciones();
        confRecyclerHuespedes();

    }


    private void confImgTipoVivienda() {
        imgCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnuncio.setTipo_vivienda(Constantes.CASA);
                tintVivienda(imgCasa);
            }
        });
        imgHabitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnuncio.setTipo_vivienda(Constantes.HABITACION);
                tintVivienda(imgHabitacion);
            }
        });
        imgPiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnuncio.setTipo_vivienda(Constantes.PISO);
                tintVivienda(imgPiso);
            }
        });
    }

    private void confRecyclerPrestaciones() {
        rvPrestaciones.setHasFixedSize(true);

        mPrestacionesAdapter = new PrestacionesAdapter(mAnuncio.getPrestaciones(), this);
        mPrestacionesAdapter.setEmptyView(findViewById(R.id.emptyViewPrestaciones));
        rvPrestaciones.setAdapter(mPrestacionesAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());
    }

    private void confRecyclerHuespedes() {
        rvHuespedes.setHasFixedSize(true);
    }

    private void recuperarAnuncio() {
        txtTituloAnuncio.setText(mAnuncio.getTitulo());
        switch (mAnuncio.getTipo_vivienda()) {
            case Constantes.CASA:
                tintVivienda(imgCasa);
                break;
            case Constantes.HABITACION:
                tintVivienda(imgHabitacion);
                break;
            case Constantes.PISO:
                tintVivienda(imgPiso);
                break;
        }
        txtDireccion.setText(mAnuncio.getDireccion());
        txtNum.setText(mAnuncio.getNumero());
        txtPoblacion.setText(mAnuncio.getPoblacion());
        txtProvincia.setText(mAnuncio.getProvincia());
        txtCamas.setText(String.valueOf(mAnuncio.getHabitaciones_o_camas()));
        txtToilets.setText(String.valueOf(mAnuncio.getNumero_banios()));
        txtTamano.setText(String.valueOf(mAnuncio.getTamanio()));
        txtDescripcion.setText(mAnuncio.getDescripcion());
        txtPrecio.setText(String.valueOf(mAnuncio.getPrecio()));
    }

    @Override
    public void onPrestacionClicked() {
        showPrestacionesDialog();
    }

    @Override
    public void onEmptyViewClicked() {
        showPrestacionesDialog();
    }

    private void showPrestacionesDialog() {
        FragmentManager fm = getSupportFragmentManager();
        if (mAnuncio.getPrestaciones() == null)
            SeleccionPrestacionesDialogFragment.newInstance(new ArrayList<Prestacion>()).show(fm, TAG_DIALOG_PRESTACIONES);
        else
            SeleccionPrestacionesDialogFragment.newInstance(mAnuncio.getPrestaciones()).show(fm, TAG_DIALOG_PRESTACIONES);
    }


    @Override
    public void onDismiss() {
        mPrestacionesAdapter.actualizarAdapter();
    }

    private void tintVivienda(ImageView view) {
        imgCasa.clearColorFilter();
        imgHabitacion.clearColorFilter();
        imgPiso.clearColorFilter();

        view.setColorFilter(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crear_anuncio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.aceptar).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.aceptar:
                confirmarCambios();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmarCambios() {
        if (requiredFieldsFilled()) {
            if(mAnuncio.getKey() != null){
                //Se borra las imágenes existentes en Firebase para que no se añadan las nuevas + las antiguas si se está editando.
                mAnuncio.setImagenes(null);
                mPresenter.publishNewAdvert(mAnuncio);
                mAnuncio.setImagenes(new HashMap<String, String>());
            }

            //Subir las imagenes a la api de imágenes.
            ArrayList<MyPresenter> presenters = new ArrayList<>();
            presenters.add(mPresenter);
            presenters.add(DetallesAnuncioActivity.getmPresenter());
            for (File f: imagenesAnuncio)
                    ImgurUploader.subirImagen(f, mAnuncio, presenters, imagenesAnuncio.indexOf(f) == 0);
            //Guardar todos los editText en el objeto anuncio.
            meterDatosEnAnuncio();
            //Subir el objeto a FireBase.
            mPresenter.publishNewAdvert(mAnuncio);
            setResult(RESULT_OK, new Intent().putExtra(EXTRA_ANUNCIO_RESULT, mAnuncio));
            finish();
        }
    }

    private boolean requiredFieldsFilled(){
        boolean r = true;
        if (TextUtils.isEmpty(txtTituloAnuncio.getText()) || TextUtils.isEmpty(txtPoblacion.getText()) || TextUtils.isEmpty(txtProvincia.getText()) || TextUtils.isEmpty(txtNum.getText()) ||
                TextUtils.isEmpty(txtPrecio.getText()) || mAnuncio.getTipo_vivienda() == null)
            r = false;
        return r;
    }

    private void meterDatosEnAnuncio(){
        mAnuncio.setTitulo(txtTituloAnuncio.getText().toString());
        mAnuncio.setPoblacion(txtPoblacion.getText().toString());
        mAnuncio.setProvincia(txtProvincia.getText().toString());
        mAnuncio.setNumero(txtNum.getText().toString());
        mAnuncio.setPrecio(Float.valueOf(txtPrecio.getText().toString()).intValue());
        mAnuncio.setDireccion(txtDireccion.getText().toString());
        mAnuncio.setAnunciante(user.getKey());
        mAnuncio.setKey(mAnuncio.generateKey());
        if (mPrestacionesAdapter.getItemCount() > 0)
            mAnuncio.setPrestaciones((ArrayList<Prestacion>) mPrestacionesAdapter.getmDatos());
        if (!TextUtils.isEmpty(txtToilets.getText()))
            mAnuncio.setNumero_banios(Integer.valueOf(txtToilets.getText().toString()));
        if (!TextUtils.isEmpty(txtCamas.getText()))
            mAnuncio.setHabitaciones_o_camas(Integer.valueOf(txtCamas.getText().toString()));
        if (!TextUtils.isEmpty(txtTamano.getText()))
            mAnuncio.setTamanio(Integer.valueOf(txtTamano.getText().toString()));
        if (!TextUtils.isEmpty(txtDescripcion.getText()))
            mAnuncio.setDescripcion(txtDescripcion.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            switch (requestCode){
                case LocalizacionActivity.RC_ADDRESS:
                    Address address = data.getParcelableExtra(LocalizacionActivity.EXTRA_ADDRESS);
                    txtDireccion.setText(address.getThoroughfare());
                    txtNum.setText(address.getSubThoroughfare());
                    txtPoblacion.setText(address.getLocality());
                    txtProvincia.setText(address.getSubAdminArea());
                    //Se guarda la latitud del punto seleccionado para la localización de la vivienda.
                    mAnuncio.setLats(new MyLatLng(address.getLatitude(), address.getLongitude()));
                    break;
            }
    }
}
