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
import c.proyecto.adapters.MessagesRecyclerViewAdapter;
import c.proyecto.fragments.MessagesFragment;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_views_interfaces.ConversationActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.mvp_models.Usuario;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.mvp_presenters.ConversationPresenter;

public class ConversationActivity extends AppCompatActivity implements ConversationActivityOps, MessagesRecyclerViewAdapter.ConversationManager {

    private static final String EXTRA_MENSAJE = "mensaje_extra";
    private static final String EXTRA_USER = "user_extra";

    private MessagePojo mensaje;
    private ImageView imgEnviar;
    private EditText txtMensaje;
    private ConversationPresenter mPresenter;
    private Usuario user;

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
        initViews();
    }

    private void initViews() {
        mPresenter = ConversationPresenter.getPresentador(this);
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, user));

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
        MainActivity.getmPresenter().requestUserMessages(user);
        super.onDestroy();
    }

    @Override
    public void removeMessage(MessagePojo m) {
        mPresenter.removeMessage(m);
    }
}
