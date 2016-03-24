package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.fragments.SeleccionPrestacionesDialogFragment;
import c.proyecto.models.Anuncio;
import c.proyecto.pojo.Prestacion;

public class CrearAnuncio2Activity extends AppCompatActivity implements PrestacionesAdapter.IPrestacionAdapter, SeleccionPrestacionesDialogFragment.ICallBackOnDismiss{

    private static final String INTENT_ANUNCIO = "intentAnuncio2";
    private static final String TAG_DIALOG_PRESTACIONES = "TAG_PRESTACIONES";
    private TextView txtTituloAnuncio;
    private ImageView imgCasa;
    private ImageView imgHabitacion;
    private ImageView imgPiso;
    private TextView txtDireccion;
    private TextView txtNum;
    private TextView txtPoblacion;
    private TextView txtProvincia;
    private RecyclerView rvPrestaciones;
    private TextView txtCamas;
    private TextView txtToilets;
    private TextView txtTamano;
    private TextView txtDescripcion;
    private TextView txtPrecio;
    private RecyclerView rvHuespedes;

    private PrestacionesAdapter mPrestacionesAdapter;
    private Anuncio mAnuncio;

    public static void start(Context context, Anuncio anuncio){
        Intent intent = new Intent(context, CrearAnuncio2Activity.class);
        intent.putExtra(INTENT_ANUNCIO, anuncio);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio2);

        mAnuncio = getIntent().getParcelableExtra(INTENT_ANUNCIO);
        //Si se entra creando
        if (mAnuncio == null){
            mAnuncio = new Anuncio();
            mAnuncio.setPrestaciones(new ArrayList<Prestacion>());
            initViews();
        }else{
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
        switch (mAnuncio.getTipo_vivienda()){
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

    private void showPrestacionesDialog(){
        FragmentManager fm = getSupportFragmentManager();
        if(mAnuncio.getPrestaciones() == null)
            SeleccionPrestacionesDialogFragment.newInstance(new ArrayList<Prestacion>()).show(fm, TAG_DIALOG_PRESTACIONES);
        else
            SeleccionPrestacionesDialogFragment.newInstance(mAnuncio.getPrestaciones()).show(fm, TAG_DIALOG_PRESTACIONES);
    }


    @Override
    public void onDismiss() {
        mPrestacionesAdapter.actualizarAdapter();
    }

    private void tintVivienda(ImageView view){
        imgCasa.clearColorFilter();
        imgHabitacion.clearColorFilter();
        imgPiso.clearColorFilter();

        view.setColorFilter(getResources().getColor(R.color.colorAccent));
    }
}
