package c.proyecto.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.fragments.DetallesAnuncioFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.AdvertsDetailsActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.pojo.MessagePojo;

public class DetallesAnuncioActivity extends AppCompatActivity implements AdvertsDetailsActivityOps, DetallesAnuncioFragment.IDetallesAnuncioFragmentListener, DetallesAnuncioFragment.OnDetallesAnuncioFragmentClic {


    public static final String ACTION_CLOSE_ACTIVITY = "c.proyecto.activities.DetallesAnuncioActivity.CLOSE_ACTIVITY";

    private static final String EXTRA_ANUNCIO = "anuncio";
    private static final String EXTRA_ADVERT_TYPE = "advert_type";
    private static final String EXTRA_USER = "user";

    private static AdvertsDetailsPresenter mPresenter;
    private Anuncio anuncio;
    private int advertType;
    private Usuario currentUser;
    private BroadcastReceiver receiver;

    public static void start(Context context, Anuncio anuncio, int advertType, Usuario u) {
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showAdvertHasBeenRemovedDialog();
            }
        };

        anuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        advertType = getIntent().getIntExtra(EXTRA_ADVERT_TYPE, -1);
        mPresenter = AdvertsDetailsPresenter.getPresentador(this);
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, currentUser));
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, currentUser));
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));

        if (advertType != AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            mPresenter.advertPublisherRequested(anuncio.getAnunciante());
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio, advertType, currentUser, currentUser)).commit();
    }

    private void showAdvertHasBeenRemovedDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.dialog_removed_advert, null);
        dialog.setView(dialogView);
        dialog.setTitle("Ups! El anuncio ha sido eliminado");
        dialogView.findViewById(R.id.btnAceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onAdvertPublisherRequestedResponsed(Usuario u) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio, advertType, u, currentUser)).commit();
    }

    @Override
    public void onImgEditClicked(Anuncio advert, Usuario user) {
        CrearAnuncio1Activity.startForResult(this, advert, user, CrearAnuncio1Activity.RC_EDITAR_ANUNCIO);
    }

    @Override
    public void updateAdvert(Anuncio anuncio) {
        DetallesAnuncioFragment f = (DetallesAnuncioFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f != null)
            f.setmAnuncio(anuncio);
    }

    @Override
    public void onImgSubClicked(Anuncio a) {
        mPresenter.userNewSubRequested(a);
    }

    @Override
    public void onImgUnSubClicked(Anuncio a) {
        mPresenter.unSubRequested(a);
    }

    @Override
    public void onNewMessageClic(MessagePojo m, String keyReceptor) {
        mPresenter.sendNewMessage(m, keyReceptor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CrearAnuncio1Activity.RC_EDITAR_ANUNCIO:
                    ((DetallesAnuncioFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).setmAnuncio((Anuncio) data.getParcelableExtra(CrearAnuncio1Activity.EXTRA_ANUNCIO_RESULT));
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filtro = new IntentFilter(ACTION_CLOSE_ACTIVITY);
        registerReceiver(receiver, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public static AdvertsDetailsPresenter getmPresenter() {
        return mPresenter;
    }
}


