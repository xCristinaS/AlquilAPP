package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import c.proyecto.R;
import c.proyecto.dialog_fragments.CaracteristicasUsuarioDialogFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_presenters.ProfilePresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public class VerPerfilActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "extra_user";
    private static final String EXTRA_ANUNCIO = "extra_anuncio";
    private static final String EXTRA_CURRENT_USER = "current_user";

    private TextView lblNombre, lblNacionalidad, lblDescripcionNoDisponible, lblDescripcion, lblProfesion;
    private ImageView imgFoto, imgDescripcion1, imgDescripcion2, imgDescripcion3, imgMessage;
    private Usuario mUser, currentUser;
    private Anuncio mAnuncio;
    private ProfilePresenter mPresenter;
    private RelativeLayout groupDescripcion;

    public static void start(Activity activity, Usuario user) {
        Intent intent = new Intent(activity, VerPerfilActivity.class);
        intent.putExtra(EXTRA_USER, user);

        activity.startActivity(intent);
    }

    public static void start(Activity activity, Usuario user, Anuncio anuncio, Usuario currentUser) {
        Intent intent = new Intent(activity, VerPerfilActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        intent.putExtra(EXTRA_CURRENT_USER, currentUser);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        mUser = getIntent().getParcelableExtra(EXTRA_USER);
        mAnuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        currentUser = getIntent().getParcelableExtra(EXTRA_CURRENT_USER);
        mPresenter = ProfilePresenter.getPresentador(this);
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, currentUser));
        initView();
        recuperarDatos();
    }

    private void initView() {
        imgFoto = (ImageView) findViewById(R.id.slider);
        lblNombre = (TextView) findViewById(R.id.lblNombre);
        lblNacionalidad = (TextView) findViewById(R.id.lblNacionalidad);
        lblProfesion = (TextView) findViewById(R.id.lblProfesion);
        lblDescripcionNoDisponible = (TextView) findViewById(R.id.lblDescripcionNoDisponible);
        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion);
        imgDescripcion1 = (ImageView) findViewById(R.id.imgDescripcion1);
        imgDescripcion2 = (ImageView) findViewById(R.id.imgDescripcion2);
        imgDescripcion3 = (ImageView) findViewById(R.id.imgDescripcion3);
        groupDescripcion = (RelativeLayout) findViewById(R.id.groupDescripcion);

        if (mAnuncio != null) {
            imgMessage = (ImageView) findViewById(R.id.imgMessage);
            mPresenter.getMessageIfConverExist(mAnuncio, mUser.getKey());
        }
    }

    public void messageIfConverExistObtained(final MessagePojo m) {
        if (m == null)
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSenMessageDialog();
                }
            });
        else {
            imgMessage.setImageResource(R.drawable.ic_chat);
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConversationActivity.start(VerPerfilActivity.this, m, currentUser);
                }
            });
        }
        imgMessage.setVisibility(View.VISIBLE);
    }

    private void showSenMessageDialog() {
        final EditText txtMensaje;
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.dialog_fragment_send_message, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Enviar mensaje");
        txtMensaje = (EditText) dialogView.findViewById(R.id.txtMensaje);
        dialogView.findViewById(R.id.imgEnviar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    mPresenter.sendNewMessage(new MessagePojo(currentUser, mAnuncio.getTitulo(), txtMensaje.getText().toString(), new Date()), mUser.getKey());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void recuperarDatos() {
        Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgFoto);
        cargarBarritas();
        cargarItemsDescriptivos();
        lblNombre.setText(mUser.getNombre());
        lblNacionalidad.setText(mUser.getNacionalidad());
        if (mUser.getComentario_desc() == null)
            lblDescripcionNoDisponible.setVisibility(View.VISIBLE);
        else
            lblDescripcion.setText(mUser.getComentario_desc());

        if (mUser.getProfesion() != null)
            lblProfesion.setText(mUser.getProfesion());

    }

    private void cargarBarritas() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frmBarritas, CaracteristicasUsuarioDialogFragment.newInstance(mUser, false)).commit();
    }

    private void cargarItemsDescriptivos() {
        ArrayList<String> items = mUser.getIdDrawItemsDescriptivos();
        RelativeLayout containerDescripcion2 = (RelativeLayout) findViewById(R.id.containerDescripcion2);
        RelativeLayout containerDescripcion3 = (RelativeLayout) findViewById(R.id.containerDescripcion3);

        if (items.size() > 0) {
            if (items.size() == 1) {
                imgDescripcion1.setImageResource(getResources().getIdentifier(items.get(0), "drawable", getPackageName()));
                containerDescripcion2.setVisibility(View.GONE);
                containerDescripcion3.setVisibility(View.GONE);
            } else if (items.size() == 2) {
                imgDescripcion1.setImageResource(getResources().getIdentifier(items.get(0), "drawable", getPackageName()));
                imgDescripcion3.setImageResource(getResources().getIdentifier(items.get(1), "drawable", getPackageName()));
                containerDescripcion2.setVisibility(View.GONE);
            } else {
                imgDescripcion1.setImageResource(getResources().getIdentifier(items.get(0), "drawable", getPackageName()));
                imgDescripcion2.setImageResource(getResources().getIdentifier(items.get(1), "drawable", getPackageName()));
                imgDescripcion3.setImageResource(getResources().getIdentifier(items.get(2), "drawable", getPackageName()));
            }
        } else
            groupDescripcion.setVisibility(View.GONE);
    }
}
