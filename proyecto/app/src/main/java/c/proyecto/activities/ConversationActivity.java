package c.proyecto.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.MessagesRecyclerViewAdapter;
import c.proyecto.fragments.MessagesFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.ConversationActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.mvp_presenters.ConversationPresenter;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity implements ConversationActivityOps, MessagesRecyclerViewAdapter.ConversationManager {

    private static final String EXTRA_MENSAJE = "mensaje_extra";
    private static final String EXTRA_USER = "user_extra";

    private MessagePojo mensaje;
    private ImageView imgEnviar;
    private CircleImageView imgContacto;
    private TextView lblNombreContacto, lblTituloAnuncio;
    private EditText txtMensaje;
    private ConversationPresenter mPresenter;
    private Usuario user;
    private Toolbar toolbar;


    public static void start(Context c, MessagePojo mensaje, Usuario user) {
        Intent intent = new Intent(c, ConversationActivity.class);
        intent.putExtra(EXTRA_MENSAJE, mensaje);
        intent.putExtra(EXTRA_USER, user);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mensaje = getIntent().getParcelableExtra(EXTRA_MENSAJE);
        user = getIntent().getParcelableExtra(EXTRA_USER);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initViews();
    }

    private void initViews() {
        imgContacto = (CircleImageView) findViewById(R.id.imgContacto);
        lblNombreContacto = (TextView) findViewById(R.id.lblNombreContacto);
        lblTituloAnuncio = (TextView) findViewById(R.id.lblTituloAnuncio);
        mPresenter = ConversationPresenter.getPresentador(this);
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, user));
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, user));

        if (mensaje != null) {
            mPresenter.userConversationRequested(mensaje);
            getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, MessagesFragment.newInstance(true, mensaje.getKeyReceptor())).commit();
        }

        imgEnviar = (ImageView) findViewById(R.id.imgEnviar);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);

        imgEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    if (mensaje instanceof MessagePojoWithoutAnswer) {
                        MessagePojo m = new MessagePojo(user, mensaje.getTituloAnuncio(), txtMensaje.getText().toString(), new Date());
                        m.setKeyReceptor(mensaje.getKeyReceptor());
                        mPresenter.sendMessage(m, mensaje.getKeyReceptor(), true);
                    } else
                        mPresenter.sendMessage(new MessagePojo(user, mensaje.getTituloAnuncio(), txtMensaje.getText().toString(), new Date()), mensaje.getEmisor().getKey(), false);

                    txtMensaje.setText("");
                }
            }
        });
        confToolbar();
    }

    private void confToolbar() {
        final Usuario usuarioAux;
        String tituloAnuncio = mensaje.getTituloAnuncio();
        if(tituloAnuncio.length()>Constantes.LENGTH_TITULO_ANUNCIO_CONVERSATION_ACTIVITY){
            tituloAnuncio = mensaje.getTituloAnuncio().substring(0, Constantes.LENGTH_TITULO_ANUNCIO_CONVERSATION_ACTIVITY);
            tituloAnuncio = tituloAnuncio.trim();
            tituloAnuncio+="...";
        }

        lblTituloAnuncio.setText(tituloAnuncio);
        if (mensaje instanceof MessagePojoWithoutAnswer)
            usuarioAux = ((MessagePojoWithoutAnswer) mensaje).getReceptor();
        else
            usuarioAux = mensaje.getEmisor();

        lblNombreContacto.setText(usuarioAux.getNombre());
        Picasso.with(this).load(usuarioAux.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgContacto);
        View.OnClickListener perfilOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(ConversationActivity.this, usuarioAux);
            }
        };

        imgContacto.setOnClickListener(perfilOnClickListener);
        lblNombreContacto.setOnClickListener(perfilOnClickListener);

        lblTituloAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getAdvertFromTitle(mensaje.getTituloAnuncio());
            }
        });
    }

    @Override
    public void advertObtained(Anuncio a){
        DetallesAnuncioActivity.start(this, a, AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS, user);
    }

    @Override
    public void messageHasBeenObtained(MessagePojo m) {
        if (getSupportFragmentManager().findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).addItem(m);
    }


    @Override
    public void removeMessage(MessagePojo m) {
        mPresenter.removeMessage(m);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachFirebaseListeners();
        super.onDestroy();
    }
}
