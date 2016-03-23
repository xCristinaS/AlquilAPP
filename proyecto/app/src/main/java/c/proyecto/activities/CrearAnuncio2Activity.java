package c.proyecto.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.fragments.SeleccionPrestacionesDialogFragment;
import c.proyecto.pojo.Prestacion;

public class CrearAnuncio2Activity extends AppCompatActivity implements PrestacionesAdapter.IPrestacionAdapter {

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
    private List<Prestacion> mPrestacionesSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio2);
        //Comprobar si es editando o no
        mPrestacionesSelected = new ArrayList<>();
        initViews();
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
        confRecyclerPrestaciones();
        confRecyclerHuespedes();
    }

    private void confRecyclerPrestaciones() {
        rvPrestaciones.setHasFixedSize(true);
        mPrestacionesAdapter = new PrestacionesAdapter(mPrestacionesSelected, this);
        rvPrestaciones.setAdapter(mPrestacionesAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());
    }

    private void confRecyclerHuespedes() {
        txtTituloAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrestacionesDialog();
            }
        });
    }

    @Override
    public void onPrestacionClicked() {

    }

    private void showPrestacionesDialog(){
        FragmentManager fm = getSupportFragmentManager();
        SeleccionPrestacionesDialogFragment.newInstance(new ArrayList<Prestacion>()).show(fm, "tag");
    }
}
