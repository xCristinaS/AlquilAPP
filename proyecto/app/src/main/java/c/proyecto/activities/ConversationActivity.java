package c.proyecto.activities;

import android.app.Activity;
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
import c.proyecto.models.Message;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.presenters.ConversationPresenter;

public class ConversationActivity extends AppCompatActivity implements ConversationActivityOps, MessagesAdapter.ConversationManager {

    private static final String EXTRA_MENSAJE = "mensaje_extra";
    private static final String EXTRA_USER = "user_extra";

    private MessagePojo mensaje;
    private ImageView imgEnviar;
    private EditText txtMensaje;
    private ConversationPresenter mPresenter;
    private Usuario user;

    public static void start(Activity a, MessagePojo mensaje, Usuario user) {
        Intent intent = new Intent(a, ConversationActivity.class);
        intent.putExtra(EXTRA_MENSAJE, mensaje);
        intent.putExtra(EXTRA_USER, user);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        if (getIntent().hasExtra(EXTRA_MENSAJE) && getIntent().hasExtra(EXTRA_USER)) {
            mensaje = getIntent().getParcelableExtra(EXTRA_MENSAJE);
            user = getIntent().getParcelableExtra(EXTRA_USER);
        }

        initViews();
    }

    private void initViews() {
        mPresenter = ConversationPresenter.getPresentador(this);
        mPresenter.userConversationRequested(user, mensaje);
        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, MessagesFragment.newInstance(true)).commit();
        imgEnviar = (ImageView) findViewById(R.id.imgEnviar);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);

        imgEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    mPresenter.sendMessage(new MessagePojo(user, mensaje.getTituloAnuncio(), txtMensaje.getText().toString(), new Date()), mensaje.getEmisor().getKey());
                    txtMensaje.setText("");
                }
            }
        });
    }

    @Override
    public void messageHasBeenObtained(MessagePojo m) {
        if (getSupportFragmentManager().findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).getmAdapter().addItem(m);
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
