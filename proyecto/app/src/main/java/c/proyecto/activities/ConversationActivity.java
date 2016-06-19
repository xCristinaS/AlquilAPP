package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.MessagesRecyclerViewAdapter;
import c.proyecto.fragments.MessagesFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_presenters.ConversationPresenter;
import c.proyecto.mvp_views_interfaces.ConversationActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.pojo.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity implements ConversationActivityOps, MessagesRecyclerViewAdapter.ConversationManager {

    private static final String EXTRA_MENSAJE = "mensaje_extra";
    private static final String EXTRA_USER = "user_extra";
    private static final String TAG_FR_MSG = "fragmento_mensajes";
    private static final String EXTRA_OPEN_FROM_ADVERT_DETAILS = "open_from_advert_details";

    private MessagePojo mensaje;
    private ImageView imgEnviar;
    private CircleImageView imgContacto;
    private TextView lblNombreContacto, lblTituloAnuncio;
    private EditText txtMensaje;
    private ConversationPresenter mPresenter;
    private Usuario user;
    private Toolbar toolbar;
    private FragmentManager mFragmentManager;
    private boolean openFromAdvertDetails;

    public static void start(Context c, MessagePojo mensaje, Usuario user, boolean openFromAdvertDetails) {
        Intent intent = new Intent(c, ConversationActivity.class);
        intent.putExtra(EXTRA_MENSAJE, mensaje);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_OPEN_FROM_ADVERT_DETAILS, openFromAdvertDetails);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mensaje = getIntent().getParcelableExtra(EXTRA_MENSAJE);
        user = getIntent().getParcelableExtra(EXTRA_USER);
        openFromAdvertDetails = getIntent().getBooleanExtra(EXTRA_OPEN_FROM_ADVERT_DETAILS, false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();
        //Cierra el teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initViews();
    }

    private void initViews() {
        imgContacto = (CircleImageView) findViewById(R.id.imgContacto);
        lblNombreContacto = (TextView) findViewById(R.id.lblNombreContacto);
        lblTituloAnuncio = (TextView) findViewById(R.id.lblTituloAnuncio);
        mPresenter = ConversationPresenter.getPresentador(this);
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, user));
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, user));

        requestUserConversation();

        imgEnviar = (ImageView) findViewById(R.id.imgEnviar);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);

        imgEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    enviarMensaje();
                }
            }
        });
        confToolbar();
    }

    private void requestUserConversation() {
        if (mensaje != null) {
            mPresenter.userConversationRequested(mensaje, openFromAdvertDetails);
            MessagesFragment f = MessagesFragment.newInstance(true, mensaje.getKeyReceptor());
            mFragmentManager.beginTransaction().replace(R.id.frmContenido, f, TAG_FR_MSG).commit();
        }
    }

    private void enviarMensaje() {
        if (mensaje instanceof MessagePojoWithoutAnswer) {
            MessagePojo m = new MessagePojo(user, mensaje.getTituloAnuncio(), txtMensaje.getText().toString(), new Date());
            m.setKeyReceptor(mensaje.getKeyReceptor());
            mPresenter.sendMessage(m, mensaje.getKeyReceptor(), true);
        } else
            mPresenter.sendMessage(new MessagePojo(user, mensaje.getTituloAnuncio(), txtMensaje.getText().toString(), new Date()), mensaje.getEmisor().getKey(), false);

        txtMensaje.setText("");
    }

    private void confToolbar() {
        final Usuario usuarioAux;

        lblTituloAnuncio.setText(mensaje.getTituloAnuncio());
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
    public void advertObtained(Anuncio a) {
        if (a != null)
            if (!a.getAnunciante().equals(user.getKey())) {
                if (a.getSolicitantes().containsKey(user.getKey()))
                    DetallesAnuncioActivity.start(this, a, AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS, user, true);
                else
                    DetallesAnuncioActivity.start(this, a, AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS, user, true);
            } else
                DetallesAnuncioActivity.start(this, a, AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS, user, true);
    }

    @Override
    public void messageHasBeenObtained(MessagePojo m) {
        if (mFragmentManager.findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) mFragmentManager.findFragmentById(R.id.frmContenido)).addItem(m);
    }


    @Override
    public void removeMessage(MessagePojo m) {
        mPresenter.removeMessage(m);
    }

    @Override
    public void allMessagesObtained() {
        if (mFragmentManager.findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) mFragmentManager.findFragmentById(R.id.frmContenido)).getmAdapter().allMessagesObtained();
    }

    @Override
    protected void onDestroy() {
        desvincularPresenter();
        super.onDestroy();
    }

    private void desvincularPresenter() {
        mPresenter.detachFirebaseListeners();
        mPresenter.liberarMemoria();
    }
}
