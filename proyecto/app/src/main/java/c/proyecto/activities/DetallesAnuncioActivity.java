package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import c.proyecto.R;
import c.proyecto.fragments.DetallesAnuncioFragment;
import c.proyecto.interfaces.AdvertsDetailsActivityOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.AdvertsDetailsPresenter;

public class DetallesAnuncioActivity extends AppCompatActivity implements AdvertsDetailsActivityOps, DetallesAnuncioFragment.IDetallesAnuncioFragmentListener{


    private static final String EXTRA_ANUNCIO = "anuncio";
    private static final String EXTRA_ADVERT_TYPE = "advert_type";
    private static final String EXTRA_USER = "user";
    private AdvertsDetailsPresenter mPresenter;
    private Anuncio anuncio;
    private int advertType;

    public static void start(Context context, Anuncio anuncio, int advertType, Usuario u){
        Intent intent = new Intent(context, DetallesAnuncioActivity.class);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        intent.putExtra(EXTRA_ADVERT_TYPE, advertType);
        intent.putExtra(EXTRA_USER, u);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_anuncio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Usuario u = getIntent().getParcelableExtra(EXTRA_USER);
        anuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        advertType = getIntent().getIntExtra(EXTRA_ADVERT_TYPE, -1);
        mPresenter = AdvertsDetailsPresenter.getPresentador(this);
        if (u == null)
            mPresenter.advertPublisherRequested(anuncio.getAnunciante());
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio, advertType, u)).commit();
    }

    @Override
    public void onAdvertPublisherRequestedResponsed(Usuario u) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio, advertType, u)).commit();
    }

    public AdvertsDetailsPresenter getmPresenter() {
        return mPresenter;
    }

    @Override
    public void onImgEditClicked(Anuncio advert, Usuario user) {
        CrearAnuncio1Activity.startForResult(this, advert, user, CrearAnuncio1Activity.RC_EDITAR_ANUNCIO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CrearAnuncio1Activity.RC_EDITAR_ANUNCIO:
                    ((DetallesAnuncioFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).setmAnuncio((Anuncio) data.getParcelableExtra(CrearAnuncio1Activity.EXTRA_ANUNCIO_RESULT));
                    break;
            }
        }
    }
}


