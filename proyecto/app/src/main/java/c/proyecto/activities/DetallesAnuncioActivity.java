package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.fragments.DetallesAnuncioFragment;
import c.proyecto.interfaces.AdvertsDetailsActivityOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.AdvertsDetailsPresenter;

public class DetallesAnuncioActivity extends AppCompatActivity implements AdvertsDetailsActivityOps{


    private static final String EXTRA_ANUNCIO = "anuncio";
    private static final String EXTRA_ADVERT_TYPE = "advert_type";
    private static final String EXTRA_USER = "user";
    private AdvertsDetailsPresenter mPresenter;
    private Anuncio anuncio;
    private int advertType;
    private Usuario currentUser;

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
        currentUser = getIntent().getParcelableExtra(EXTRA_USER);
        anuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        advertType = getIntent().getIntExtra(EXTRA_ADVERT_TYPE, -1);
        mPresenter = AdvertsDetailsPresenter.getPresentador(this);
        if (advertType != AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            mPresenter.advertPublisherRequested(anuncio.getAnunciante());
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio, advertType, currentUser, currentUser)).commit();
    }

    @Override
    public void onAdvertPublisherRequestedResponsed(Usuario u) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio, advertType, u, currentUser)).commit();
    }

    public AdvertsDetailsPresenter getmPresenter() {
        return mPresenter;
    }
}
