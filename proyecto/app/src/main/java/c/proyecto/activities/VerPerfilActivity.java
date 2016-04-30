package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.dialog_fragments.CaracteristicasUsuarioDialogFragment;
import c.proyecto.pojo.Usuario;

public class VerPerfilActivity extends AppCompatActivity {

    private static final String INTENT_USER = "IntentUser";
    private ImageView imgFoto;
    private RecyclerView rvDescripcion;
    private TextView lblNombre, lblNacionalidad, lblDescripcionNoDisponible, lblDescripcion, lblProfesion;
    private ImageView imgDescripcion1, imgDescripcion2, imgDescripcion3;
    private Usuario mUser;


    public static void start(Activity activity, Usuario user){
        Intent intent = new Intent(activity, VerPerfilActivity.class);
        intent.putExtra(INTENT_USER, user);

        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        mUser = getIntent().getParcelableExtra(INTENT_USER);
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
    }

    private void recuperarDatos() {
        Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgFoto);
        cargarBarritas();
        cargarItemsDescriptivos();
        lblNombre.setText(mUser.getNombre());
        lblNacionalidad.setText(mUser.getNacionalidad());
        if(mUser.getComentario_desc() == null)
            lblDescripcionNoDisponible.setVisibility(View.VISIBLE);
        else
            lblDescripcion.setText(mUser.getComentario_desc());

        if(mUser.getProfesion() != null)
            lblProfesion.setText(mUser.getProfesion());

    }

    private void cargarBarritas() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frmBarritas, CaracteristicasUsuarioDialogFragment.newInstance(mUser, false)).commit();
    }

    private void cargarItemsDescriptivos() {
        ArrayList<Integer> items = mUser.getIdDrawItemsDescriptivos();

        if(items.size() > 0){
            if(items.size() == 1)
                imgDescripcion2.setImageResource(items.get(0));
            else if(items.size() == 2){
                imgDescripcion1.setImageResource(items.get(0));
                imgDescripcion2.setImageResource(items.get(1));
            }
            else{
                imgDescripcion1.setImageResource(items.get(0));
                imgDescripcion2.setImageResource(items.get(1));
                imgDescripcion3.setImageResource(items.get(2));
            }
        }
    }


}
