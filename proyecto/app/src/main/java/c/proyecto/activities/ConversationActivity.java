package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;

import c.proyecto.R;
import c.proyecto.adapters.MessagesAdapter;
import c.proyecto.fragments.MessagesFragment;
import c.proyecto.interfaces.ConversationActivityOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.presenters.ConversationPresenter;

public class ConversationActivity extends AppCompatActivity implements ConversationActivityOps, MessagesAdapter.ConversationManager {

    private static final String EXTRA_MENSAJE = "mensaje_extra";
    private static final String EXTRA_USER = "user_extra";
    private static final String EXTRA_ANUNCIO = "anuncio";

    private MessagePojo mensaje;
    private ImageView imgEnviar;
    private EditText txtMensaje;
    private ConversationPresenter mPresenter;
    private Usuario user;
    private Anuncio anuncio;

    public static void start(Context c, MessagePojo mensaje, Usuario user, Anuncio anuncio) {
        Intent intent = new Intent(c, ConversationActivity.class);
        intent.putExtra(EXTRA_MENSAJE, mensaje);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mensaje = getIntent().getParcelableExtra(EXTRA_MENSAJE);
        user = getIntent().getParcelableExtra(EXTRA_USER);
        anuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        initViews();
    }

    private void initViews() {
        mPresenter = ConversationPresenter.getPresentador(this);
        if (mensaje != null) {
            mPresenter.userConversationRequested(user, mensaje);
            getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, MessagesFragment.newInstance(true, mensaje.getKeyReceptor())).commit();
        } else if (anuncio != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, MessagesFragment.newInstance(true, anuncio.getAnunciante())).commit();
        imgEnviar = (ImageView) findViewById(R.id.imgEnviar);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);

        imgEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    if (anuncio != null) {
                        // user aqui es el que creo el anuncio, por eso no vale.
                        MessagePojo m = new MessagePojo(user, anuncio.getTitulo(), txtMensaje.getText().toString(), new Date());
                        m.setKeyReceptor(anuncio.getAnunciante());
                        mPresenter.sendMessage(m, user.getKey(), true);
                    } else {
                        mPresenter.sendMessage(new MessagePojo(user, mensaje.getTituloAnuncio(), txtMensaje.getText().toString(), new Date()), mensaje.getEmisor().getKey(), false);
                    }
                    txtMensaje.setText("");
                }
            }
        });

        //mensaje.getEmisor().getFoto(); // FOTO DEL QUE TE HA HABLADO
        //mensaje.getEmisor().getNombre(); // NOMBRE DEL QUE TE HA HABLADO
        //mensaje.getTituloAnuncio(); // TITULO DEL ANUNCIO
    }

    @Override
    public void messageHasBeenObtained(MessagePojo m) {
        if (getSupportFragmentManager().findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).addItem(m);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachFirebaseListeners();
        super.onDestroy();
    }

    @Override
    public void removeMessage(MessagePojo m) {
        mPresenter.removeMessage(m);
    }
}
