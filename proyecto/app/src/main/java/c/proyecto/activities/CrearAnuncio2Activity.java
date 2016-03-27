package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.api.ImgurAPI;
import c.proyecto.api.ImgurResponse;
import c.proyecto.fragments.SeleccionPrestacionesDialogFragment;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.Prestacion;
import c.proyecto.presenters.CrearEditarAnuncioPresenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearAnuncio2Activity extends AppCompatActivity implements PrestacionesAdapter.IPrestacionAdapter, SeleccionPrestacionesDialogFragment.ICallBackOnDismiss {

    private static final String TAG_DIALOG_PRESTACIONES = "TAG_PRESTACIONES";
    private static final String EXTRA_ANUNCIO = "intentAnuncio2";
    private static final String EXTRA_USUARIO = "extra_user";
    private static final String EXTRA_IMAGE_0 = "img0";
    private static final String EXTRA_IMAGE_1 = "img1";
    private static final String EXTRA_IMAGE_2 = "img2";
    private static final String EXTRA_IMAGE_3 = "img3";
    private static final String EXTRA_IMAGE_4 = "img4";
    private static final String EXTRA_IMAGE_5 = "img5";

    private TextView txtTituloAnuncio, txtDireccion, txtNum, txtPoblacion, txtProvincia, txtCamas, txtToilets, txtTamano, txtDescripcion, txtPrecio;
    private ImageView imgCasa, imgHabitacion, imgPiso;
    private RecyclerView rvPrestaciones, rvHuespedes;

    private PrestacionesAdapter mPrestacionesAdapter;
    private CrearEditarAnuncioPresenter mPresenter;
    private ArrayList<File> imagenesAnuncio;
    private Anuncio mAnuncio;
    private Usuario user;

    public static void start(Context context, Anuncio anuncio, Usuario user, File img0, File img1, File img2, File img3, File img4, File img5) {
        Intent intent = new Intent(context, CrearAnuncio2Activity.class);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        intent.putExtra(EXTRA_USUARIO, user);
        intent.putExtra(EXTRA_IMAGE_0, img0);
        intent.putExtra(EXTRA_IMAGE_1, img1);
        intent.putExtra(EXTRA_IMAGE_2, img2);
        intent.putExtra(EXTRA_IMAGE_3, img3);
        intent.putExtra(EXTRA_IMAGE_4, img4);
        intent.putExtra(EXTRA_IMAGE_5, img5);
        context.startActivity(intent);
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
        txtTituloAnuncio = (TextView) findViewById(R.id.txtTituloAnuncio);
        imgCasa = (ImageView) findViewById(R.id.imgCasa);
        imgHabitacion = (ImageView) findViewById(R.id.imgHabitacion);
        imgPiso = (ImageView) findViewById(R.id.imgPiso);
        txtDireccion = (TextView) findViewById(R.id.txtDireccion);
        txtNum = (TextView) findViewById(R.id.txtNum);
        txtPoblacion = (TextView) findViewById(R.id.txtPoblacion);
        txtProvincia = (TextView) findViewById(R.id.txtProvincia);
        rvPrestaciones = (RecyclerView) findViewById(R.id.rvPrestaciones);
        txtCamas = (TextView) findViewById(R.id.txtCamas);
        txtToilets = (TextView) findViewById(R.id.txtToilets);
        txtTamano = (TextView) findViewById(R.id.txtTamano);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtPrecio = (TextView) findViewById(R.id.txtPrecio);
        rvHuespedes = (RecyclerView) findViewById(R.id.rvHuespedes);
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
        txtCamas.setText(mAnuncio.getHabitaciones_o_camas());
        txtToilets.setText(mAnuncio.getNumero_banios());
        txtTamano.setText(String.format("%d%s", mAnuncio.getTamanio(), Constantes.UNIDAD));
        txtDescripcion.setText(mAnuncio.getDescripcion());
        txtPrecio.setText(String.format("%.2f%s", mAnuncio.getPrecio(), Constantes.MONEDA));
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
        getMenuInflater().inflate(R.menu.menu_crear_editar_anuncio, menu);
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
            //Subir las imagenes a la api de imágenes.
            new Uploader().execute(imagenesAnuncio);
            //Guardar todos los editText en el objeto anuncio.
            meterDatosEnAnuncio();
            //Subir el objeto a FireBase.
            mPresenter.publishNewAdvert(mAnuncio);
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
        mAnuncio.setPrecio(Integer.valueOf(txtPrecio.getText().toString()));
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

    //Sube las imagenes a la Api Imgur y guarda las url que den como resultado en el objeto Anuncio.
    class Uploader extends AsyncTask<ArrayList<File>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<File>... params) {
            for (int i = 0; i < params[0].size(); i++)
                if (params[0].get(i) != null)
                    subirImagen(params[0].get(i));

            return null;
        }
    }

    private void subirImagen(File file) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

        Call<ImgurResponse> llamada = ImgurAPI.getMInstance().getService().uploadImage(body);
        llamada.enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                ImgurResponse respuesta = response.body();
                //Se añade la urls del bitmap escogido
                mAnuncio.getImagenes().add(respuesta.getData().getLink());
                mPresenter.publishNewAdvert(mAnuncio);
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {
                System.out.println();
            }
        });
    }
}
